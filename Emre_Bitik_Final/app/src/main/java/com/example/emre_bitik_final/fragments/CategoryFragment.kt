package com.example.emre_bitik_final.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.emre_bitik_final.R
import com.example.emre_bitik_final.adaptors.ProductAdaptors
import com.example.emre_bitik_final.configs.ApiClient
import com.example.emre_bitik_final.models.Product
import com.example.emre_bitik_final.models.Products
import com.example.emre_bitik_final.services.IDummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment : Fragment() {

    private lateinit var dummyService: IDummyService
    private lateinit var ctg_listview: ListView
    private lateinit var ctg_txt: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_category, container, false)

        ctg_listview = rootView.findViewById(R.id.ctg_listview)
        ctg_txt = rootView.findViewById(R.id.ctg_txt)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dummyService = ApiClient.getClient().create(IDummyService::class.java)
        val selectedName = arguments?.getString("selected_name")
        val query =selectedName.toString()
        ctg_txt.setText(query)
        fetchData(query)
    }

    private fun fetchData(query: String) {

        dummyService.getCategoriesDetail(query).enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    val productList = response.body()?.products ?: emptyList()
                    val productAdapter = ProductAdaptors(requireContext(), productList)
                    ctg_listview.adapter = productAdapter
                    //listview item tıklama işlemi
                    ctg_listview.setOnItemClickListener { _, _, position, _ ->
                        val selectedProduct = productList[position]

                        val detailFragment = DetailFragment()

                        val bundle = Bundle()
                        bundle.putSerializable("selectedProduct", selectedProduct)
                        detailFragment.arguments = bundle

                       //fragment geçişi
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, detailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                } else {
                    Toast.makeText(context, "Response unsuccessful: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                Toast.makeText(context, "Error fetching categories: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
