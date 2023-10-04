package com.example.appskasir.Database

import androidx.room.*

@Dao
interface CafeDao {

//    User
    @Insert
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM User")
    fun getUser(): List<User>

    @Query("SELECT * FROM User WHERE id=:User_id")
    fun getUser(User_id: Int): List<User>

    @Query("SELECT * FROM User WHERE email =:mEmail AND password =:mPass")
    fun login(mEmail: String, mPass: String): List<User>

    @Query("SELECT User.name FROM User INNER JOIN Transaksi ON User.id = Transaksi.idUser")
    fun getUserName(): List<String>

    @Query("SELECT name FROM User WHERE id = :userId")
    fun getUserNameById(userId: Int): String

    //    Menu
    @Insert
    fun addMenu(menu: Menu)

    @Update
    fun updateMenu(menu: Menu)

    @Delete
    fun deleteMenu(menu: Menu)

    @Query("SELECT * FROM Menu")
    fun getMenu(): List<Menu>

    @Query("SELECT * FROM Menu WHERE id=:Menu_id")
    fun getMenu(Menu_id: Int): Menu

//    Meja
    @Insert
    fun addMeja(meja: Meja)

    @Update
    fun updateMeja(meja: Meja)

    @Delete
    fun deleteMeja(meja: Meja)

    @Query("SELECT * FROM Meja")
    fun getMeja(): List<Meja>

    @Query("SELECT * FROM Meja WHERE id=:mejaId")
    fun getMeja(mejaId: Int): List<Meja>

    @Query("SELECT name FROM Meja")
    fun getAllNamaMeja(): List<String>

    @Query("SELECT id FROM Meja WHERE name = :namaMeja")
    fun getIdMejaFromNama(namaMeja: String): Int

//    Transaksi
    @Insert
    fun addTransaksi(transaksi: Transaksi)

    @Update
    fun updateTransaksi(transaksi: Transaksi)

    @Delete
    fun deleteTransaksi(transaksi: Transaksi)

    @Query("SELECT * FROM Transaksi")
    fun getTransaksi(): List<Transaksi>

    @Query("SELECT * FROM Transaksi WHERE idTransaction =:transaksiId")
    fun getTransaksi(transaksiId: Int): Transaksi

    @Query("SELECT idTransaction FROM Transaksi WHERE date = :tglTransaksi AND idUser = :idUser AND idMeja = :idMeja AND visitorName = :namaPelanggan AND status = :Status")
    fun getIdTransaksiFromOther(tglTransaksi: String, idUser: Int, idMeja: Int, namaPelanggan: String, Status: String): Int

    //    Detail Transaksi
    @Insert
    fun addDetailTransaksi(detailTransaksi: DetailTransaksi)

    @Update
    fun updateDetailTransaksi(detailTransaksi: DetailTransaksi)

    @Delete
    fun deleteDetailTransaksi(detailTransaksi: DetailTransaksi)

    @Query("SELECT * FROM DetailTransaksi")
    fun getDetailTransaksi(): List<DetailTransaksi>

    @Query("SELECT * FROM DetailTransaksi WHERE idTransaction =:transaksiId")
    fun getDetailTransaksi(transaksiId: Int): List<DetailTransaksi>

}