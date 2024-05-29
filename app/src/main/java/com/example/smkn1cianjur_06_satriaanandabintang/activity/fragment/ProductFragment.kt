package com.example.smkn1cianjur_06_satriaanandabintang.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.smkn1cianjur_06_satriaanandabintang.activity.InvoiceActivity
import com.example.smkn1cianjur_06_satriaanandabintang.adapter.ProductAdapter
import com.example.smkn1cianjur_06_satriaanandabintang.databinding.FragmentProductBinding
import com.example.smkn1cianjur_06_satriaanandabintang.model.Product
import com.example.smkn1cianjur_06_satriaanandabintang.utils.Constants
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var productList: List<Product>
    private var subtotal = 0
    private var ordersId = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        refreshLayout = binding.productRefreshLayout
        recyclerView = binding.productRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.productBtnPay.setOnClickListener {
            if (subtotal == 0) {
                Toast.makeText(requireContext(), "Pilih item terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                Log.i("Encode keren", encodeToString())

//                startActivity(Intent(requireContext(), InvoiceActivity::class.java)
//                    .putExtra("ordersId", "anjasmara")
//                )
            }
        }
//        orders = mutableListOf()
        loadProduct()
        return binding.root
    }

    private fun loadProduct() {
        refreshLayout.isRefreshing = true

        val request = object : JsonObjectRequest(
            Method.GET,
            "${Constants.BASE_URL}/api/products",
            null,
            { response ->
                try {
                    val productJSON = response.getString("data")
                    productList = jsonToList(productJSON)
                    recyclerView.adapter = ProductAdapter(productList) {product, totalPrice ->
                        ordersId.add(product.id)
                        subtotal = totalPrice
                        binding.productTextTotalPrice.text = "Total Harga: Rp$subtotal"
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

    private fun jsonToList(productJSON: String): List<Product> {
        val productList = mutableListOf<Product>()

        val productArray =  JSONArray(productJSON)
        for (i in 0..<productArray.length() - 1) {
            val product = Gson().fromJson(productArray[i].toString(), Product::class.java)
            productList.add(product)
        }

        return productList
    }

    private fun encodeToString(): String {
        var result = ""
        var total = 0
        ordersId.sort()
        Log.i("order id", ordersId.toString())
        for (i in 0..<ordersId.size - 1) {
            if (ordersId[i] == ordersId[i + 1]) {
                total += 1
            } else {
                result += "${ordersId[i]}${total + 1}"
                total = 0
            }
            Log.i("uhuy", i.toString())
        }
        return result
    }
//    private fun addOrders(product: Product) {
//        var totalPrice = 0
//
//        if (orders.size == 0) {
//            orders.add(product)
//            totalPrice = product.price
//        } else {
//            for (order in orders) {
//                totalPrice += product.subtotal
//                if (order.id != product.id) {
//                    orders.add(product)
//                    continue
//                }
//                if (order.quantity > product.quantity) {
//                    order.quantity--
//                } else {
//                    order.quantity++
//                }
//                order.subtotal = product.subtotal
//            }
//        }
//
//
//        binding.productTextTotalPrice.text = "Total Harga: Rp$totalPrice"
//    }
}