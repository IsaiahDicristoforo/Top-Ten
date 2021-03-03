package edu.uc.groupProject.topten.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.DTO.Strawpoll
import edu.uc.groupProject.topten.Service.ListService
import edu.uc.groupProject.topten.Service.StrawpollService

/**
 * MainViewModel class. Does most of the heavy lifting for the database work.
 *
 * Responsible for connecting the Firebase database to the recyclerview, and actively updating the
 * list when a vote is made.
 */
class MainViewModel : ViewModel() {
    var list:MutableLiveData<ArrayList<ListItem>> = MutableLiveData<ArrayList<ListItem>>()
    var listService: ListService = ListService()

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
  
    /**
     * This function fires every time a vote is added or changed on the generated list. Responsible
     * for creating DTO objects from the database list.
     */
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    init{
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        waitForListUpdate()
    }

    private fun waitForListUpdate() {
        firestore.collection("lists/Top Fifteen Movies/MyListItems").addSnapshotListener{
                snapshot, e ->
            if(e != null){
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }
            if(snapshot != null){
                val allListItems = ArrayList<ListItem>()
                val documents = snapshot.documents
                documents.forEach{

                    //val listItem = it.toObject(ListItem::class.java)

                    val listItem :ListItem = ListItem(it.getString("title")!!, "Test", it.getLong("totalVotes")!!.toInt())
                    allListItems.add(listItem!!)
                }

                list.value = allListItems
            }
        }
    }
}