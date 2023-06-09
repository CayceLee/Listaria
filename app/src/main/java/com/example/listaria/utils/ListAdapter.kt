package com.example.listaria.utils

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.listaria.R
import com.example.listaria.model.ListOfListsData

class ListAdapter(
    private val listSet: MutableList<ListOfListsData>,
    private val onListClick: (Int) -> Unit,
    val onDeleteClick: (Int) -> Unit,
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListViewHolder(layoutInflater.inflate(R.layout.item_list_layout, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ListViewHolder, id: Int) {
        val listsData = listSet[id]
        viewHolder.bind(listsData.title, listsData.gpsOn)
        viewHolder.bindClickHandlers(
            id = id,
            onListClick = onListClick,
            onDeleteClick = {
                onDeleteClick(id)
                notifyItemRemoved(id)
            },
        )
    }

    override fun getItemCount(): Int {
        return listSet.size
    }

    fun updateList(list: MutableList<ListOfListsData>) {
        val diffCallBack = ListsCallBack(listSet, list)
        val diffItems = DiffUtil.calculateDiff(diffCallBack)
        listSet.clear()
        listSet.addAll(list)
        diffItems.dispatchUpdatesTo(this)
    }

    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val menuButton = view.findViewById<ImageButton>(R.id.menuBtn)
        val popUpMenu: PopupMenu = PopupMenu(view.context,menuButton)

        init {
            setupPopUpMenu()
        }

        fun bind(listTitle : String, gps: Int) {
            //Bind the list title to the RecyclerView items
            val listTitleTextView = view.findViewById<TextView>(R.id.listTitle)
            listTitleTextView.text = listTitle
            //Get the boolean value of the GPS option
            // and display an icon on the mainactivity if true
            if (gps == 1) {
                val gpsIcon = view.findViewById<ImageView>(R.id.gpsIcon)
                gpsIcon.visibility = VISIBLE
            }
        }

        fun bindClickHandlers(
            id: Int,
            onListClick: (Int) -> Unit,
            onDeleteClick: (Int) -> Unit,
        ){
            bindSelectedListButtonClickHandler(id, onListClick)
            bindOnDeleteClickHandler(id, onDeleteClick)
        }

        private fun bindOnDeleteClickHandler(id: Int, onDeleteClick: (Int) -> Unit){
            popUpMenu.setOnMenuItemClickListener {
                onDeleteClick(id)
                true
            }
        }

        private fun bindSelectedListButtonClickHandler(id: Int, selectedButtonClick: (Int) -> Unit){
            val selectedListButton = view.findViewById<TextView>(R.id.listTitle)
            selectedListButton.setOnClickListener {
                selectedButtonClick(id)
            }
        }

        private fun setupPopUpMenu(){
            popUpMenu.menuInflater.inflate(R.menu.item_menu, popUpMenu.menu)
            menuButton.setOnClickListener {
                popUpMenu.show()
            }
        }
    }
}