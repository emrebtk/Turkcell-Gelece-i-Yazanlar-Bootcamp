package com.example.emre_bitik_final.fragments
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.example.emre_bitik_final.R
import com.example.emre_bitik_final.adaptors.CartsAdaptors
import com.example.emre_bitik_final.configs.ApiClient
import com.example.emre_bitik_final.models.Product
import com.example.emre_bitik_final.models.Products
import com.example.emre_bitik_final.models.User
import com.example.emre_bitik_final.services.IDummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersFragment : Fragment() {

    private lateinit var dummyService: IDummyService
    private lateinit var cart_listview: ListView
    private lateinit var cartAdapter: CartsAdaptors
    private lateinit var arr: List<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_orders, container, false)

        cart_listview = rootView.findViewById(R.id.cart_listview)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dummyService = ApiClient.getClient().create(IDummyService::class.java)
        val user = arguments?.getSerializable("user") as? User
        fetchData(user?.id.toString())

    }

    private fun fetchData(userId:String) {
        dummyService.getCart(userId).enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    arr = response.body()?.products?: emptyList()
                    cartAdapter = CartsAdaptors(requireContext(), arr)
                    cart_listview.adapter = cartAdapter
                }

            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
           Toast.makeText(context,"Request error",Toast.LENGTH_SHORT).show()
            }
        })
    }
    companion object {
        @JvmStatic
        fun newInstance(user: User?): OrdersFragment {
            val fragment = OrdersFragment()
            val args = Bundle()
            args.putSerializable("user", user)
            fragment.arguments = args
            return fragment
        }
    }

}
