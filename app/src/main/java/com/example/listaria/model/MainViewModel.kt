package com.example.listaria.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.listaria.repository.ListsRepository
import kotlinx.coroutines.launch

/**
 * the ViewModel should be used as the "brain" of the view. it should have a
 * place to get data from (a repository), do work on the data, and expose the data
 * to the view (activity/fragment) in a safe way (detailed below). it should
 * also know what to do with any user interaction such as user input and click
 * events (this hasnt been set up here yet)
 */
class MainViewModel(
    application: Application
): AndroidViewModel(application) {
    // get a reference to where the data is coming from
    private val repository = ListsRepository(application)
    private val _listSet: MutableList<ListOfListsData> // this is the mutable list the ViewModel should be making any changes to
        get() = getLists() // it is initialized by customizing the getter for this val (a Kotlin thing thats really neat, check it out here: https://kotlinlang.org/docs/properties.html#getters-and-setters
    val listSet: List<ListOfListsData> // this is the immutable list anything outside of ViewModel can use to reference
        get() = _listSet.toList() // the .toList() function is built into the Kotlin library. Ideally you would want to work with MutableList/List instead of ArrayLists, Kotlin has a lot of neat filtering, sorting, and other operator methods specifically for MutableLists/Lists
    val updateFlow = repository.dbUpdateFlow

    init {
        viewModelScope.launch {
            updateFlow.collect{
                _listSet.clear()
                _listSet.addAll(it)
            }
        }
    }

    fun deleteList(id: Int)  {
        repository.deleteFromDatabase(_listSet[id])
        repository.getListsFromDatabase()
    }

    private fun getLists() = repository.getListsFromDatabase()
}