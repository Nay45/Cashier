package com.example.appskasir

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appskasir.Adapter.AdapterCart
import com.example.appskasir.Database.CafeDatabase
import com.example.appskasir.Database.DetailTransaksi
import com.example.appskasir.Database.Menu
import com.example.appskasir.Database.Transaksi
import com.example.appskasir.databinding.ActivityCartBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var db: CafeDatabase
    private lateinit var adapterCart: AdapterCart

    private var listCart = arrayListOf<Int>()
    private var listMenu = arrayListOf<Menu>()

    private var id_user: Int = 0
//    private var addAgain: Boolean = false
    private var id_transaksi: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = CafeDatabase.getInstance(applicationContext)
        setupView()
        setupListener()
        setupRecyclerView()
    }

    private fun setupView() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        extras?.let {
            listCart = it.getIntegerArrayList("CART") ?: arrayListOf()
            id_user = it.getInt("id_user", 0)
            id_transaksi = it.getInt("id_transaksi", 0)
//            addAgain = it.getBoolean("addAgain", false)
        }

        listCart.forEach { i ->
            val menu = db.cafeDao().getMenu(i)
            listMenu.add(menu)
        }

        setDataSpinner()
    }

    private fun setupListener() {
//        var status = "Belum Bayar"

//        if (addAgain) {
//            val transaksi = db.cafeDao().getTransaksi(id_transaksi)
//            binding.edtCusName.setText(transaksi.visitorName)
//            binding.spinnerTable.setSelection(transaksi.idMeja)
//            if (transaksi.status == "Dibayar") {
//                binding.payment.isChecked = true
//            }
//        }

        val total = listMenu.sumBy { it.price }
        binding.Checkout.text = "Checkout (${listMenu.size}) Rp.$total"

        binding.Checkout.setOnClickListener {
//            if (addAgain) {
//                listMenu.forEach { i ->
//                    db.cafeDao().addDetailTransaksi(
//                        DetailTransaksi(
//                            null,
//                            id_transaksi,
//                            i.id!!,
//                            i.price
//                        )
//                    )
//                }
//                finish()
//            } else {
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                val current = LocalDateTime.now().format(formatter)

//                if (binding.payment.isChecked) {
//                    status = "Dibayar"
//                }

                if (binding.edtCusName.text.toString().isEmpty()) {
                    Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                } else {
                    val newTransaksi = Transaksi(
                        null,
                        current,
                        id_user,
                        db.cafeDao().getIdMejaFromNama(binding.spinnerTable.selectedItem.toString()),
                        binding.edtCusName.text.toString(),
                        "Dibayar"
                    )
                    db.cafeDao().addTransaksi(newTransaksi)

                    val idtransaksi = db.cafeDao().getIdTransaksiFromOther(
                        newTransaksi.date,
                        newTransaksi.idUser,
                        newTransaksi.idMeja,
                        newTransaksi.visitorName,
                        newTransaksi.status
                    )

                    listMenu.forEach { i ->
                        db.cafeDao().addDetailTransaksi(
                            DetailTransaksi(
                                null,
                                idtransaksi,
                                i.id!!,
                                i.price
                            )
                        )
                    }
                    finish()
                }
//            }
        }
    }

    private fun setupRecyclerView() {
        adapterCart = AdapterCart(listMenu)

        binding.recyclerCart.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = adapterCart
        }
    }

    private fun setDataSpinner(){
        val adapter = ArrayAdapter(applicationContext, R.layout.simple_spinner_item, db.cafeDao().getAllNamaMeja())
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerTable.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}