package com.example.listaria.model

import androidx.lifecycle.ViewModel

class AddNewListViewModel: ViewModel() {

    val list = mutableListOf<ItemsListData>()
}