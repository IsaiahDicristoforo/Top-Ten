package edu.uc.groupProject.topten.ui.main

import android.animation.Animator
import android.content.Context
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.ListItem
import edu.uc.groupProject.topten.dto.TopTenList
import edu.uc.groupProject.topten.service.FirestoreService


class CustomListGenerationAdapter(public var listItems: ArrayList<ListItem>): Adapter<CustomListGenerationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.custom_list_item_layout,
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


        val customListItem:TextInputEditText
        val deleteCustomListItem:ImageButton


        init {
           customListItem = view.findViewById(R.id.txt_customListItem)
            deleteCustomListItem = view.findViewById(R.id.btn_deleteListItem)


        }
    }

    /**
     * Populates a list item
     *
     * @param holder represent the list item
     * @param position its current position in the list
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.customListItem.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listItems[position].title = holder.customListItem.text.toString()

            }


        })


        holder.deleteCustomListItem.setOnClickListener(){
            listItems.removeAt(position)
            this.notifyItemRemoved(position)
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