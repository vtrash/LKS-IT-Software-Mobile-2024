package com.example.smkn1cianjur_06_satriaanandabintang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smkn1cianjur_06_satriaanandabintang.R
import com.example.smkn1cianjur_06_satriaanandabintang.databinding.RowProductBinding
import com.example.smkn1cianjur_06_satriaanandabintang.model.Product
import com.squareup.picasso.Picasso

class ProductAdapter(
    private val productList: List<Product>,
    private val addItemListener: (Product, Int) -> Unit
):
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = RowProductBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.binding.apply {
            productTextName.text = product.name
            productTextPrice.text = product.price.toString()
            productTextRating.text = product.rating.toString()
            Picasso.get().load(product.image).into(productImgProduct)

            productBtnPlus.setOnClickListener {
                product.quantity++
                product.subtotal = product.quantity * product.price
                productTextQty.text = product.quantity.toString()
                addItemListener(product, productList.sumOf { it.subtotal })
            }

            productBtnMinus.setOnClickListener {
                if (product.quantity > 0) {
                    product.quantity--
                    product.subtotal = product.quantity * product.price
                    productTextQty.text = product.quantity.toString()
                    addItemListener(product, productList.sumOf { it.subtotal })
                }
            }
        }
    }
}