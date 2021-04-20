package edu.uc.groupProject.topten.dto

import com.google.gson.annotations.SerializedName

/**
 * Data class that represents a strawpoll
 *
 * REQUIRED POST FIELDS
 * @param title
 * @param options
 */
data class Strawpoll(var id: Int? = 0, var title: String?,
                     var options: ArrayList<String>?, var votes: ArrayList<Int>? = null,
                     var multi: Boolean? = true, var dupcheck: String? = "normal", var captcha: Boolean? = true) {
}