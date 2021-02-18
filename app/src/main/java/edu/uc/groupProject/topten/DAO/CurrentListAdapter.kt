package edu.uc.groupProject.topten.DAO

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.R

class CurrentListAdapter(private val listItems: ArrayList<ListItem>):RecyclerView.Adapter<CurrentListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): CurrentListAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_current_list_item,viewGroup, false)
        return  ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val listItemTitle: TextView
        val totalVotes: TextView

        init {
            listItemTitle = view.findViewById(R.id.txt_Title)
            totalVotes = view.findViewById(R.id.txt_TotalVotes)
        }
    }

    override fun onBindViewHolder(holder: CurrentListAdapter.ViewHolder, position: Int) {

        holder.listItemTitle.text = listItems[position].title
        holder.totalVotes.text = listItems[position].totalVotes.toString()

    }

    override fun getItemCount(): Int {

        return listItems.size
    }


}