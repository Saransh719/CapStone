package com.example.capstone

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.capstone.ui.theme.CapStoneTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            MenuItems::class.java,
            "records.db"
        ).build()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CapStoneTheme {
                LaunchedEffect(Unit) {
                    lifecycleScope.launch {
                        val response= client.get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json")
                        val menuData = response.body<MenuNetworkData>()
                        val items = menuData.menuItems.map {
                            Item(
                                id = it.id,
                                title = it.title,
                                description = it.description,
                                price = it.price,
                                image = it.image,
                                category = it.category
                            )
                        }
                        db.ItemsDao().insertAll(items)
                    }
                }
                val navController = rememberNavController()
                val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)  // Default to false
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController, startDestination = if (isLoggedIn) "Home" else "onBoarding") {
                        composable(OnBoarding.route) {
                            OnBoarding(innerPadding, editor,navController)
                        }
                        composable(Home.route) {
                            Home(navController,innerPadding,db)
                        }
                        composable(Profile.route) {
                            Profile(navController,innerPadding,sharedPreferences,editor)
                        }
                    }

                }
            }
        }
    }
}
