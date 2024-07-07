package com.example.emre_bitik_final.adaptors


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.emre_bitik_final.R

import com.example.emre_bitik_final.models.Product


class CartsAdaptors(private val context: Context, private val arr: List<Product>)
    : ArrayAdapter<Product>(context, R.layout.cart_row, arr)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView = convertView ?: LayoutInflater.from(context).inflate(R.layout.cart_row, parent, false)

        val dt = arr[position]
        val cart_discountedTotal: TextView = rootView.findViewById(R.id.cart_discountedTotal)
        val cart_discountPercentage: TextView = rootView.findViewById(R.id.cart_discountPercentage)
        val cart_price: TextView = rootView.findViewById(R.id.cart_price)
        val cart_quantity: TextView = rootView.findViewById(R.id.cart_quantity)
        val cart_title: TextView = rootView.findViewById(R.id.cart_title)
        val cart_total: TextView = rootView.findViewById(R.id.cart_total)
        val cart_image: ImageView = rootView.findViewById(R.id.cart_image)

        val discountedTotal = "Discounted Total : " + ""+dt.discountedTotal.toString()
        cart_discountedTotal.text = discountedTotal
        val quantity = "Quantity : "+""+dt.quantity.toString()+"$"
        cart_quantity.text =quantity
        val total = "Total : "+""+dt.total.toString()+"$"
        cart_total.text =total
        cart_title.text = dt.title
        val discountPercentage = "Discount Percentage : "+"" +dt.discountPercentage.toString()
        cart_discountPercentage.text =discountPercentage
        val price = dt.price.toString()+""+"$"
        cart_price.text = price

        Glide.with(context)
            .load(dt.thumbnail)
            .into(cart_image)
        return rootView
    }
}