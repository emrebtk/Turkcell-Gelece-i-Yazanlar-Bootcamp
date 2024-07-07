package com.example.emre_bitik_final.adaptors

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.emre_bitik_final.R
import com.example.emre_bitik_final.configs.ApiClient
import com.example.emre_bitik_final.configs.AppDatabase
import com.example.emre_bitik_final.models.LikeProducts
import com.example.emre_bitik_final.services.IDummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LikeAdaptors(private val context: Context, private val arr: List<LikeProducts>)
    : ArrayAdapter<LikeProducts>(context, R.layout.like_row, arr)
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Görünümü inflate etme veya varsa convertView'i kullanma
        val rootView = convertView ?: LayoutInflater.from(context).inflate(R.layout.like_row, parent, false)

        val db = Room.databaseBuilder(context, AppDatabase::class.java, "pro1")
            .allowMainThreadQueries() 
            .build()
        val dao = db.productDao()

        // Liste öğesini al
        val dt = arr[position]


        val l_brand: TextView = rootView.findViewById(R.id.l_brade)
        val l_description: TextView = rootView.findViewById(R.id.l_detail)
        val l_price: TextView = rootView.findViewById(R.id.l_price)
        val l_image: ImageView = rootView.findViewById(R.id.l_image)
        val l_delete: ImageButton = rootView.findViewById(R.id.l_delete)
        val l_order: ImageButton = rootView.findViewById(R.id.l_order)

        // Set işlemi
        l_brand.text = dt.brand
        l_description.text = dt.description
        val price = dt.price.toString() + " $" // Fiyatı biçimlendirme
        l_price.text = price
        Glide.with(context)
            .load(dt.images)
            .into(l_image)

        // Silme butonu işlevi
        l_delete.setOnClickListener {
            dao.deleteById(dt.id) // Room DAO'su üzerinden seçilen öğeyi silme
        }

        // Sipariş verme işlemi için servis çağrısı
        val dummyService = ApiClient.retrofit.create(IDummyService::class.java)
        l_order.setOnClickListener {
            dummyService.postAddCart(dt.id.toString()).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        // Başarılı yanıt durumu
                        Log.d("TAG", "Order placed successfully")
                    } else {
                        // Hata durumu
                        Log.e("TAG", "Failed to place order: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    // İstek başarısız olduğunda
                    Log.e("TAG", "Failed to place order: ${t.message}")
                }
            })
        }

        return rootView // Oluşturulan veya yeniden kullanılan görünümü döndürme
    }
}
