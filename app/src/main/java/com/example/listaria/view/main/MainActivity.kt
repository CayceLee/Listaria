package com.example.listaria.view.main

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listaria.R
import com.example.listaria.model.ListOfListsData
import com.example.listaria.model.MainViewModel
import com.example.listaria.utils.DBHelper
import com.example.listaria.utils.ListAdapter
import com.example.listaria.view.addnewlist.AddNewListActivity
import com.example.listaria.view.openedlist.CurrentOpenedListActivity
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var rv: RecyclerView
    private lateinit var listAdapter: ListAdapter
    private lateinit var db: DBHelper



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Places.initialize(this, BuildConfig.MAPS_API_KEY)
        viewModel = ViewModelProvider.AndroidViewModelFactory(application).create(MainViewModel::class.java)
        val listOfLists = viewModel.listSet.toMutableList()

        val onListClick: (Int) -> Unit = { id ->
            openList(listOfLists[id])
        }

        val onDeleteClick: (Int) -> Unit = { id ->
            System.out.println("OnDelete ID: $id")
            viewModel.deleteList(id)
        }

        rv = findViewById(R.id.listRecyclerView)
        listAdapter = ListAdapter(
            listOfLists,
            onListClick,
            onDeleteClick,
        )
        rv.adapter = listAdapter
        rv.layoutManager = LinearLayoutManager(this)

        //Collect all of the data from the DB
        lifecycleScope.launch {
            viewModel.updateFlow.collect{
                listAdapter.updateList(it)
            }
        }

        //get all storeCategories and call StoreSearcher
        val searchCategory = mutableListOf<String>()
        val listSetWithGpsOn = mutableListOf<ListOfListsData>()
        for (i in 0 until viewModel.listSet.size) {
            if (viewModel.listSet[i].storeCategory != "" && viewModel.listSet[i].gpsOn == 1){
                searchCategory.add(viewModel.listSet[i].storeCategory)
                listSetWithGpsOn.add(viewModel.listSet[i])
            }
        }


        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                ACCESS_BACKGROUND_LOCATION
            ) !==
            PackageManager.PERMISSION_GRANTED) {
//            if (ContextCompat.checkSelfPermission(
//                this,
//                    ACCESS_FINE_LOCATION
//                ) !== PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                        ACCESS_BACKGROUND_LOCATION
                    )) {
                    ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(ACCESS_FINE_LOCATION, ACCESS_BACKGROUND_LOCATION), 1)
                } else {
                    ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(ACCESS_FINE_LOCATION, ACCESS_BACKGROUND_LOCATION), 1)
                }
//            }
        }



        //Setup menu and popup
        val menuButton = findViewById<ImageButton>(R.id.menuBtn)
        val popUpMenu = PopupMenu(this,menuButton)
        popUpMenu.menuInflater.inflate(R.menu.item_menu, popUpMenu.menu)

        //Initialize database if first time app is opened
        if (!this::db.isInitialized) {
            db = DBHelper(this, null)
        }
    }

    private fun openList(list: ListOfListsData){
        val jsonString = Json.encodeToString(list)
        val intent = Intent(this, CurrentOpenedListActivity::class.java).apply {
            putExtra("list", jsonString)
        }
        startActivity(intent)
    }

    fun addNewList(view: View) {
        val switchActivityIntent = Intent(this, AddNewListActivity::class.java)
        startActivity(switchActivityIntent)
    }
}