package edu.uc.groupProject.topten.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.Service.ListService

/**
 * MainViewModel class. Does most of the heavy lifting for the database work.
 *
 * Responsible for connecting the Firebase database to the recyclerview, and actively updating the
 * list when a vote is made.
 */
class MainViewModel : ViewModel() {

    var listItems:MutableLiveData<ArrayList<ListItem>> = MutableLiveData()
    var listService: ListService = ListService()

    private var firestore : FirebaseFirestore

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        waitForListUpdate()
    }

    /**
     * This function fires every time a vote is added or changed on the generated list. Responsible
     * for creating DTO objects from the database list.
     */
    private fun waitForListUpdate() {
        firestore.collection("lists/Top Fifteen Movies/MyListItems").addSnapshotListener{
            snapshot, e ->
            //executed every time the vote changes

            //logs error, if any
            if(e != null){
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }
            if(snapshot != null){
                val allListItems = ArrayList<ListItem>()
                val documents = snapshot.documents // collection of list items on database
                documents.forEach{
                    //create new DTO
                    val listItem = ListItem(it.getString("title")!!, "Test", it.getLong("totalVotes")!!.toInt())
                    allListItems.add(listItem!!)
                }

                listItems.value = allListItems //changing live data collection
            }
        }
    }

    /**
     * Passes the fetchList function down to the service layer.
     * @return listService.fetchList(listItems)
     */
    fun fetchList(s: String) {
        listItems = listService.fetchList(listItems)
    }
}