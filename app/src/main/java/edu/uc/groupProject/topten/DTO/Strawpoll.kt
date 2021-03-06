package edu.uc.groupProject.topten.DTO

import com.google.gson.annotations.SerializedName

data class Strawpoll(
    @SerializedName("id") var id: Int, @SerializedName("title") var title: String,
    var votes: ArrayList<Int>, var topTenListID: Int = 0
) {
}