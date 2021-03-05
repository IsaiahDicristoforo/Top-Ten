package edu.uc.groupProject.topten.DAO

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.ui.main.MainViewModel

/*  By Benjamin Gomori Code Review
    I don't think that the RecyclerView Adapter should not be using firebase directly.
    As I understand the RecyclerView Adapter - it should use ready data to populate the recycle view.
    The ViewModels should be the centralized location for feeding live and firebase data.
*/
class CurrentListAdapter(private val mvm: MainViewModel, private val listItems: ArrayList<ListItem>):RecyclerView.Adapter<CurrentListAdapter.ViewHolder>() {
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
        val currentRank: TextView
        val voteButton: ImageButton

        init {
            listItemTitle = view.findViewById(R.id.txt_Title)
            totalVotes = view.findViewById(R.id.txt_TotalVotes)
            currentRank = view.findViewById(R.id.txt_Rank)
            voteButton = view.findViewById(R.id.btn_Vote)
        }
    }

    override fun onBindViewHolder(holder: CurrentListAdapter.ViewHolder, position: Int) {

        holder.listItemTitle.text = listItems[position].title
        holder.totalVotes.text = listItems[position].totalVotes.toString()
        holder.currentRank.text = (position + 1).toString()
        var voteCount = listItems[position].totalVotes

        holder.voteButton.setOnClickListener(){
            /*  By Benjamin Gomori Code Review
                [1] Once a user voted, that ViewHolder vote button should be disabled.
                [2] There might not be a need to use synced data coming in from firebase to keep the vote count in-synced.
                Basing the UI vote count on firebase may not be the most user friendly, as it will not update instantly.
                Instead in such cases, when the update in data is always predictable, the UI count can be updated programmatically,
                and data should be sent to update Firebase.
             */
            holder.voteButton.isClickable = false
            holder.totalVotes.text = (voteCount + 1).toString()
            mvm.addListItemVote(holder.listItemTitle.text.toString())
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }
}