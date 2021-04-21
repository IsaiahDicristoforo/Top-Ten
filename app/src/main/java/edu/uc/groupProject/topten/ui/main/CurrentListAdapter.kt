package edu.uc.groupProject.topten.ui.main

import android.animation.Animator
import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.ListItem


/**
 * Populates a list of data into a container.
 *
 * @param MainViewModel the main view model
 * @param listItems an array list of incoming data
 * @return RecyclerView.Adapter<CurrentListAdapter.ViewHolder>
 */
class CurrentListAdapter(private val mvm: MainViewModel, private var listItems: ArrayList<ListItem>, private var currentActivity : Context):RecyclerView.Adapter<CurrentListAdapter.ViewHolder>() {
    var newList: Boolean = false
    var userHasVoted:Boolean  = false
    var notifyDatasetChanged:Boolean = false
    var listItemTitle = ""
    var lastClickedListItemTitle = ""

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

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)

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
        val buttonClickAnimation:LottieAnimationView

        init {
            listItemTitle = view.findViewById(R.id.txt_Title)
            totalVotes = view.findViewById(R.id.txt_TotalVotes)
            currentRank = view.findViewById(R.id.txt_Rank)
            voteButton = view.findViewById(R.id.btn_Vote)
            buttonClickAnimation = view.findViewById(R.id.animationView)


        }
    }



    /**
     * Populates a list item
     *
     * @param holder represent the list item
     * @param position its current position in the list
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listItemTitle.text = listItems[position].title



        val maxTitleLengthBeforeFontSizeNeedsToBeLowered = 20
        if(listItems[position].title.length > maxTitleLengthBeforeFontSizeNeedsToBeLowered){

            holder.listItemTitle.textSize = 17.0F

        }else{
            holder.listItemTitle.textSize = 24.0f
        }


        if(!currentActivity.getSharedPreferences("HasVoted", Context.MODE_PRIVATE).getBoolean("HasVoted", false)){
            holder.totalVotes.text = "?"
            holder.currentRank.text = "?"
        }else{
            holder.totalVotes.text = listItems[position].totalVotes.toString() + " votes"
            holder.currentRank.text = (position + 1).toString()
        }

        holder.voteButton.setImageResource(android.R.drawable.btn_star_big_off)

        if(lastClickedListItemTitle == holder.listItemTitle.text.toString()){
            holder.voteButton.setImageResource(android.R.drawable.btn_star_big_on)
        }


        holder.voteButton.setOnClickListener(){

            currentActivity.getSharedPreferences("HasVoted", Context.MODE_PRIVATE).edit().putString("VotedOnTitle",holder.listItemTitle.text.toString()).apply()

            var animation:Animation= AnimationUtils.loadAnimation(currentActivity,R.anim.vote_button_animation)
            holder.voteButton.startAnimation(animation)


            lastClickedListItemTitle = holder.listItemTitle.text.toString()
            userHasVoted = currentActivity.getSharedPreferences("HasVoted", Context.MODE_PRIVATE).getBoolean("HasVoted",false)
            if(!userHasVoted){
                userHasVoted = true
                currentActivity.getSharedPreferences("HasVoted", Context.MODE_PRIVATE).edit().putBoolean("HasVoted",true).apply()
                notifyDatasetChanged = true


                mvm.firestoreService.addListItemVote(holder.listItemTitle.text.toString())

                holder.voteButton.setImageResource(android.R.drawable.btn_star_big_on)
                holder.totalVotes.text = listItems[position].totalVotes.toString() + " votes"
                holder.currentRank.text = (position + 1).toString()
                holder.buttonClickAnimation.speed = 2.0f
                holder.buttonClickAnimation.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        notifyDataSetChanged()

                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }


                    override fun onAnimationStart(animation: Animator?) {
                        //Do nothing
                    }

                })

                holder.buttonClickAnimation.playAnimation()

            }else{
                notifyDatasetChanged = false

                mvm.firestoreService.addListItemVote(holder.listItemTitle.text.toString())

                holder.voteButton.setImageResource(android.R.drawable.btn_star_big_on)
                holder.buttonClickAnimation.playAnimation()
                holder.totalVotes.text = listItems[position].totalVotes.toString() + " votes"
                holder.currentRank.text = (position + 1).toString()
            }

            //userHasVoted = currentActivity.getPreferences(Context.MODE_PRIVATE).getBoolean("HasVoted",false)

        }

        if(newList){
            holder.voteButton.setImageResource(android.R.drawable.btn_star_big_off)
            newList = false

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