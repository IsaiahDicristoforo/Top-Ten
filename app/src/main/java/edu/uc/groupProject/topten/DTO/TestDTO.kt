package edu.uc.groupProject.topten.DTO

import java.util.*

/**
 * The TestDTO
 *
 * @param listName
 * @param description
 * @param isArchived
 * @param isBettable
 * @param isVotable
 * @param dateOfRelease
 */
data class TestDTO(var listName:String = "", var isArchived:Boolean, var isBettable:Boolean, var isVotable:Boolean, var dateOfRelease: Date) {

}
