package com.example.emre_bitik_final.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.emre_bitik_final.R
import com.example.emre_bitik_final.configs.ApiClient
import com.example.emre_bitik_final.configs.AppDatabase
import com.example.emre_bitik_final.models.LikeProducts
import com.example.emre_bitik_final.models.Product
import com.example.emre_bitik_final.services.IDummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment : Fragment() {
    private lateinit var d_image: ImageButton
    private lateinit var d_like: ImageButton
    private lateinit var d_brand: TextView
    private lateinit var d_detail: TextView
    private lateinit var d_price: TextView
    private lateinit var d_warranty: TextView
    private lateinit var d_addorder: Button

    private var currentIndex = 0 // Görsel indeksi için değişken

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment'in layout'unu inflate et
        val rootView = inflater.inflate(R.layout.fragment_detail, container, false)

        // Argument'ten seçilen ürünü al
        val bundle = arguments
        val product = bundle?.getSerializable("selectedProduct") as? Product

        // Room veritabanı oluştur ve DAO'yu al
        val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "pro1").allowMainThreadQueries().build()
        val dao = db.productDao()


        d_image = rootView.findViewById(R.id.d_image)
        d_like = rootView.findViewById(R.id.d_like)
        d_brand = rootView.findViewById(R.id.d_brand)
        d_detail = rootView.findViewById(R.id.d_detail)
        d_price = rootView.findViewById(R.id.d_price)
        d_warranty = rootView.findViewById(R.id.d_warranty)
        d_addorder = rootView.findViewById(R.id.d_addorder)

        // Ürün bilgilerini görünümlere set et
        d_brand.text = product?.brand
        d_detail.text = product?.description
        val price = product?.price.toString() + " $"
        d_price.text = price
        d_warranty.text = product?.warrantyInformation

        // İlk görseli yükle ve görsel tıklamalarını işle
        Glide.with(this)
            .load(product?.images?.getOrNull(0))
            .into(d_image)

        d_image.setOnClickListener {
            currentIndex++
            if (currentIndex >= product?.images?.size ?: 0) {
                currentIndex = 0
            }
            Glide.with(this)
                .load(product?.images?.getOrNull(currentIndex))
                .into(d_image)
        }

        // Ürün beğenme durumunu kontrol et ve işle
        val existingProduct = dao.getProductById(product!!.id)
        if (existingProduct != null) {
            d_like.setImageResource(R.drawable.liked_icon)
            product.isLiked = true
        }
        //Like buton işlevi
        d_like.setOnClickListener {
            product?.isLiked = !(product?.isLiked ?: false)

            if (product?.isLiked == true) {
                d_like.setImageResource(R.drawable.liked_icon)
                Toast.makeText(requireContext(), "The product was liked!", Toast.LENGTH_SHORT).show()

                if (existingProduct == null) {
                    // Ürün daha önce beğenilmemişse, veritabanına ekle
                    val likeProduct = LikeProducts(
                        product.id,
                        product.description,
                        product.price,
                        product.brand,
                        product.images[0],
                        product.warrantyInformation,
                        true
                    )
                    dao.insert(likeProduct)
                }

            } else {
                d_like.setImageResource(R.drawable.dont_like)

                // Ürün veritabanında beğenilmişse ve tekrar tıklama yapılıyorsa, veritabanından sil
                val existingProduct = dao.getProductById(product.id)
                if (existingProduct != null) {
                    val rowsDeleted = dao.deleteById(product!!.id)
                    if (rowsDeleted > 0) {
                        Toast.makeText(requireContext(), "The product was deleted from favorites", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "The product could not be deleted from the favorites", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Sepete ekleme işlemi
        val dummyService = ApiClient.retrofit.create(IDummyService::class.java)
        d_addorder.setOnClickListener {
            dummyService.postAddCart(product.id.toString()).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Order added successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Error adding order: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error adding order: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return rootView
    }
}
