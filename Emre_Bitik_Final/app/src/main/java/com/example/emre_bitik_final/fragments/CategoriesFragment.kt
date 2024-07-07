package com.example.emre_bitik_final.fragments
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.emre_bitik_final.R
import com.example.emre_bitik_final.adaptors.CategoriesAdaptors
import com.example.emre_bitik_final.configs.ApiClient
import com.example.emre_bitik_final.models.Category
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var categoryAdaptor: CategoriesAdaptors

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(R.id.c_listview)
        // Kategorileri çekme işlemini gerçekleştir
        fetchCategories()

        // ListView öğelerine tıklama dinleyicisi ekle
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedCategory = categoryAdaptor.getItem(position)

            // Seçilen kategorinin name değerini al
            val selectedName = selectedCategory?.name

            // Yeni fragmenta geçiş yapmak için Bundle oluşturma
            val bundle = Bundle()
            bundle.putString("selected_name", selectedName)

            // Yeni fragmenta geçiş yapma
            val categoryFragment = CategoryFragment()
            categoryFragment.arguments = bundle  // Bundle'ı fragmenta ekleyin
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, categoryFragment)
                .addToBackStack(null)  // Geri tuşu ile geri dönüşü sağlar
                .commit()
        }

    }

    private fun fetchCategories() {
        val call = ApiClient.getCategoryService().getCategories()
        call.enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    val categories = response.body()
                    categories?.let {
                        // Kategori listesi geldiğinde adapter ile ListView'e bağla
                        categoryAdaptor = CategoriesAdaptors(requireContext(), categories)
                        listView.adapter = categoryAdaptor
                    }
                } else {
                    Toast.makeText(context, "Response unsuccessful: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Toast.makeText(context, "Error fetching categories: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
