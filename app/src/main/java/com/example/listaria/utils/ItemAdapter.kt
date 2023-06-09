package com.example.listaria.utils

import android.text.*
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.listaria.R
import com.example.listaria.model.ItemsListData


class ItemAdapter(
    private val listSet : MutableList<ItemsListData>,
    private val onItemClick : (ItemsListData) -> Unit,
    private val onCheckBoxClick : (ItemsListData) -> Unit)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(layoutInflater.inflate(R.layout.individual_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, id: Int) {
        val itemList = listSet[id]
//        val itemList = listSet[itemPosition]

        holder.bind(itemList)
        holder.clickHandlers(
            itemData = itemList,
            onItemClick = onItemClick,
            onCheckBoxClick = onCheckBoxClick
        )
    }

    override fun getItemCount(): Int {
        return listSet.size
    }

    fun updateList(list : MutableList<ItemsListData>) {
        val diffCallBack = ItemsCallBack(listSet, list)
        val diffItems = DiffUtil.calculateDiff(diffCallBack)
        diffItems.dispatchUpdatesTo(this)
        listSet.clear()
        listSet.addAll(list)

    }

    class ItemViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bind(itemData: ItemsListData) {
            val itemInList = view.findViewById<TextView>(R.id.itemInList)
            if (itemData.struckThrough == 1) {
                val ss = SpannableStringBuilder()
                ss.append(itemData.item).setSpan(
                    StrikethroughSpan(),
                    0,
                    itemData.item!!.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                itemInList.setText(ss, TextView.BufferType.NORMAL)
            } else {
                itemInList.text = itemData.item
            }
        }

        fun clickHandlers(
            itemData: ItemsListData,
            onItemClick: (ItemsListData) -> Unit,
            onCheckBoxClick: (ItemsListData) -> Unit
        ){
            bindStrikeSelectedItemClickHandler(itemData, onItemClick)
            bindSelectedItemCheckBoxClickHandler(itemData, onCheckBoxClick)
        }

        private fun bindStrikeSelectedItemClickHandler(
            itemData: ItemsListData,
            onItemClick: (ItemsListData) -> Unit
        ) {
            val itemClick = view.findViewById<TextView>(R.id.itemInList)
            view.setOnClickListener {
                val ss = SpannableString(itemData.item)
                val strikethroughSpan = StrikethroughSpan()
                if (itemData.struckThrough == 0) {
                    ss.setSpan(
                        strikethroughSpan,
                        0,
                        ss.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    itemClick.setText(ss, TextView.BufferType.NORMAL)
                    itemData.item = ss.toString()
                    itemData.struckThrough = 1
                } else {
                    ss.removeSpan(strikethroughSpan)
                    itemClick.setText(ss, TextView.BufferType.NORMAL)
                    itemData.item = ss.toString()
                    itemData.struckThrough = 0
                }
                onItemClick(itemData)
            }
        }

        private fun bindSelectedItemCheckBoxClickHandler(
            itemData: ItemsListData,
            onCheckBoxClick: (ItemsListData) -> Unit
        ) {
            val checkBox = view.findViewById<AppCompatCheckBox>(R.id.itemCheckBox)
            checkBox.setOnClickListener {
                if (checkBox.isPressed && itemData.isChecked == 0) {
                    itemData.isChecked = 1
                } else if (checkBox.isPressed && itemData.isChecked == 1){
                    itemData.isChecked = 0
                }
                itemData.id = layoutPosition
                onCheckBoxClick(itemData)
            }
        }
    }
}