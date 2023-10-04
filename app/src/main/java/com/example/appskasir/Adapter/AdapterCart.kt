package com.example.appskasir.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appskasir.Database.Menu
import com.example.appskasir.R
import kotlinx.android.synthetic.main.adapter_menu.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterCart(private var items: ArrayList<Menu>): RecyclerView.Adapter<AdapterCart.CartViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.adapter_cart,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val iMenu = items[position]
        holder.view.tv_item_name.text = iMenu.name
        holder.view.tv_item_desc.text = iMenu.desc
        holder.view.tv_item_price.text = iMenu.price.toRupiahFormat()
    }

    fun Int.toRupiahFormat(): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(this)
    }

    inner class CartViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}