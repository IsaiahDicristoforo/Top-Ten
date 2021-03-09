package edu.uc.groupProject.topten.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.DTO.Strawpoll
import edu.uc.groupProject.topten.service.FirestoreService
import edu.uc.groupProject.topten.service.ListService
import edu.uc.groupProject.topten.service.StrawpollService

/**
 * MainViewModel class. Does most of the heavy lifting for the database work.
 *
 * Responsible for connecting the Firebase database to the recyclerview, and actively updating the
 * list when a vote is made.
 */
class MainViewModel : ViewModel() {
    var list:MutableLiveData<ArrayList<ListItem>> = MutableLiveData<ArrayList<ListItem>>()
    var listService: ListService = ListService()
    var firestoreService: FirestoreService = FirestoreService()

    /**
     * Passes the fetchList function down to the service layer.
     * @return listService.fetchList(listItems)
     */
    fun fetchList(listName: String) {
        list = listService.fetchList(listName)
    }

    fun fetchStrawpoll(id: Int): MutableLiveData<Strawpoll>? {
        val service = StrawpollService()
        return service.getStrawpoll(id)
    }
  
    fun fetchFirebaseList(){
        list = firestoreService.fetchList()
    }
}