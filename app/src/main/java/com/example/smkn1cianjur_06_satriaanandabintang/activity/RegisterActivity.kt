package com.example.smkn1cianjur_06_satriaanandabintang.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.smkn1cianjur_06_satriaanandabintang.SharedPrefs
import com.example.smkn1cianjur_06_satriaanandabintang.databinding.ActivityRegisterBinding
import com.example.smkn1cianjur_06_satriaanandabintang.utils.Constants
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        refreshLayout = binding.registerRefreshLayout
        setContentView(binding.root)

        binding.registerBtnRegister.setOnClickListener { register() }
        binding.registerBtnLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun validateInput(): Boolean {
        if (
            TextUtils.isEmpty(binding.registerEditName.text) ||
            TextUtils.isEmpty(binding.registerEditUsername.text) ||
            TextUtils.isEmpty(binding.registerEditAddress.text) ||
            TextUtils.isEmpty(binding.registerEditPassword.text) ||
            TextUtils.isEmpty(binding.registerEditConfirmPassword.text)
        ) {
            Toast.makeText(this, "Semua input wajib diisi", Toast.LENGTH_SHORT).show()
            return false
        }

        if (
            binding.registerEditPassword.text.toString() !=
            binding.registerEditConfirmPassword.text.toString()
        ) {
            Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun register() {
        if (!validateInput()) return

        refreshLayout.isRefreshing = true

        val registerData = HashMap<String, String>()
        registerData["name"] = binding.registerEditName.text.toString()
        registerData["username"] = binding.registerEditUsername.text.toString()
        registerData["address"] = binding.registerEditAddress.text.toString()
        registerData["password"] = binding.registerEditConfirmPassword.text.toString()
        registerData["password_confirmation"] = binding.registerEditConfirmPassword.text.toString()

        val request = object : JsonObjectRequest(
            Method.POST,
            "${Constants.BASE_URL}/api/register",
            JSONObject(registerData.toString()),
            { response ->
                try {
                    val token = response.getString("data")
                    SharedPrefs.putString(this, SharedPrefs.TOKEN, token)
                    Toast.makeText(this, "Berhasil Register", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
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
        refreshLayout.isRefreshing = false
    }
}