package com.example.listaria.view.addnewlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.listaria.R
import com.example.listaria.databinding.AddNewListBinding
import com.example.listaria.model.AddNewListViewModel
import com.example.listaria.model.ItemsListData
import com.example.listaria.utils.DBHelper
import com.example.listaria.utils.ItemAdapter
import com.example.listaria.view.main.MainActivity
import com.google.gson.Gson


class AddNewListActivity : AppCompatActivity() {

    private val viewModel: AddNewListViewModel by viewModels()
    lateinit var itemAdapter: ItemAdapter
    private lateinit var checkedItemsList: MutableList<ItemsListData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_list)

        val binding: AddNewListBinding = DataBindingUtil.setContentView(this, R.layout.add_new_list)
        checkedItemsList = mutableListOf()

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
                }
            }
            itemAdapter.updateList(viewModel.list)
        }

        binding.editTextField.text
        itemAdapter = ItemAdapter(
            mutableListOf(),
            onItemClick,
            onCheckBoxClick,
        )
        binding.gpsButtonHintText.visibility = View.VISIBLE
        binding.gpsToggleButton.setOnClickListener { gpsToggle ->
            val gpsToggleButton = gpsToggle as SwitchCompat
            if (gpsToggleButton.isChecked) {
                binding.categorySpinner.visibility = View.VISIBLE
                binding.gpsButtonHintText.visibility = View.INVISIBLE
            } else {
                binding.categorySpinner.visibility = View.INVISIBLE
                binding.gpsButtonHintText.visibility = View.VISIBLE
            }
        }
//        binding.userEnteredItem.setOnKeyListener {
//            if(keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                val item = binding.userEnteredItem.text
//                if (binding.userEnteredItem.text.isNotEmpty()) {
//                    viewModel.list.add(
//                        ItemsListData(
//                            id = viewModel.list.size + 1,
//                            item = item.toString(),
//                            struckThrough = 0,
//                            isChecked = 0
//                        )
//                    )
//                    itemAdapter.updateList(viewModel.list)
//                    itemAdapter.notifyDataSetChanged()
//                    binding.userEnteredItem.text.clear()
//                }
//            }
//
//        }
        binding.addItemToListButton.setOnClickListener {
            val item = binding.userEnteredItem.text
            if (binding.userEnteredItem.text.isNotEmpty()) {
                viewModel.list.add(ItemsListData(id = viewModel.list.size + 1, item = item.toString(), struckThrough = 0, isChecked = 0))
                itemAdapter.updateList(viewModel.list)
                itemAdapter.notifyDataSetChanged()
                binding.userEnteredItem.text.clear()
            }
            System.out.println(viewModel.list)
        }

        binding.saveListButton.setOnClickListener {
            val db = DBHelper(this, null)
            var gpsBoolVal = 0
            if (viewModel.list.isNotEmpty()) {
                val json = Gson().toJson(viewModel.list)
                Log.d("ItemList", json)

                if (binding.editTextField.text.toString().isEmpty()) {
                    Toast.makeText(this, "A list title is required", Toast.LENGTH_SHORT).show();
                } else if (binding.gpsToggleButton.isChecked && binding.categorySpinner.selectedItem.equals("(Select a list category)" )){
                    Toast.makeText(this, "A category is required if GPS is enabled", Toast.LENGTH_SHORT).show()
                } else if(!binding.editTextField.text.toString().isEmpty() && binding.gpsToggleButton.isChecked) {
                    gpsBoolVal = 1
                    db.addList(binding.editTextField.text.toString(), gpsBoolVal, binding.categorySpinner.selectedItem.toString(), json)
                    val switchActivityIntent = Intent(this, MainActivity::class.java)
                    startActivity(switchActivityIntent)
                }
                else if(!binding.editTextField.text.toString().isEmpty() && !binding.gpsToggleButton.isChecked) {
                    db.addList(binding.editTextField.text.toString(), gpsBoolVal, binding.categorySpinner.selectedItem.toString(), json)
                    val switchActivityIntent = Intent(this, MainActivity::class.java)
                    startActivity(switchActivityIntent)
                }
            } else {
                val emptyList = mutableListOf<String>()
                val json = Gson().toJson(emptyList)
                if (binding.editTextField.text.toString().isEmpty()) {
                    Toast.makeText(this, "A list title is required", Toast.LENGTH_SHORT).show();
                } else if (binding.gpsToggleButton.isChecked && binding.categorySpinner.selectedItem.equals("(Select a list category)" )){
                    Toast.makeText(this, "A category is required if GPS is enabled", Toast.LENGTH_SHORT).show()
                } else if(!binding.editTextField.text.toString().isEmpty() && binding.gpsToggleButton.isChecked) {
                    gpsBoolVal = 1
                    db.addList(binding.editTextField.text.toString(), gpsBoolVal, binding.categorySpinner.selectedItem.toString(), json)
                    val switchActivityIntent = Intent(this, MainActivity::class.java)
                    startActivity(switchActivityIntent)
                }
                else if(!binding.editTextField.text.toString().isEmpty() && !binding.gpsToggleButton.isChecked) {
                    db.addList(binding.editTextField.text.toString(), gpsBoolVal, binding.categorySpinner.selectedItem.toString(), json)
                    val switchActivityIntent = Intent(this, MainActivity::class.java)
                    startActivity(switchActivityIntent)
                }
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

        val recyclerView = findViewById<RecyclerView>(R.id.new_list_items)
        recyclerView.adapter = itemAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun checkBoxClick(binding : AddNewListBinding, itemData: ItemsListData, checkedItemsList : MutableList<ItemsListData>) {
        if (itemData.isChecked == 1) {
            checkedItemsList.add(itemData)
            System.out.println("CheckedItemList in ANLA OnCheckBoxClick: $checkedItemsList")
            binding.trashButton.visibility = View.VISIBLE
            binding.cancelButton.visibility = View.INVISIBLE
            binding.saveListButton.visibility = View.INVISIBLE
        } else {
            checkedItemsList.remove(itemData)

            if (checkedItemsList.isEmpty()) {
                binding.trashButton.visibility = View.INVISIBLE
                binding.cancelButton.visibility = View.VISIBLE
                binding.saveListButton.visibility = View.VISIBLE
            }
        }
    }
}