package edu.uc.groupProject.topten.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uc.groupProject.topten.dto.ListItem
import edu.uc.groupProject.topten.service.FirestoreService

class PastListsViewModel : ViewModel() {
    var list : MutableLiveData<ArrayList<ListItem>> = MutableLiveData<ArrayList<ListItem>>()
    var firestoreService : FirestoreService =  FirestoreService()

    /**
     * loadNextList function.
     * Connects the PastListFragment with firestoreService via fetchPastList.
     * @param listTitle the title of a given list.
     * @return list
     */
    fun loadNextList(listTitle: String){
        list = firestoreService.fetchPastList(listTitle)
    }
}