package edu.uc.groupProject.topten.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.ListItem


/**
 * Populates a list of data into a container.
 *
 * @param MainViewModel the main view model
 * @param listItems an array list of incoming data
 * @return RecyclerView.Adapter<CurrentListAdapter.ViewHolder>
 */
class CurrentListAdapter(private val mvm: MainViewModel, private var listItems: ArrayList<ListItem>):RecyclerView.Adapter<CurrentListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.layout_current_list_item,
            viewGroup,
            false
        )
        return  ViewHolder(view)
    }

  fun setItemList(list: ArrayList<ListItem>){
        if(list == null){
            listItems = list
            notifyItemRangeInserted(0, list.size)
        }else{
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

            listItems = list

            var theCallback = RecyclerCallback()
            theCallback.bind(this)

            result.dispatchUpdatesTo(theCallback)

            }

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
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listItemTitle.text = listItems[position].title
        holder.totalVotes.text = listItems[position].totalVotes.toString() + " votes"
        holder.currentRank.text = (position + 1).toString()


        val maxTitleLengthBeforeFontSizeNeedsToBeLowered = 20
        if(listItems[position].title.length > maxTitleLengthBeforeFontSizeNeedsToBeLowered){

            holder.listItemTitle.textSize = 17.0F

        }else{
            holder.listItemTitle.textSize = 24.0f
        }


        holder.voteButton.setOnClickListener(){
            //holder.voteButton.isClickable = false
            mvm.firestoreService.addListItemVote(holder.listItemTitle.text.toString())
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