package edu.uc.groupProject.topten.DTO

import com.google.type.DateTime
import java.util.*


data class TopTenList(var id:Int, var title:String, var description:String="", var isActive:Boolean,
                      var category:String /* Possible category dto? */, var publishDate:Date,
                      var listItems:List<ListItem>, var expireDate: DateTime) {

}