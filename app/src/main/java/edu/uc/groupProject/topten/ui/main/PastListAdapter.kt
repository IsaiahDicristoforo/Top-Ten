package edu.uc.groupProject.topten.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.ListItem

//Handles the items put INTO the recyclerview, and connects to the PastListFragments class
class PastListAdapter(private val mvm: PastListsViewModel, private var listItems: ArrayList<ListItem>): RecyclerView.Adapter<PastListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.layout_past_list_item,
            viewGroup,
            false
        )
        return ViewHolder(view)
    }

    fun setItemList(list: ArrayList<ListItem>) {
        if (list == null) {
            listItems = list
            notifyItemRangeInserted(0, list.size)
        } else {
            var result: DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return listItems.size
                }

                override fun getNewListSize(): Int {
                    return list.size;
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

                    return listItems[oldItemPosition].id == list[newItemPosition].id
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    var oldItem: ListItem = listItems[oldItemPosition]
                    var newItem: ListItem = list[newItemPosition]

                    return oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.totalVotes == newItem.totalVotes
                }

            })
        }
    }

    //identifies the IDs of the items in the layout_past_list_item fragment
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val listName: TextView
        init{
            listName = view.findViewById(R.id.tv_listName)
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listName.text = listItems[position].title //THIS NEEDS TO BE CHANGED! CURRENTLY ONLY PRODUCES THE NAME OF A LIST ITEM, NOT THE LIST TITLE
    }

    override fun getItemCount(): Int {
        return listItems.size
    }
}