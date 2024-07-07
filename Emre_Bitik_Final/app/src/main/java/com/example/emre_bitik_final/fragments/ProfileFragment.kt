package com.example.emre_bitik_final.fragments
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.emre_bitik_final.LoginActivity
import com.example.emre_bitik_final.R
import com.example.emre_bitik_final.configs.ApiClient
import com.example.emre_bitik_final.models.User
import com.example.emre_bitik_final.services.IDummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private lateinit var p_name: TextView
    private lateinit var p_gender: TextView
    private lateinit var p_email: TextView
    private lateinit var p_image: ImageView
    private lateinit var p_quit: Button
    private lateinit var p_update: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        p_name = view.findViewById(R.id.p_name)
        p_email = view.findViewById(R.id.p_email)
        p_gender = view.findViewById(R.id.p_gender)
        p_image = view.findViewById(R.id.p_image)
        p_quit = view.findViewById(R.id.p_quit)
        p_update = view.findViewById(R.id.p_update)


        val user = arguments?.getSerializable("user") as? User
        val fullname = user?.firstName+" "+user?.lastName
        p_name.text = fullname
        Glide.with(this)
            .load(user?.image)
            .into(p_image)
        val email = "E-Mail : "+" " + user?.email
        p_email.text = email
        val gender = "Gender : "+" " + user?.gender
        p_gender.text = gender
        p_quit.setOnClickListener{
            navigateToLogin()
        }
        p_update.setOnClickListener{
            updateUser(user?.id.toString())
        }
        return view

    }
    // Giriş sayfasına yönlendirme işlemi
    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.putExtra("quit", "ok")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val activityManager = requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.appTasks.forEach { task ->
            task.finishAndRemoveTask()
        }

        startActivity(intent)
    }
    // Kullanıcıyı güncelleme işlemi
    private fun updateUser(id: String){
        var dummyService = ApiClient.getClient().create(IDummyService::class.java)
        dummyService.getUser(id).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val updateUser = response.body()
                    val fullname = updateUser?.firstName+" "+updateUser?.lastName
                    p_name.text = fullname
                    Glide.with(requireContext())
                        .load(updateUser?.image)
                        .into(p_image)
                    val email = "E-Mail : "+" " + updateUser?.email
                    p_email.text = email
                    val gender = "Gender : "+" " + updateUser?.gender
                    p_gender.text = gender
                    Toast.makeText(requireContext(),"Profile Update Successful",Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(context, "Response unsuccessful: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(context, "Error fetching data: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    companion object {
        @JvmStatic
        fun newInstance(user: User?): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putSerializable("user", user)
            fragment.arguments = args
            return fragment
        }
    }
}
