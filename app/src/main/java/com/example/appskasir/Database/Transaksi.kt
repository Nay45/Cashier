package com.example.appskasir.Database

import androidx.room.*

@Entity
data class Transaksi(
    @PrimaryKey(autoGenerate = true)
    val idTransaction: Int?,
    val date: String,
    val idUser: Int,
    val idMeja: Int,
    val visitorName: String,
    val status: String
)