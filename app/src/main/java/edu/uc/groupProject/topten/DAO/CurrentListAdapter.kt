package edu.uc.groupProject.topten.DAO

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
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

        holder.voteButton.setOnClickListener{
            val firestore : FirebaseFirestore
            firestore = FirebaseFirestore.getInstance()
            firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

            addVote(holder.listItemTitle.text.toString(), firestore)



        }
    }

  fun addVote(listItemToIncrement: String, firestore: FirebaseFirestore) {

        var listItemDocument = firestore.document("lists/Top Fifteen Movies/MyListItems/" + listItemToIncrement)

        var totalVotes: Number

        listItemDocument.get().addOnSuccessListener {
            totalVotes = it.getLong("totalVotes")!!
            listItemDocument.update("totalVotes", (totalVotes.toLong() + 1))

        }
    }



    override fun getItemCount(): Int {
        return listItems.size
    }


}