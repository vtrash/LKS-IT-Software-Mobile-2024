package com.example.smkn1cianjur_06_satriaanandabintang.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.smkn1cianjur_06_satriaanandabintang.SharedPrefs
import com.example.smkn1cianjur_06_satriaanandabintang.databinding.ActivityLoginBinding
import com.example.smkn1cianjur_06_satriaanandabintang.utils.Constants
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        refreshLayout = binding.loginRefreshLayout
        setContentView(binding.root)

        binding.loginBtnLogin.setOnClickListener { login() }
        binding.loginBtnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun validateInput(): Boolean {
        if (
            TextUtils.isEmpty(binding.loginEditUsername.text) ||
            TextUtils.isEmpty(binding.loginEditPassword.text)
        ) {
            Toast.makeText(this, "Username / Password wajib diisi", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun login() {
        if (!validateInput()) return

        refreshLayout.isRefreshing = true

        val credentials = HashMap<String, String>()
        credentials["username"] = binding.loginEditUsername.text.toString()
        credentials["password"] = binding.loginEditPassword.text.toString()

        val request = object : JsonObjectRequest(
            Method.POST,
            "${Constants.BASE_URL}/api/login",
            JSONObject(credentials.toString()),
            { response ->
                try {
                    val token = response.getString("data")
                    SharedPrefs.putString(this, SharedPrefs.TOKEN, token)
                    Toast.makeText(this, "Berhasil login", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } catch (e: JSONException) {
                    Toast.makeText(this, "JSON error", Toast.LENGTH_SHORT).show()
                    Log.e("JSON error", e.toString())
                }
            },
            { error ->
                Toast.makeText(this, "Username / Password salah", Toast.LENGTH_SHORT).show()
                Log.e("Response error", error.toString())
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return Constants.getHeaders(false)
            }
        }

        Volley.newRequestQueue(this).add(request)
        refreshLayout.isRefreshing = true
    }
}