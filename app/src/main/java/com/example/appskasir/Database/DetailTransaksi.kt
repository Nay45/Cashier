package com.example.appskasir.Database

import androidx.room.*

@Entity
data class DetailTransaksi(
    @PrimaryKey(autoGenerate = true)
    val idTransactionDetails: Int?,
    val idTransaction: Int,
    val idMenu: Int,
    val price: Int
)