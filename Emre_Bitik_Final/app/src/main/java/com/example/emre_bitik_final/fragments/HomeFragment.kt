package com.example.emre_bitik_final.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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

class HomeFragment : Fragment() {

    private lateinit var dummyService: IDummyService
    private lateinit var listViewProducts: ListView
    private lateinit var productAdapter: ProductAdaptors
    private lateinit var arr: List<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        listViewProducts = rootView.findViewById(R.id.s_listview)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dummyService = ApiClient.getClient().create(IDummyService::class.java)
        fetchData()

    }
//Ürünleri getir
    private fun fetchData() {
        dummyService.getProducts().enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    arr = response.body()?.products ?: emptyList()
                    productAdapter = ProductAdaptors(requireContext(), arr)
                    listViewProducts.adapter = productAdapter

                    listViewProducts.onItemClickListener =
                        AdapterView.OnItemClickListener { _, _, position, _ ->
                            val selectedProduct = arr[position]

                            val detailFragment = DetailFragment()

                            val bundle = Bundle()
                            bundle.putSerializable("selectedProduct",selectedProduct)
                            detailFragment.arguments = bundle

                            // FragmentTransaction ile DetailFragment'ı başlatma
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, detailFragment)
                                .addToBackStack(null)
                                .commit()
                        }

                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                Toast.makeText(context, "GetProducts error: ${t.message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}