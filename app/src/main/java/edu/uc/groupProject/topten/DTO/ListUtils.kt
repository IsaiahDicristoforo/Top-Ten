package edu.uc.groupProject.topten.DTO

class ListUtils {

    fun sortListItems(listToSort: ArrayList<ListItem>):ArrayList<ListItem>{
        listToSort.sortByDescending { it.totalVotes }

        return listToSort
    }
}