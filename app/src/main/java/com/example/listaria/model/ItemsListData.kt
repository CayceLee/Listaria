package com.example.listaria.model

import java.io.Serializable

data class ItemsListData(
    var id: Int = -1,
    var item: String? = "",
    var struckThrough: Int = 0,
    var isChecked: Int = 0
) : Serializable

