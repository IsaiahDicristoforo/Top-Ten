package edu.uc.groupProject.topten.Service

import androidx.lifecycle.MutableLiveData
import edu.uc.groupProject.topten.DTO.ListItem

/**
 * The ListService Class. Handles fetching lists.
 *
 * Contains the fetchList() function, which fetches a specific list according to the list's name.
 */
class ListService {

    /**
     * Fetches a specific list according to the list's name.
     * @param listName The name of the list being fetched.
     * @return MutableLiveData<ArrayList<ListItem>>
     */
    fun  fetchList(listName: String): MutableLiveData<ArrayList<ListItem>> {
       return MutableLiveData<ArrayList<ListItem>>()
    }
}