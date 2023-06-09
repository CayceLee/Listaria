package com.example.listaria.model

@kotlinx.serialization.Serializable
data class ListOfListsData(
    var id: Int = -1,
    var title: String = "No Title",
    val gpsOn: Int = 0,
    val storeCategory: String = "",
    val listContents: String = "Empty List",
)