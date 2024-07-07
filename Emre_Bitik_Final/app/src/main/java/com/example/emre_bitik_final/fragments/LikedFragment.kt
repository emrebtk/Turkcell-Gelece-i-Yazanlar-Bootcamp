package com.example.emre_bitik_final.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.emre_bitik_final.R
import com.example.emre_bitik_final.adaptors.LikeAdaptors
import com.example.emre_bitik_final.configs.AppDatabase

class LikedFragment : Fragment() {
    private lateinit var l_listView: ListView
    private lateinit var likeAdaptor: LikeAdaptors

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_liked, container, false)

        // ListView bileşenini XML'den alıp ilgili değişkene atama
        l_listView = rootView.findViewById(R.id.l_listview)

        // Room veritabanını başlatma ve DAO'yu oluşturma
        val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "pro1")
            .allowMainThreadQueries()
            .build()
        val dao = db.productDao()

        // Veritabanından beğenilen ürünleri alma ve adaptörü oluşturma
        val likedProducts = dao.getAll()
        likeAdaptor = LikeAdaptors(requireContext(), likedProducts)
        l_listView.adapter = likeAdaptor

        return rootView
    }
}
