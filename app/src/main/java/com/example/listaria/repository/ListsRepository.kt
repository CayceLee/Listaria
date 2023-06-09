package com.example.listaria.repository

import android.app.Application
import com.example.listaria.model.ListOfListsData
import com.example.listaria.utils.DBHelper

class ListsRepository(application: Application) {
    private val databaseHelper: DBHelper = DBHelper(application, null)
    val dbUpdateFlow = databaseHelper.dbUpdateFlow
    fun getListsFromDatabase() = databaseHelper.readData()

    fun deleteFromDatabase(listOfLists: ListOfListsData) = databaseHelper.onDelete(listOfLists.id)

    fun addListToDatabase(title: String, gps: Int, category: String, listSet: String) = databaseHelper.addList(title, gps, category, listSet)

    fun updateListInDatabase(id: Int, title: String, gps: Int, category: String, listSet: String) = databaseHelper.updateList(id, title, gps, category, listSet)

}