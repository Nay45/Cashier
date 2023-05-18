package com.example.appskasir

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.example.appskasir.Database.CafeDatabase
import com.example.appskasir.Database.User
import com.example.appskasir.databinding.ActivitySignUpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    lateinit var db: CafeDatabase

    private lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        db = CafeDatabase.getInstance(applicationContext)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setupListener()
    }

    private fun setupListener() {
        binding.btnSaveUser.setOnClickListener {
            val nameInput = binding.edtName.text.toString()
            val emailInput = binding.edtEmail.text.toString()
            val passInput = binding.edtPass.text.toString()

            if (nameInput.isEmpty() || emailInput.isEmpty() || passInput.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    db.cafeDao().addUser(
                        User(
                            0,
                            nameInput,
                            emailInput,
                            passInput
                        )
                    )
                    finish()
                }
            }
        }

        binding.showpass.setOnClickListener {
            if (binding.showpass.isChecked) {
                binding.edtPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.edtPass.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
