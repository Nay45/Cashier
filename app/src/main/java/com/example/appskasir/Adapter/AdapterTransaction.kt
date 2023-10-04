package com.example.appskasir.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appskasir.Database.CafeDatabase
import com.example.appskasir.Database.DetailTransaksi
import com.example.appskasir.Database.Transaksi
import com.example.appskasir.Database.User
import com.example.appskasir.R
import kotlinx.android.synthetic.main.adapter_menu.view.*
import kotlinx.android.synthetic.main.adapter_transaction.view.*
import kotlinx.android.synthetic.main.adapter_user.view.*
import java.text.NumberFormat
import java.util.*

class AdapterTransaction(private val db: CafeDatabase, private var items: MutableList<Transaksi>, private var details: List<DetailTransaksi>) :
    RecyclerView.Adapter<AdapterTransaction.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.adapter_transaction,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        try {
            val iTransaction = items[position]
            val userId = iTransaction.idUser
            val userName = db.cafeDao().getUserNameById(userId)
            holder.view.numRow.text = iTransaction.idTransaction.toString()
            holder.view.userRow.text = userName
            holder.view.dateRow.text = iTransaction.date

            val totalHarga = calculateTotalPrice(iTransaction.idTransaction ?: 0)
            holder.view.priceRow.text = totalHarga.toRupiahFormat()
        } catch (e: Exception) {
            Log.e("AdapterTransaction", "Error fetching username data: ${e.message}", e)
        }
    }

    private fun calculateTotalPrice(transactionId: Int): Int {
        var totalPrice = 0
        for (detailTransaksi in details) {
            if (detailTransaksi.idTransaction == transactionId) {
                totalPrice += detailTransaksi.price
            }
        }
        return totalPrice
    }

    fun Int.toRupiahFormat(): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(this)
    }

    inner class TransactionViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(newList: List<Transaksi>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}