package com.example.appskasir.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appskasir.Database.CafeDatabase
import com.example.appskasir.Database.DetailTransaksi
import com.example.appskasir.Database.Menu
import com.example.appskasir.Database.Transaksi
import com.example.appskasir.R
import kotlinx.android.synthetic.main.adapter_transaction.view.*
import kotlinx.android.synthetic.main.adapter_transaction.view.dateRow
import kotlinx.android.synthetic.main.adapter_transaction.view.numRow
import kotlinx.android.synthetic.main.adapter_transaction.view.priceRow
import kotlinx.android.synthetic.main.adapter_transaction_cashier.view.*
import java.text.NumberFormat
import java.util.*

class AdapterTransactionCashier(private val db: CafeDatabase, private var items: MutableList<Transaksi>, private var details: List<DetailTransaksi>, private var menus: List<Menu>) :
    RecyclerView.Adapter<AdapterTransactionCashier.TransactionCashierViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterTransactionCashier.TransactionCashierViewHolder {
        return TransactionCashierViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.adapter_transaction_cashier,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TransactionCashierViewHolder, position: Int) {
        try {
            val transaction = items[position]

            val mejaId = transaction.idMeja
            val mejaName = db.cafeDao().getAllNamaMeja().getOrNull(mejaId - 1) ?: ""

            val menuItems = mutableListOf<String>()
            val totalPrice = calculateTotalPrice(transaction.idTransaction ?: 0, menuItems)

            val menuText = menuItems.joinToString(", ")

            holder.view.numRow.text = transaction.idTransaction.toString()
            holder.view.cusRow.text = transaction.visitorName
            holder.view.tableRow.text = mejaName
            holder.view.dateRow.text = transaction.date
            holder.view.menuRow.text = menuText
            holder.view.priceRow.text = totalPrice.toRupiahFormat()
        } catch (e: Exception) {
            Log.e("AdapterTransactionCashier", "Error fetching data: ${e.message}", e)
        }
    }

    private fun calculateTotalPrice(transactionId: Int, menuItems: MutableList<String>): Int {
        var totalPrice = 0
        for (detailTransaksi in details) {
            if (detailTransaksi.idTransaction == transactionId) {
                val menuId = detailTransaksi.idMenu
                val menu = menus.find { it.id == menuId }
                menu?.let {
                    menuItems.add("${it.name}")
                    totalPrice += detailTransaksi.price
                }
            }
        }
        return totalPrice
    }

    fun Int.toRupiahFormat(): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(this)
    }

    inner class TransactionCashierViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(newList: List<Transaksi>, newDetails: List<DetailTransaksi>, newMenus: List<Menu>) {
        items.clear()
        items.addAll(newList)
        details = newDetails
        menus = newMenus
        notifyDataSetChanged()
    }
}