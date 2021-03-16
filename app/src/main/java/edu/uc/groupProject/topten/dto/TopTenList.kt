package edu.uc.groupProject.topten.dto

import java.time.LocalDateTime
import java.util.*

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
    var listItems:List<ListItem>, var expireDate: LocalDateTime
) {

}