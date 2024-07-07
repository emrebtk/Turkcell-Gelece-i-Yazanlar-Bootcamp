
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


class ProductAdaptors(private val context: Context, private val arr: List<Product>)
    : ArrayAdapter<Product>(context, R.layout.product_row, arr)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView = convertView ?: LayoutInflater.from(context).inflate(R.layout.product_row, parent, false)

        val dt = arr[position]
        val r_brand: TextView = rootView.findViewById(R.id.r_brand)
        val r_description: TextView = rootView.findViewById(R.id.r_description)
        val r_price: TextView = rootView.findViewById(R.id.r_price)
        val r_image: ImageView = rootView.findViewById(R.id.r_image)
        //Set işlemi
        r_brand.text = dt.brand
        r_description.text = dt.description
        val price = dt.price.toString()+""+"$"
        r_price.text = price

        Glide.with(context)
            .load(dt.images[0])
            .into(r_image)
        return rootView
    }
}