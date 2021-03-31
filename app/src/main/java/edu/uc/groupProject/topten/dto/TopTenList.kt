package edu.uc.groupProject.topten.dto

import com.google.firebase.firestore.Exclude
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

/**
 * Data class that represents a list
 *
 * @param id
 * @param description
 * @param isActive
 * @param category
 * @param publishDate
 * @param listItems
 */
data class TopTenList(
    var id:Int, var title:String, var description:String="", var isActive:Boolean,
    var category:String /* Possible category dto? */, var publishDate:Date,
     var expireDate: Date

){
    private var mlistItems:List<ListItem> = ArrayList<ListItem>()


    var listItems:List<ListItem>
        @Exclude get()  {return mlistItems} set(value){ mlistItems = value}

}



