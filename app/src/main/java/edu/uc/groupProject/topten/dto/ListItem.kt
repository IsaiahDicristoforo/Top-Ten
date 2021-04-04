package edu.uc.groupProject.topten.dto

/**
 * The ListItem dto.
 *
 * This dto holds the name, description, and number of votes of a specific list item.
 * @param title The title of the listItem
 * @param description The description of the listItem
 * @param totalVotes The total number of votes counted towards this listItem. Handled by Firebase.
 */
data class ListItem(
    var id: Int,
    var title: String,
    var description: String = "",
    var totalVotes: Int
) {

    /**
     * returns a string representing an object
     * @return title
     */
    override fun toString(): String {
        return title
    }

}
