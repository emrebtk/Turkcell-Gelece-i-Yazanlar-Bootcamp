package com.example.emre_bitik_final

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emre_bitik_final.MainActivity
import com.example.emre_bitik_final.configs.ApiClient
import com.example.emre_bitik_final.models.User
import com.example.emre_bitik_final.models.UserLogin
import com.example.emre_bitik_final.ui.theme.Emre_Bitik_FinalTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext


class LoginActivity : ComponentActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var remoteConfig: FirebaseRemoteConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val state = intent.getStringExtra("quit")
        viewModel = LoginViewModel(applicationContext)
        remoteConfig = FirebaseRemoteConfig.getInstance()
        setContent {
            Emre_Bitik_FinalTheme {
                Surface(
                    border = BorderStroke(5.dp, color = Color.Blue),
                    modifier = Modifier.padding(all = 16.dp),
                    color = Color(156, 230, 186)

                ) {
                    LoginScreen(viewModel)
                    if (state=="ok"){
                        //Çıkış yapıldıgında cachedeki kullanıcı verileri silinsin
                        LoginViewModel(applicationContext).clearUserFromSharedPreferences()
                    }
                }
            }
        }

        // Otomatik giriş kontrolü
        viewModel.autoLogin(this)
        setupRemoteConfig()
    }
    private fun setupRemoteConfig() {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        fetchRemoteConfig()
    }

    private fun fetchRemoteConfig() {
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.e("onUpdate", "Updated keys: " + configUpdate.updatedKeys)

                if (configUpdate.updatedKeys.contains("backGroundColor")) {
                    remoteConfig.activate().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val color = FirebaseRemoteConfig.getInstance().getString("backGroundColor")
                            Log.d("Pull Color", color)
                            updateBackgroundColor(color)
                        }
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.w("onError", "Config update error with code: " + error.code, error)
            }
        })
    }

    private fun updateBackgroundColor(color: String) {
        val backgroundColor = Color(android.graphics.Color.parseColor(color))
        Log.d("Pull Color2", "$backgroundColor")
        // Compose tarafında arka plan rengini güncelleme
        setContent {
            Emre_Bitik_FinalTheme {
                Surface(
                    color = backgroundColor,
                    modifier = Modifier.fillMaxSize()
                ) {
                    LoginScreen(viewModel)
                }
            }
        }

    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    val context = LocalContext.current as Activity
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Android AVM", fontSize = 35.sp, fontWeight = FontWeight.Bold, color = Color(15, 219, 98))
        Spacer(modifier = Modifier.height(60.dp))
        Icon(
            painter = painterResource(id = R.drawable.loginpage_con),
            contentDescription = "Android Icon",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(44.dp))
        Text("Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(50.dp))

        // Kullanıcı adı giriş alanı
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Şifre giriş alanı
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Beni hatırla seçeneği
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Remember me", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Giriş yap butonu
        Button(
            onClick = {
                viewModel.userLogin(username, password, context)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}


class LoginViewModel(private val context: Context) {

    private val apiService = ApiClient.getService()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    // Kullanıcı giriş yapmışsa ve "Beni Hatırla" seçeneği işaretliyse otomatik giriş yap
    fun autoLogin(activity: Activity) {

        if (getRememberMeFromSharedPreferences()) {
            val savedUser = getUserFromSharedPreferences()
            savedUser?.let {
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra("user", it)
                activity.startActivity(intent)
                activity.finish()
            }
        }
    }

    fun userLogin(username: String, password: String, activity: Activity, rememberMe: Boolean = false) {
        val userLogin = UserLogin(username, password)

        apiService.userLogin(userLogin).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, res: Response<User>) {
                if (res.isSuccessful) {
                    val user = res.body()
                    user?.let {
                        // Başarılı giriş durumunda kullanıcı bilgilerini SharedPreferences'e kaydet
                        saveUserToSharedPreferences(user, rememberMe)

                        // MainActivity'e geçiş yap
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra("user", user)
                        activity.startActivity(intent)
                        activity.finish()
                    }
                } else {
                    Toast.makeText(context, "Username or password is wrong", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(context, "Connection Error Occurred", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Kullanıcı bilgilerini SharedPreferences'e kaydetme
    private fun saveUserToSharedPreferences(user: User, rememberMe: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putString("user_json", Gson().toJson(user))
        editor.putBoolean("remember_me", rememberMe)
        editor.apply()
    }

    // SharedPreferences'ten kayıtlı kullanıcıyı alma
    private fun getUserFromSharedPreferences(): User? {
        val userJson = sharedPreferences.getString("user_json", null)
        return if (userJson != null) Gson().fromJson(userJson, User::class.java) else null
    }

    // SharedPreferences'ten "Beni Hatırla" seçeneğini alma
   private  fun getRememberMeFromSharedPreferences(): Boolean {
        return sharedPreferences.getBoolean("remember_me", false)
    }

    // SharedPreferences'te kayıtlı kullanıcı bilgilerini silme
    fun clearUserFromSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.remove("user_json")
        editor.remove("remember_me")
        editor.apply()
    }
}
