package edu.uc.groupProject.topten.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.Service.ListService

class MainViewModel : ViewModel() {

    public var listItems:MutableLiveData<ArrayList<ListItem>> = MutableLiveData<ArrayList<ListItem>>()
    var listService: ListService = ListService()

    private lateinit var firestore : FirebaseFirestore

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        waitForListUpdate()
    }

    //The code inside of this function fires every time a vote changes
    private fun waitForListUpdate() {
        firestore.collection("lists/Top Fifteen Movies/MyListItems").addSnapshotListener{
            snapshot, e ->
            //executed every time the vote changes
            if(e != null){
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }
            if(snapshot != null){
                val allListItems = ArrayList<ListItem>()
                val documents = snapshot.documents // collection of list items on database
                documents.forEach{

                    //val listItem = it.toObject(ListItem::class.java)

                    //create new DTO
                    val listItem :ListItem = ListItem(it.getString("title")!!, "Test", it.getLong("totalVotes")!!.toInt())
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