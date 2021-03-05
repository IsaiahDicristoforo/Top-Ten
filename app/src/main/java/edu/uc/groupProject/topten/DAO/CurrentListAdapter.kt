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

/**
 * Populates a list of data into a container.
 *
 * @param MainViewModel the main view model
 * @param listItems an array list of incoming data
 * @return RecyclerView.Adapter<CurrentListAdapter.ViewHolder>
 */
class CurrentListAdapter(private val mvm: MainViewModel, private val listItems: ArrayList<ListItem>):RecyclerView.Adapter<CurrentListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): CurrentListAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_current_list_item,viewGroup, false)
        return  ViewHolder(view)
    }

    /**
     * Populates a list item
     *
     * @param view
     * @return RecyclerView.ViewHolder
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

    /**
     * Populates a list item
     *
     * @param holder reprenet the list item
     * @param position its current position in the list
     */
    override fun onBindViewHolder(holder: CurrentListAdapter.ViewHolder, position: Int) {

        holder.listItemTitle.text = listItems[position].title
        holder.totalVotes.text = listItems[position].totalVotes.toString()
        holder.currentRank.text = (position + 1).toString()
        var voteCount = listItems[position].totalVotes

        holder.voteButton.setOnClickListener(){
            holder.voteButton.isClickable = false
            holder.totalVotes.text = (voteCount + 1).toString()
            mvm.addListItemVote(holder.listItemTitle.text.toString())
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