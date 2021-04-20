package edu.uc.groupProject.topten.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.Poll
import edu.uc.groupProject.topten.dto.PollChoice
import edu.uc.groupProject.topten.dto.PollResponse

class NestedRecyclerViewAdapter(private var listItems: ArrayList<PollChoice>):
    RecyclerView.Adapter<NestedRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.private_list_nested,
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
        val itemTitle = view.findViewById<TextView>(R.id.lbl_privateListItemTitle)
        val  totalVotesLabel  = view.findViewById<TextView>(R.id.txt_privateTotalVotes)
        val voteButton = view.findViewById<Button>(R.id.btn_privateVote)
    }

    /**
     * Populates a list item
     *
     * @param holder represent the list item
     * @param position its current position in the list
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemTitle.text = listItems[position].choice
        holder.totalVotesLabel.text = listItems[position].votes.toString()  + " votes"

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