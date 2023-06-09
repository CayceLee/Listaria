package com.example.listaria.view.openedlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.SwitchCompat
import com.example.listaria.model.OpenedCurrentListViewModel
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listaria.R
import com.example.listaria.databinding.ActivityCurrentOpenedListBinding
import com.example.listaria.model.*
import com.example.listaria.utils.DBHelper
import com.example.listaria.utils.ItemAdapter
import com.example.listaria.utils.ListAdapter
import com.example.listaria.view.main.MainActivity
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.json.JSONArray
import org.json.JSONTokener

class CurrentOpenedListActivity :AppCompatActivity() {
    private lateinit var viewModel: OpenedCurrentListViewModel
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var listData: ListOfListsData
    private lateinit var itemsList: MutableList<ItemsListData>
    private lateinit var checkedItemsList: MutableList<ItemsListData>
    lateinit var recyclerView: RecyclerView

    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_opened_list)

        val binding: ActivityCurrentOpenedListBinding = DataBindingUtil.setContentView(this, R.layout.activity_current_opened_list)
        viewModel = ViewModelProvider.AndroidViewModelFactory(application).create(OpenedCurrentListViewModel::class.java)
        itemsList = mutableListOf()
        checkedItemsList = mutableListOf()
        recyclerView = findViewById(R.id.opened_list_items)

        val jsonString = intent.getStringExtra("list")
        listData = Json.decodeFromString<ListOfListsData>(serializer(), jsonString!!)
        itemsList = getAllItems(listData)

        val onItemClick: (ItemsListData) -> Unit = {
            viewModel.list.sortBy {list ->
                list.struckThrough
            }
            itemAdapter.updateList(viewModel.list)
        }

        val onCheckBoxClick: (ItemsListData) -> Unit = {
            if (it.isChecked == 1) {
                checkedItemsList.add(it)
                binding.trashButton.visibility = View.VISIBLE
                binding.cancelButton.visibility = View.INVISIBLE
                binding.saveListButton.visibility = View.INVISIBLE

            } else if (it.isChecked == 0) {
                checkedItemsList.remove(it)
                if (checkedItemsList.isEmpty()) {
                    binding.trashButton.visibility = View.INVISIBLE
                    binding.cancelButton.visibility = View.VISIBLE
                    binding.saveListButton.visibility = View.VISIBLE
                } else {
                    binding.trashButton.visibility = View.VISIBLE
                    binding.cancelButton.visibility = View.INVISIBLE
                    binding.saveListButton.visibility = View.INVISIBLE
                }
            }
        }

        binding.trashButton.setOnClickListener {
            binding.trashButton.visibility = View.INVISIBLE
            binding.cancelButton.visibility = View.VISIBLE
            binding.saveListButton.visibility = View.VISIBLE
            viewModel.list.removeAll {
                it.isChecked == 1
            }
            for (j in 0 until viewModel.list.size -1) {
                if (viewModel.list[j].isChecked == 1) {
                    System.out.println("List Size: ${viewModel.list.size}")
                    System.out.println("List item: ${viewModel.list[j]}")
                    itemsList.remove(viewModel.list[j])
                }
            }
            itemAdapter.updateList(viewModel.list)
        }

        itemAdapter = ItemAdapter(
            itemsList,
            onItemClick,
            onCheckBoxClick,
        )

        binding.editTextField.setText(listData.title)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = itemAdapter

        if (listData.gpsOn == 1) {
            val category = listData.storeCategory
            binding.gpsToggleButton.isChecked = true
            binding.category.visibility = View.VISIBLE
            binding.category.text = category
        } else if (listData.gpsOn == 0) {
            binding.gpsToggleButton.visibility = View.INVISIBLE
        }

        binding.editTextField.text
        binding.gpsToggleButton.setOnClickListener { gpsToggle ->
            val gpsToggleButton = gpsToggle as SwitchCompat
            if (gpsToggleButton.isChecked) {
                binding.category.visibility = View.VISIBLE
                binding.category.text = listData.storeCategory
            } else if (!gpsToggleButton.isChecked && !binding.category.text.equals("(Select a list category)")){
                binding.category.visibility = View.VISIBLE
                binding.category.text = listData.storeCategory
            } else {
                binding.category.visibility = View.INVISIBLE
            }
        }

        binding.addItemToListButton.setOnClickListener {
            val item = binding.userEnteredItem.text
            if (binding.userEnteredItem.text.isNotEmpty()) {
                viewModel.list.add(
                    ItemsListData(
                        id = viewModel.list.size + 1,
                        item = item.toString(),
                        struckThrough = 0
                    )
                )
                binding.userEnteredItem.text.clear()
            }
            viewModel.list.sortBy {list ->
                list.struckThrough
            }
            itemAdapter.updateList(viewModel.list)
            itemAdapter.notifyDataSetChanged()
        }

//        binding.userEnteredItem.setOnKeyListener { v, keyCode, event ->
//            when {
//                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {
//                    //perform an action here e.g. a send message button click
//                    binding.addItemToListButton.performClick()
//
//                return@setOnKeyListener true
//            }
//            else -> false
//        }

        binding.saveListButton.setOnClickListener {
            val db = DBHelper(this, null)
            var gpsBoolVal = 0

            val gson = Gson()
            val json = gson.toJson(viewModel.list)
//            val json = gson.toJson(toHtml(viewModel.list))

            Log.d("ItemList", json)

            if (binding.editTextField.text.toString().isEmpty()) {
                Toast.makeText(this, "A list title is required", Toast.LENGTH_SHORT).show();

            } else if(!binding.editTextField.text.toString().isEmpty() && binding.gpsToggleButton.isChecked) {
                gpsBoolVal = 1
                db.updateList(listData.id, binding.editTextField.text.toString(), gpsBoolVal,
                    listData.storeCategory, json)
                val switchActivityIntent = Intent(this, MainActivity::class.java)
                startActivity(switchActivityIntent)
            } else if(!binding.editTextField.text.toString().isEmpty() && !binding.gpsToggleButton.isChecked) {
                db.updateList(listData.id, binding.editTextField.text.toString(), gpsBoolVal,
                    listData.storeCategory, json)
                val switchActivityIntent = Intent(this, MainActivity::class.java)
                startActivity(switchActivityIntent)
            }
        }

        binding.cancelButton.setOnClickListener {
            val switchActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(switchActivityIntent)
        }

        val storeCategories = resources.getStringArray(R.array.StoreCategories)

        val categorySpinner = findViewById<Spinner>(R.id.category_spinner)
        if (categorySpinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, storeCategories)
            categorySpinner.adapter = adapter
        }

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun getAllItems(list: ListOfListsData): MutableList<ItemsListData> {
        val data = JSONTokener(list.listContents).nextValue() as JSONArray
        System.out.println("Data from GetAllItems function: $data")
        var itemData: MutableList<ItemsListData> = mutableListOf()

        for (i in 0 until data.length()) {
            var item: ItemsListData
            val id = data.getJSONObject(i).getInt("id")
            var itemText = data.getJSONObject(i).getString("item")
            val struckThrough = data.getJSONObject(i).getInt("struckThrough")
            val isChecked = data.getJSONObject(i).getInt("isChecked")
            item = ItemsListData(id, itemText, struckThrough, isChecked)
            itemData.add(item)
            viewModel.list.add(item)
        }
        return itemData
    }
}