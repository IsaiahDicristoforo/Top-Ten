package edu.uc.groupProject.topten.DTO

import java.util.*


data class TestDTO(
    var listName: String = "",
    var isArchived: Boolean,
    var isBettable: Boolean,
    var isVotable: Boolean,
    var dateOfRelease: Date
) {

}
