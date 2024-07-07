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

class SearchFragment : Fragment() {

    private lateinit var dummyService: IDummyService
    private lateinit var s_listview: ListView
    private lateinit var s_search: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        s_listview = rootView.findViewById(R.id.s_listview)
        s_search = rootView.findViewById(R.id.s_search)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dummyService = ApiClient.getClient().create(IDummyService::class.java)
        s_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Değişiklik öncesi işlemler
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Metin değiştiğinde yapılacak işlemler
            }

            override fun afterTextChanged(s: Editable?) {
                // Metin değişikliği tamamlandığında yapılacak işlemler
                val query = s.toString().trim()


                fetchData(query)
            }
        })
    }

    private fun fetchData(query: String) {
        dummyService.searchProducts(query).enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    val productList = response.body()?.products ?: emptyList()
                    val productAdapter = ProductAdaptors(requireContext(), productList)
                    s_listview.adapter = productAdapter

                    s_listview.setOnItemClickListener { _, _, position, _ ->
                        val selectedProduct = productList[position]

                        val detailFragment = DetailFragment()

                        val bundle = Bundle()
                        bundle.putSerializable("selectedProduct", selectedProduct)
                        detailFragment.arguments = bundle

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, detailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                } else {
                    Toast.makeText(requireContext(), "Response unsuccessful: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                Toast.makeText(requireContext(), "Error fetching data: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
