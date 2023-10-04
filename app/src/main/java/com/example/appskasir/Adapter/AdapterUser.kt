package com.example.appskasir.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appskasir.Database.Menu
import com.example.appskasir.Database.User
import com.example.appskasir.R
import kotlinx.android.synthetic.main.adapter_menu.view.*
import kotlinx.android.synthetic.main.adapter_menu.view.iconDel
import kotlinx.android.synthetic.main.adapter_menu.view.iconUpdate
import kotlinx.android.synthetic.main.adapter_menu.view.tv_item_name
import kotlinx.android.synthetic.main.adapter_user.view.*

class AdapterUser(private var items: ArrayList<User>, private val listener: AdapterUser.OnAdapterListener, private val isAdmin: Boolean) :
    RecyclerView.Adapter<AdapterUser.UserViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.adapter_user,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val iMenu = items[position]
        holder.view.tv_item_name.text = iMenu.name
        holder.view.tv_item_role.text = iMenu.role

        if (isAdmin.equals(false)) {
            holder.view.iconUpdate.visibility = View.GONE
            holder.view.iconDel.visibility = View.GONE
        }

        holder.view.iconUpdate.setOnClickListener {
            listener.onUpdate(iMenu)
        }
        holder.view.iconDel.setOnClickListener {
            listener.onDelete(iMenu)
        }

    }

    inner class UserViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(newList: List<User>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onUpdate(user: User)
        fun onDelete(user: User)
    }
}