package com.example.capstone

import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.capstone.ui.theme.yellow

@Composable
fun Profile(
    navController: NavHostController,
    innerPadding: PaddingValues,
    sharedPreferences: SharedPreferences,
    editor: SharedPreferences.Editor,
) {
    val firstName=sharedPreferences.getString("firstName",  "")
    val lastName=sharedPreferences.getString("lastName",  "")
    val email=sharedPreferences.getString("email",  "")
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(innerPadding)){
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Image(painter = painterResource(R.drawable.logo) , contentDescription = "Logo"
                , modifier = Modifier.width(200.dp).height(60.dp).padding(10.dp), contentScale = ContentScale.Fit)
        }
        Show("First Name" , firstName.toString())
        Show("Last Name" , lastName.toString())
        Show("Email" , email.toString())
        Button(onClick = {
            navController.navigate(OnBoarding.route){
                popUpTo(Home.route) { inclusive = true }
            }
            editor.clear().apply()
            Toast.makeText(context, "Logged out successfully!", Toast.LENGTH_SHORT).show()
        }, colors = ButtonDefaults.buttonColors(containerColor = yellow), modifier = Modifier.fillMaxWidth().padding(top = 20.dp, start = 10.dp, end = 10.dp) ) {
            Text("Logout", color =Color.Black,)
        }
    }


}

@Composable
fun Show(text: String, value: String){
    Column (modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 5.dp)){
        Text(text = text , style = TextStyle(fontSize = 15.sp))
        Text(value, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(1.dp, Color.Gray, shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                .padding(10.dp),
            )
    }

}