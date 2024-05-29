package com.example.smkn1cianjur_06_satriaanandabintang.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.smkn1cianjur_06_satriaanandabintang.activity.LoginActivity
import com.example.smkn1cianjur_06_satriaanandabintang.databinding.FragmentProfileBinding
import com.example.smkn1cianjur_06_satriaanandabintang.model.User
import com.example.smkn1cianjur_06_satriaanandabintang.utils.Constants
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.json.JSONException

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        refreshLayout = binding.profileRefreshLayout
        binding.profileBtnLogout.setOnClickListener { logout() }
        loadProfile()
        return binding.root
    }

    private fun loadProfile() {
        refreshLayout.isRefreshing = true

        val request = object : JsonObjectRequest(
            Method.GET,
            "${Constants.BASE_URL}/api/user",
            null,
            { response ->
                try {
                    val user = Gson().fromJson(response.toString(), User::class.java)
                    binding.apply {
                        profileTextUsername.text = user.username
                        profileTextAddress.text = user.address
                        Picasso.get().load(user.image).into(profileImgProfile)
                    }
                } catch (e: JSONException) {
                    Toast.makeText(requireContext(), "JSON error", Toast.LENGTH_SHORT).show()
                    Log.e("JSON error", e.toString())
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Response error", Toast.LENGTH_SHORT).show()
                Log.e("Response error", error.toString())
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return Constants.getHeaders(true, requireContext())
            }
        }

        Volley.newRequestQueue(requireContext()).add(request)
        refreshLayout.isRefreshing = false
    }

    private fun logout() {
        refreshLayout.isRefreshing = true

        val request = object : JsonObjectRequest(
            Method.GET,
            "${Constants.BASE_URL}/api/logout",
            null,
            { _ ->
                Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
            },
            { error ->
                Toast.makeText(requireContext(), "Response error", Toast.LENGTH_SHORT).show()
                Log.e("Response error", error.toString())
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return Constants.getHeaders(true, requireContext())
            }
        }

        Volley.newRequestQueue(requireContext()).add(request)
        refreshLayout.isRefreshing = false
    }

}