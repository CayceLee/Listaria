package com.example.listaria.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.listaria.model.ListOfListsData

class ListsCallBack(private val oldList: MutableList<ListOfListsData>, private val newList: MutableList<ListOfListsData>): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int  = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (_, title, gps, category, list) = oldList[oldItemPosition]
        val (_, title1, gps1, category1, list1) = newList[newItemPosition]
        return title == title1 && gps == gps1 && category == category1 && list == list1
    }
}