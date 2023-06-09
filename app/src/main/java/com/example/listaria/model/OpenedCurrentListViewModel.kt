package com.example.listaria.model

import androidx.lifecycle.ViewModel

class OpenedCurrentListViewModel: ViewModel() {

    val list: MutableList<ItemsListData> = mutableListOf()
}