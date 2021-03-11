package edu.uc.groupProject.topten.DTO

import com.google.gson.annotations.SerializedName

/**
 * Data class that represents a strawpoll
 *
 * @param id
 * @param title
 * @param votes
 * @param topTenListID
 */
data class Strawpoll(var id: Int, var title: String,
                     var votes: ArrayList<Int>, var topTenListID: Int = 0) {
}