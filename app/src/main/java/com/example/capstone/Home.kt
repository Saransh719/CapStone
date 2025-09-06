package com.example.capstone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.capstone.ui.theme.green
import com.example.capstone.ui.theme.yellow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController, innerPadding: PaddingValues, db: MenuItems)
{
    var filter by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .padding(10.dp),
                contentScale = ContentScale.Fit
            )
            Image(
                painter = painterResource(R.drawable.profile),
                contentDescription = "Profile",
                modifier = Modifier
                    .width(100.dp)
                    .height(60.dp)
                    .padding(10.dp)
                    .clickable(onClick = { navController.navigate(Profile.route) }),
                contentScale = ContentScale.Fit
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(color = green)
            .padding(horizontal = 5.dp)) {
            Row {
                Column(Modifier.fillMaxWidth(0.5f)) {
                    Text(
                        text = "Little Lemon",
                        color = yellow,
                        style = TextStyle(fontSize = 30.sp),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Text(
                        text = "Chicago",
                        color = Color.White,
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Text(
                        text = "We are a family-owned Mediterranean restaurant, focused on traditional recipes served with a modern twist",
                        color = Color.White,
                        style = TextStyle(fontSize = 15.sp),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
                Image(painter = painterResource(R.drawable.hero_image) , contentDescription = "Hero", modifier = Modifier
                    .height(150.dp)
                    .padding(start = 50.dp,top =10.dp), contentScale = ContentScale.Fit)
            }
            OutlinedTextField(
                value = filter,
                onValueChange = {filter=it},
                placeholder = { Text("Enter Search Phrase") },
                modifier = Modifier
                    .padding(10.dp),
                    shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
            )
        }
        val categories = arrayOf("All", "Starters", "Mains", "Desserts")
        var category_active = rememberSaveable { mutableIntStateOf(0) }
            LazyRow {
                itemsIndexed(categories) { index,category -> Display_Category(index,category , category_active) }
            }
        val menuItems by db.ItemsDao().getReadings().observeAsState(emptyList())
        var newMenuItems = menuItems
        if (filter.isNotBlank())
        {
            newMenuItems = newMenuItems.filter { item -> item.title.contains(filter, ignoreCase = true) }
        }
        if (category_active.intValue !=0)
        {
            newMenuItems= newMenuItems.filter { item -> item.category==categories[category_active.intValue].lowercase()}
        }
        Items(newMenuItems)
    }


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Items(menuItems: List<Item>)
{
    LazyColumn (modifier = Modifier.padding(10.dp)){

        items(menuItems){
            item ->
            Column {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
                Row {
                    Column(modifier = Modifier.fillMaxWidth(0.75f)) {
                        Text(text = item.title , style = TextStyle(fontWeight = FontWeight.Bold , fontSize = 16.sp), modifier = Modifier.padding(vertical = 5.dp))
                        Text(text = item.description , style= TextStyle(fontSize = 12.sp), modifier = Modifier.padding(bottom = 5.dp))
                        Text(text ="$${item.price}", style= TextStyle(fontSize = 12.sp), modifier = Modifier.padding(bottom = 5.dp))
                    }
                    GlideImage(
                        model = item.image,
                        contentDescription = "Menu Item Image",
                        modifier = Modifier.height(60.dp),
                        contentScale = ContentScale.Crop
                    )
                }

            }
        }
    }
}

@Composable
fun Display_Category(index : Int, category : String , active : MutableState<Int>)
{

    Button(onClick = {active.value=index}, shape = RoundedCornerShape(16.dp) , modifier = Modifier.padding(3.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (index==active.value) Color.Black else Color.White)
    ) {
        Text(text=category , color = if (index == active.value) Color.White else Color.Black , fontWeight = FontWeight.Bold)
    }
}