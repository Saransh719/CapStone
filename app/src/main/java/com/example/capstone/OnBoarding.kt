package com.example.capstone

import android.content.SharedPreferences
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.capstone.ui.theme.green
import com.example.capstone.ui.theme.yellow

@Composable
fun OnBoarding(
    innerPadding: PaddingValues,
    editor: SharedPreferences.Editor,
    navController: NavHostController
) {
    val context = LocalContext.current
    var firstName = remember { mutableStateOf("") }
    var lastName = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Image(painter = painterResource(R.drawable.logo) , contentDescription = "Logo"
                , modifier = Modifier.width(200.dp).height(60.dp).padding(10.dp, bottom = 20.dp), contentScale = ContentScale.Fit)
        }
        Row(modifier = Modifier.fillMaxWidth().background(color = green), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Lets Get To Know You"  , color = Color.White , style = TextStyle(fontSize = 20.sp), modifier = Modifier.padding(vertical = 40.dp))
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Personal Information"  , style = TextStyle(fontSize = 23.sp), modifier = Modifier.padding(vertical = 40.dp , horizontal = 10.dp))
        }

        Column {
            TakeInput("First Name",firstName)
            TakeInput("Last Name",lastName)
            TakeInput("Email",email)
            Button(onClick = {
                if (firstName.value.isBlank() or lastName.value.isBlank() or !isValidEmail(email.value))
                {
                    Toast.makeText(context, "Registration unsuccessful. Please enter all data", Toast.LENGTH_SHORT).show()
                }
                else{
                    editor.putString("firstName", firstName.value)
                    editor.putString("lastName", lastName.value)
                    editor.putString("email", email.value)
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()
                    Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                    navController.navigate(Home.route){
                        popUpTo(OnBoarding.route) { inclusive = true }
                    }
                }

            }, colors = ButtonDefaults.buttonColors(containerColor = yellow), modifier = Modifier.fillMaxWidth() ) {
                Text("Register", color =Color.Black,)
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakeInput(field: String, variable: MutableState<String>){
    Column (modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)){
        Text(text = field , style = TextStyle(fontSize = 15.sp))
        TextField(
            value = variable.value,
            onValueChange = {variable.value=it},
            label = { Text(field) },
            modifier = Modifier
                .padding(10.dp)
                .border(1.dp, Color.Gray, shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent),
            singleLine = true
        )
    }

}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}