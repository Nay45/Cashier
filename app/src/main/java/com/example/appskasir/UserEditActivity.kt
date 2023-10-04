package com.example.appskasir

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import android.widget.Toast
import com.example.appskasir.Database.CafeDatabase
import com.example.appskasir.Database.Constant
import com.example.appskasir.Database.User
import com.example.appskasir.databinding.ActivityUserEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserEditActivity : AppCompatActivity() {

    lateinit var db: CafeDatabase

    private var Id: Int = 0

    private lateinit var binding : ActivityUserEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = CafeDatabase.getInstance(applicationContext)
        setupView()
        setupListener()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when(intentType){
            Constant.TYPE_UPDATE -> {
                getUser()
            }
        }
    }

    private fun setupListener() {
        binding.btnUpdateUser.setOnClickListener {
            val nameInput = binding.edtNameUser.text.toString()
            val emailInput = binding.edtEmailUser.text.toString()
            val passInput = binding.edtPassUser.text.toString()
            val roleInput = binding.spinnerRole.getItemAtPosition(binding.spinnerRole.selectedItemPosition).toString()

            if (nameInput.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    db.cafeDao().updateUser(
                        User(
                            Id,
                            nameInput,
                            emailInput,
                            passInput,
                            roleInput
                        )
                    )
                    finish()
                }
            }
        }
    }

    fun getUser(){
        Id = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val user = db.cafeDao().getUser(Id)[0]
            binding.edtNameUser.setText(user.name)
            binding.edtEmailUser.setText(user.email)
            binding.edtPassUser.setText(user.password)
            binding.spinnerRole.setSelection(getIndex(binding.spinnerRole, user.role))
        }
    }

    private fun getIndex(spinner: Spinner, category: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(category, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}