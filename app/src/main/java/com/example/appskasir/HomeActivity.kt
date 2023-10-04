package com.example.appskasir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appskasir.Adapter.*
import com.example.appskasir.Database.*
import com.example.appskasir.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    private lateinit var txtUserName: TextView
    private lateinit var txtRole: TextView

    private var userName: String? = null
    private var role: String? = null

    private lateinit var binding : ActivityHomeBinding

    lateinit var adapterMenu: AdapterMenu
    lateinit var adapterTable: AdapterTable
    lateinit var adapterUser: AdapterUser
    lateinit var adapterTransaction: AdapterTransaction
    lateinit var adapterTransactionCashier: AdapterTransactionCashier

    lateinit var db: CafeDatabase

    private var listCart = arrayListOf<Int>()

    private var searchQuery: String = ""
    private var userRole: String? = null

    var id_user: Int = 0

    private var details: List<DetailTransaksi> = emptyList()
    private var menus: List<Menu> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = CafeDatabase.getInstance(applicationContext)
        details = db.cafeDao().getDetailTransaksi()
        menus = db.cafeDao().getMenu()
        setupView()
        setupListener()
        setupRecyclerView()
        userRole = intent.extras?.getString("role")
    }

    private fun setupView() {
        txtUserName = findViewById(R.id.userName)
        txtRole = findViewById(R.id.roleUser)

        val extras = intent.extras
        userName = extras?.getString("name")
        role = extras?.getString("role")
        id_user = extras!!.getInt("id_user", 0)
        txtUserName.text = "Welcome, $userName"
        txtRole.text = "Login as $role"

        if (role.equals("Admin")) {
            binding.buttonCheckout.visibility = View.GONE
            binding.tvTransaction.visibility = View.GONE
            binding.tableIndicator.visibility = View.GONE
            binding.tableIndicatorCashier.visibility = View.GONE
            binding.listTransaction.visibility = View.GONE
        } else if (role.equals("Manager")) {
            binding.buttonCreate.visibility = View.GONE
            binding.buttonCheckout.visibility = View.GONE
            binding.tvMenu.visibility = View.GONE
            binding.listMenu.visibility = View.GONE
            binding.tvUser.visibility = View.GONE
            binding.listUser.visibility = View.GONE
            binding.tvTable.visibility = View.GONE
            binding.listMeja.visibility = View.GONE
            binding.tableIndicatorCashier.visibility = View.GONE

            val layoutParams = binding.listTransaction.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.listTransaction.layoutParams = layoutParams
        } else if (role.equals("Cashier")) {
            binding.buttonCreate.visibility = View.GONE
            binding.tvUser.visibility = View.GONE
            binding.listUser.visibility = View.GONE
            binding.tvTable.visibility = View.GONE
            binding.listMeja.visibility = View.GONE
            binding.tvTransaction.visibility = View.GONE
            binding.tableIndicator.visibility = View.GONE
            binding.listTransaction.visibility = View.GONE
            binding.tableIndicatorCashier.visibility = View.GONE

            val layoutParams = binding.listMenu.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.listMenu.layoutParams = layoutParams
        }
    }

    override fun onStart() {
        super.onStart()
        loadRV()
    }

    fun loadRV(){
        CoroutineScope(Dispatchers.IO).launch {
            val menus = db.cafeDao().getMenu()
            val tables = db.cafeDao().getMeja()
            val users = db.cafeDao().getUser()
            val transaction = db.cafeDao().getTransaksi()
            val details = db.cafeDao().getDetailTransaksi()
            Log.d("MainActivity", "dbResponse: $menus")
            withContext(Dispatchers.Main){
                adapterMenu.setData(menus)
                adapterTable.setData(tables)
                adapterUser.setData(users)
                adapterTransaction.setData(transaction)
                adapterTransactionCashier.setData(transaction, details, menus)
            }
        }
    }

    private fun setupListener() {
        binding.buttonCreate.setOnClickListener {
            intentEdit(0,Constant.TYPE_CREATE)
        }

        binding.buttonCheckout.setOnClickListener {
            intentCheckout(id_user, listCart)
        }
    }

    fun intentCheckout(userId: Int, listCart: ArrayList<Int>) {
        startActivity(
            Intent(applicationContext, CartActivity::class.java)
                .putExtra("id_user", userId)
                .putIntegerArrayListExtra("CART", listCart)
                .putExtra("id_transaksi", 0)
        )
    }

    fun intentEdit(Id: Int, intentType: Int){
        startActivity(
            Intent(applicationContext, MenuEditActivity::class.java)
                .putExtra("intent_id", Id)
                .putExtra("intent_type", intentType)
        )
    }

    fun intentEditMeja(Id: Int, intentType: Int){
        startActivity(
            Intent(applicationContext, MejaEditActivity::class.java)
                .putExtra("intent_id", Id)
                .putExtra("intent_type", intentType)
        )
    }

    fun intentEditUser(Id: Int, intentType: Int){
        startActivity(
            Intent(applicationContext, UserEditActivity::class.java)
                .putExtra("intent_id", Id)
                .putExtra("intent_type", intentType)
        )
    }

    private fun setupRecyclerView() {
        adapterMenu = AdapterMenu(arrayListOf(), object : AdapterMenu.OnAdapterListener {
            override fun onRead(menu: Menu) {
                intentEdit(menu.id, Constant.TYPE_READ)
            }

            override fun onUpdate(menu: Menu) {
                intentEdit(menu.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(menu: Menu) {
                deleteDialog(menu)
            }

        }, isAdmin = role.equals("Admin"))
        binding.listMenu.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = adapterMenu
        }

        adapterTable = AdapterTable(arrayListOf(), object : AdapterTable.OnAdapterListener {
            override fun onUpdate(meja: Meja) {
                intentEditMeja(meja.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(meja: Meja) {
                deleteDialog(meja)
            }

        }, isAdmin = role.equals("Admin"))
        binding.listMeja.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = adapterTable
        }

        adapterUser = AdapterUser(arrayListOf(), object : AdapterUser.OnAdapterListener {
            override fun onUpdate(user: User) {
                intentEditUser(user.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(user: User) {
                deleteDialog(user)
            }

        }, isAdmin = role.equals("Admin"))
        binding.listUser.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = adapterUser
        }

        val transactionList = mutableListOf<Transaksi>()
        adapterTransaction = AdapterTransaction(db, transactionList, details)
        binding.listTransaction.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = adapterTransaction
        }

        adapterTransactionCashier = AdapterTransactionCashier(db, transactionList, details, menus)
        binding.listTransactionCashier.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = adapterTransactionCashier
        }

        if(listCart.size == 0){
            binding.buttonCheckout.visibility = View.INVISIBLE
        }

        adapterMenu.onCheck = {
            listCart.add(it.id)
            binding.buttonCheckout.visibility = View.VISIBLE
        }

        adapterMenu.onUnCheck = {
            listCart.remove(it.id)
            if(listCart.size == 0){
                binding.buttonCheckout.visibility = View.GONE
            }
        }

//        adapterMenu.onAddClick = {
//            listCart.add(it.id)
//            binding.buttonCheckout.isEnabled = true
//            binding.buttonCheckout.visibility = View.VISIBLE
//        }
//
//        adapterMenu.onSubstractClick = {
//            listCart.remove(it.id)
//            if(listCart.size == 0){
//                binding.buttonCheckout.isEnabled = false
//                binding.buttonCheckout.visibility = View.GONE
//            }
//        }
    }

    private fun deleteDialog(menu: Menu){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Sure to remove \"${menu.name}\"?" )
            setNegativeButton("Cancle") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Remove") { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.cafeDao().deleteMenu(menu)
                    loadRV()
                }
            }
        }
        alertDialog.show()
    }

    private fun deleteDialog(meja: Meja){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Sure to remove \"${meja.name}\"?" )
            setNegativeButton("Cancle") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Remove") { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.cafeDao().deleteMeja(meja)
                    loadRV()
                }
            }
        }
        alertDialog.show()
    }

    private fun deleteDialog(user: User){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Sure to remove \"${user.name}\"?" )
            setNegativeButton("Cancle") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Remove") { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.cafeDao().deleteUser(user)
                    loadRV()
                }
            }
        }
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.nav_bar, menu)

        val searchViewItem = menu?.findItem(R.id.search_bar)
        val searchView = searchViewItem?.actionView as SearchView

        val listTransactionItem = menu?.findItem(R.id.list_transaction)

        var isCashierViewVisible = false

        if (userRole != null) {
            if (userRole == "Manager") {
                // Jika peran pengguna adalah "Manager," maka tampilkan SearchView dan sembunyikan listTransaction
                searchView.visibility = View.VISIBLE
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        // Tidak perlu melakukan apa-apa saat teks dikirim
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        // Simpan teks pencarian
                        searchQuery = newText.orEmpty()
                        filterTransactions(searchQuery)
                        return true
                    }
                })
                listTransactionItem?.isVisible = false
            } else if (userRole == "Cashier") {
                listTransactionItem?.isVisible = true
                searchViewItem.isVisible = false
                listTransactionItem?.setOnMenuItemClickListener {
                    if (isCashierViewVisible) {
                        // Jika tampilan Kasir sudah terlihat, tampilkan tvMenu dan listMenu, serta sembunyikan tableIndicatorCashier
                        binding.tvMenu.visibility = View.VISIBLE
                        binding.listMenu.visibility = View.VISIBLE
                        binding.tvTransaction.visibility = View.GONE
                        binding.tableIndicatorCashier.visibility = View.GONE
                    } else {
                        // Jika tampilan Kasir belum terlihat, sembunyikan tvMenu dan listMenu, serta tampilkan tableIndicatorCashier
                        binding.tvMenu.visibility = View.GONE
                        binding.listMenu.visibility = View.GONE
                        binding.tvTransaction.visibility = View.VISIBLE
                        binding.tableIndicatorCashier.visibility = View.VISIBLE

                        val layoutParams = binding.listTransactionCashier.layoutParams
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                        binding.listTransactionCashier.layoutParams = layoutParams
                    }
                    isCashierViewVisible = !isCashierViewVisible
                    true
                }
            } else {
                listTransactionItem?.isVisible = false
                searchViewItem.isVisible = false
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun filterTransactions(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val filteredTransactions = db.cafeDao().getTransaksi()
                .filter { transaction ->
                    val userName = db.cafeDao().getUserNameById(transaction.idUser)
                    val date = transaction.date
                    val isQueryInteger = query.toIntOrNull() != null // Check if the query is an integer

                    if (isQueryInteger) {
                        date.contains(query, true)
                    } else {
                        val dateParts = query.split("-")
                        if (dateParts.size == 3) {
                            val day = dateParts[0].toIntOrNull()
                            val month = dateParts[1].toIntOrNull()
                            val year = dateParts[2].toIntOrNull()

                            if (day != null && month != null && year != null) {
                                val transactionDateParts = date.split("-")
                                val transactionDay = transactionDateParts[0].toIntOrNull()
                                val transactionMonth = transactionDateParts[1].toIntOrNull()
                                val transactionYear = transactionDateParts[2].toIntOrNull()

                                transactionDay == day && transactionMonth == month && transactionYear == year
                            } else {
                                false
                            }
                        } else {
                            userName.contains(query, true)
                        }
                    }
                }
            withContext(Dispatchers.Main) {
                adapterTransaction.setData(filteredTransactions)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getMenu()
        filterTransactions(searchQuery)
    }

    fun getMenu() {
        listCart.clear()
        adapterUser.notifyDataSetChanged()
        adapterTable.notifyDataSetChanged()
        adapterMenu.notifyDataSetChanged()
        binding.buttonCheckout.visibility = View.GONE
    }
}