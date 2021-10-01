package com.example.demo9

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_student.view.*


class StudentAdapter : RecyclerView.Adapter<StudentAdapter.ItemHolder> {

    companion object {
        var clickListener: ClickListener? = null
    }

    var arrayList: ArrayList<Student>

    constructor(list: ArrayList<Student>) : super() {
        this.arrayList = list
    }

    class ItemHolder : RecyclerView.ViewHolder, View.OnClickListener {

        var text_id: TextView
        var text_name: TextView
        var text_age: TextView

        constructor(itemView: View) : super(itemView) {
            text_id = itemView.text_id
            text_name = itemView.text_name
            text_age = itemView.text_age

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            clickListener!!.onItemClick(adapterPosition, v!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        var v: View = LayoutInflater.from(parent.context).inflate(R.layout.row_student, parent, false)
        var itemHolder = ItemHolder(v)

        return itemHolder
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var item = arrayList[holder.adapterPosition]
        holder.text_id.text = item.id.toString()
        holder.text_name.text = item.name
        holder.text_age.text = item.age.toString()

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View)
    }

    fun setOnItemClickListener(clickListener: ClickListener?) {
        StudentAdapter.clickListener = clickListener!!
    }

}
