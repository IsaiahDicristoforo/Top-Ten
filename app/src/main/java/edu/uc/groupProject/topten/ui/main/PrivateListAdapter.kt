package edu.uc.groupProject.topten.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.ListItem
import org.w3c.dom.Text

/**
 * Populates a list of data into a container.
 *
 * @param MainViewModel the main view model
 * @param listItems an array list of incoming data
 * @return RecyclerView.Adapter<CurrentListAdapter.ViewHolder>
 */
class PrivateListAdapter(private val pvm: PrivateListViewModel, private var listItems: ArrayList<String>):RecyclerView.Adapter<PrivateListAdapter.ViewHolder>() {
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
        val listTitle : TextView = view.findViewById(R.id.lblTitle)
    }

    /**
     * Populates a list item
     *
     * @param holder represent the list item
     * @param position its current position in the list
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.listTitle.text = listItems[position]
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