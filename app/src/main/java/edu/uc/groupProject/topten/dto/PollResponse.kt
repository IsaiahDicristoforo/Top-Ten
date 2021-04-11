package edu.uc.groupProject.topten.dto

data class PollResponse(var question: String, var published_at: String = "", var url: String = "", var choices: ArrayList<String>) {}