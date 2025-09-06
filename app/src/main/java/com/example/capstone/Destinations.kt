package com.example.capstone

interface Destinations {
    val route : String
}

object Home : Destinations{
    override val route = "Home"
}

object OnBoarding : Destinations{
    override val route = "onBoarding"
}

object Profile : Destinations{
    override val route = "profile"
}