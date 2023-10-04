package com.example.appskasir.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appskasir.Database.Menu
import com.example.appskasir.R
import kotlinx.android.synthetic.main.adapter_menu.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterMenu(private var items: ArrayList<Menu>, private val listener: OnAdapterListener, private val isAdmin: Boolean) :
    RecyclerView.Adapter<AdapterMenu.MenuViewHolder>(){

//    var onAddClick: ((Menu) -> Unit)? = null
//    var onSubstractClick: ((Menu) -> Unit)? = null
    var onCheck: ((Menu) -> Unit)? = null
    var onUnCheck: ((Menu) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.adapter_menu,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val iMenu = items[position]
        holder.view.tv_item_name.text = iMenu.name
        holder.view.tv_item_desc.text = iMenu.desc
        holder.view.tv_item_price.text = iMenu.price.toRupiahFormat()

        if (!isAdmin) {
            holder.view.iconUpdate.visibility = View.GONE
            holder.view.iconDel.visibility = View.GONE
        } else  {
//            holder.view.iconAdd.visibility = View.GONE
//            holder.view.iconMinus.visibility = View.GONE
//            holder.view.numMenu.visibility = View.GONE
            holder.view.iconChecklist.visibility = View.GONE
        }

        holder.view.setOnClickListener {
            listener.onRead(iMenu)
        }
        holder.view.iconUpdate.setOnClickListener {
            listener.onUpdate(iMenu)
        }
        holder.view.iconDel.setOnClickListener {
            listener.onDelete(iMenu)
        }

        val checklistDrawable = if (iMenu.isSelected == false) R.drawable.ic_baseline_check_box_outline_blank_24 else R.drawable.ic_baseline_check_box_24
        holder.view.iconChecklist.setImageResource(checklistDrawable)

        holder.view.iconChecklist.setOnClickListener {
            iMenu.isSelected = !iMenu.isSelected
            notifyItemChanged(position)
            if (!iMenu.isSelected) {
                onUnCheck?.invoke(iMenu)
            } else {
                onCheck?.invoke(iMenu)
            }
        }

//        holder.view.iconAdd.setOnClickListener {
//            var i: Int = holder.view.numMenu.text.toString().toInt() + 1
//            holder.view.numMenu.text = "" + i
//            onAddClick?.invoke(iMenu)
//        }
//
//        holder.view.iconMinus.setOnClickListener{
//            var i: Int = holder.view.numMenu.text.toString().toInt()
//            if(i > 0){
//                i -= 1
//                holder.view.numMenu.text = "" + i
//                onSubstractClick?.invoke(iMenu)
//            }
//        }

    }

    fun Int.toRupiahFormat(): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(this)
    }

    inner class MenuViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(newList: List<Menu>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onRead(menu: Menu)
        fun onUpdate(menu: Menu)
        fun onDelete(menu: Menu)
    }
}