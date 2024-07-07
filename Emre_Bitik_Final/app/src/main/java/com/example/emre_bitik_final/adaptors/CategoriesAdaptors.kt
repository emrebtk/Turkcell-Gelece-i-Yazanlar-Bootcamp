package com.example.emre_bitik_final.adaptors

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.emre_bitik_final.models.Category
import kotlin.random.Random

class CategoriesAdaptors(context: Context, categories: List<Category>) : ArrayAdapter<Category>(context, 0, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val holder: ViewHolder

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
            holder = ViewHolder(itemView)
            itemView.tag = holder
        } else {
            holder = itemView.tag as ViewHolder
        }

        val category = getItem(position)
        holder.textViewName.text = category?.name
        holder.textViewName.setTextSize(25f)
        // Rastgele bir arka plan rengi ayarla
        val randomColor = getRandomColor()
        holder.textViewName.setBackgroundColor(randomColor)

        return itemView!!
    }

    private fun getRandomColor(): Int {
        val random = Random
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }

    private class ViewHolder(view: View) {
        val textViewName: TextView = view.findViewById(android.R.id.text1)
    }
}
