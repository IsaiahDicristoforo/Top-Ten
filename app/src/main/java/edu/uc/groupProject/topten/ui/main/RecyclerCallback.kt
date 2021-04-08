package edu.uc.groupProject.topten.ui.main

import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView

class RecyclerCallback : ListUpdateCallback {
    var firstInsert = -1
    var adapter: RecyclerView.Adapter<CurrentListAdapter.ViewHolder>? = null

    fun bind(adapter: RecyclerView.Adapter<CurrentListAdapter.ViewHolder>?) {
        this.adapter = adapter
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter?.notifyItemRangeChanged(position, count, payload)
    }

    override fun onInserted(position: Int, count: Int) {
        if (firstInsert == -1 || firstInsert > position) {
            firstInsert = position
        }

        adapter?.notifyItemRangeInserted(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter?.notifyItemMoved(fromPosition, toPosition)
        adapter?.notifyItemChanged(fromPosition)
        adapter?.notifyItemChanged(fromPosition, toPosition)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter?.notifyItemRangeRemoved(position, count)
    }
}
