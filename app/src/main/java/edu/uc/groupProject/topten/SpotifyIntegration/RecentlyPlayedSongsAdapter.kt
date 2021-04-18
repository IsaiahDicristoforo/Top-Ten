package edu.uc.groupProject.topten.SpotifyIntegration


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.ListItem

class RecentlyPlayedSongsAdapter(private var songs: ArrayList<Song>, var itemListener:ItemListener): RecyclerView.Adapter<RecentlyPlayedSongsAdapter.ViewHolder>() {

     var itemsToAddToDatabase:ArrayList<ListItem> = ArrayList<ListItem>()


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.spotify_list_item,
            viewGroup,
            false)

        return ViewHolder(view)
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        lateinit var song:Song

        override fun onClick(v: View?) {

            if(this@RecentlyPlayedSongsAdapter.itemListener != null){
            itemListener.onItemClick(song)
        }
    }
    init{
        view.setOnClickListener(this)
    }
        val songName: TextView = view.findViewById(R.id.txt_songName)
        val songArtist: TextView = view.findViewById(R.id.txtArtist)
        val layout: ConstraintLayout = view.findViewById(R.id.spotifyItemLayout)
        var addSongToDatabaseButton = view.findViewById<FloatingActionButton>(R.id.actionButton_AddSpotifyItem).setOnClickListener{

            var id = itemsToAddToDatabase.size + 1

            itemsToAddToDatabase.add(ListItem(id,songName.text.toString(),"",0))
        }

}

override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    holder.song = songs[position]
    holder.songName.setText(songs[position].name)
    holder.songArtist.text= songs[position].artist

       }

    override fun getItemCount(): Int {
        return songs.size
    }

    interface ItemListener {
        fun onItemClick(item: Song?)
    }


}