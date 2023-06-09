package com.example.listaria.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.listaria.model.ItemsListData


class ItemsCallBack(private val oldItemList: MutableList<ItemsListData>, private val newItemList: MutableList<ItemsListData>): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemList.size

    override fun getNewListSize(): Int  = newItemList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemList[oldItemPosition].id == newItemList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (_, item, struckThrough, isChecked) = oldItemList[oldItemPosition]
        val (_, item1, struckThrough1, isChecked1) = newItemList[newItemPosition]
        return item == item1 && struckThrough == struckThrough1 && isChecked == isChecked1
    }

}