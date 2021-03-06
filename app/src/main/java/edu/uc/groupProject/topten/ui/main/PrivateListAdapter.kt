package edu.uc.groupProject.topten.ui.main

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.PollResponse

/**
 * Populates a list of data into a container.
 *
 * @param MainViewModel the main view model
 * @param listItems an array list of incoming data
 * @return RecyclerView.Adapter<CurrentListAdapter.ViewHolder>
 */
class PrivateListAdapter(private val context: Context, private val pollResponses:ArrayList<PollResponse>, private val pvm: PrivateListViewModel, private var listItems: ArrayList<String?>):RecyclerView.Adapter<PrivateListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.private_list_item,
            viewGroup,
            false
        )

        return  ViewHolder(view)
    }


    /**
     * Populates a list item
     *
     * @param view
     * @return RecyclerView.ViewHolder
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val listTitle : TextView = view.findViewById(R.id.lbl_privateListItemTitle)
        var expandableLayout = view.findViewById<RelativeLayout>(R.id.expandedView)
        var clickableLayout = view.findViewById<LinearLayout>(R.id.nestedListItem)
        var nestedRecyclerView = view.findViewById<RecyclerView>(R.id.nestedRecyclerView)
    }

    /**
     * Populates a list item
     *
     * @param holder represent the list item
     * @param position its current position in the list
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.listTitle.text = listItems[position]

        holder.nestedRecyclerView.layoutManager = LinearLayoutManager(context)
        holder.nestedRecyclerView.adapter = NestedRecyclerViewAdapter(pollResponses[position].choices)


        val isExpandable:Boolean = pollResponses[position].isExpandable

        holder.expandableLayout.visibility = if(isExpandable) View.VISIBLE else View.GONE
        holder.clickableLayout.setOnClickListener{
            val listItems = pollResponses[position]
            listItems.isExpandable = !listItems.isExpandable
            var currentColor =  (holder.clickableLayout.background as ColorDrawable).color
            notifyItemChanged(position)


        }


    }

    /**
     * Return the size of the list
     *
     * @return size
     */
    override fun getItemCount(): Int {
        return listItems.size
    }
}