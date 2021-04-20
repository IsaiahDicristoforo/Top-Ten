package edu.uc.groupProject.topten.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.Poll
import edu.uc.groupProject.topten.dto.PollChoice
import edu.uc.groupProject.topten.dto.PollResponse
import edu.uc.groupProject.topten.service.PollService

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
        var poll = PollService()

        holder.itemTitle.text = listItems[position].choice
        holder.totalVotesLabel.text = listItems[position].votes.toString()  + " votes"

        holder.voteButton.setOnClickListener{
            var url = listItems[position].url
            var urlArrary = url?.split('/')

            var votedPoll = poll.castVote(urlArrary[2].toInt(), urlArrary[4].toInt())

            var votes = listItems[position].votes
            votes = votes + 1

            holder.totalVotesLabel.text = votes.toString()  + " votes"
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