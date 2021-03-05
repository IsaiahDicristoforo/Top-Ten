package edu.uc.groupProject.topten.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.groupProject.topten.DTO.TestDTO
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.DTO.Strawpoll
import edu.uc.groupProject.topten.Service.ListService
import edu.uc.groupProject.topten.Service.StrawpollService
import java.util.*
import kotlin.collections.ArrayList

/**
 * MainViewModel class. Does most of the heavy lifting for the database work.
 *
 * Responsible for connecting the Firebase database to the recyclerview, and actively updating the
 * list when a vote is made.
 */
class MainViewModel : ViewModel() {
    var list:MutableLiveData<ArrayList<ListItem>> = MutableLiveData<ArrayList<ListItem>>()
    var listService: ListService = ListService()
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    init{
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        writeListToDatabase()//Moved from MainActivity -Benjamin Gomori, Code Review
        waitForListUpdate()
    }

    /**
     * Writes the list to the Firebase Database. Currently writes the mocked data below to the
     * database.
     */
    fun writeListToDatabase(){
        var testList = TestDTO("Top Fifteen Movies", false, true, true, Date())
        var  listsReference = (firestore.collection("lists").document(testList.listName))

        listsReference.set(testList).addOnSuccessListener {
            Log.d("Firebase", "document saved");
        }.addOnFailureListener{
            Log.d("Firebase", "Save Failed");
        }

        var listItemCollectionReference = listsReference.collection("MyListItems")
        var arrayOfListItemsToAdd : Array<ListItem> = createSampleListItems();

        for(item in arrayOfListItemsToAdd){
            listItemCollectionReference.document(item.title).set(item);
        }
    }

    /**
     * Creates the mocked data list used by the program for testing purposes
     * @return sampleListItems
     */
    fun createSampleListItems():Array<ListItem>{

        var sampleListItems = arrayOf(
            ListItem("The Dark Knight", "A movie about Batman", 100),
            ListItem("The Return of the King", "A movie about a ring and some eagles", 150),
            ListItem("The Empire Strikes Back", "A movie about some light wands and parent issues", 200),
            ListItem("The Godfather", "n/a", 24),
            ListItem("The Avengers", "They all team up to fight bad guys", 231),
            ListItem("Inception", "", 12),
            ListItem("E.T", "", 124),
            ListItem("The Matrix", "", 42)
        )
        return sampleListItems
    }

    fun getUserName(): String {
        val docRef = firestore.collection("users").document("testuser")
        var userName: String = ""

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("document", "DocumentSnapshot data: ${document.data}")
                    userName = document.getString("username").toString()
                } else {
                    Log.d("no document", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("error", "get failed with ", exception)
            }

        return userName
    }


    fun getUserPoints(): String{
        val docRef = firestore.collection("users").document("testuser")
        var userPoints: String = ""

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("document", "DocumentSnapshot data: ${document.data}")
                    userPoints = document.getString("points").toString()
                } else {
                    Log.d("no document", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("error", "get failed with ", exception)
            }

        return userPoints
    }

    private fun waitForListUpdate() {
        /*  By Benjamin Gomori Code Review
            Every time a user votes - data is sent to firebase via "holder.voteButton.setOnClickListener()" in the CurrentListAdapter.
            Thus this Listener gets called
         */
        firestore.collection("lists/Top Fifteen Movies/MyListItems").addSnapshotListener{
                snapshot, e ->
            if(e != null){
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }

            if(snapshot != null){
                /*  By Benjamin Gomori Code Review
                   [1] Re-rendering the list of Items, into the recycler view, when a user votes, is a waist of resources
                   [2] Since on each single vote this will run, we should distinguish between a user vote - only one change, vs new list - many changes.
                   If you do re-render the list, then added should be added to disable the voting button user has used.
                */
                if(snapshot.getDocumentChanges().size > 1 ){

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

    /**
     * Passes the fetchList function down to the service layer.
     */
    fun fetchList(listName: String) {
        list = listService.fetchList(listName)
    }

    fun fetchStrawpoll(id: Int): MutableLiveData<Strawpoll>? {
        val service = StrawpollService()
        return service.getStrawpoll(id)
    }

    /**
     * Updates user vote in firebase
     */
    fun addListItemVote(listItemToIncrement: String) {
        var listItemDocument = firestore.document("lists/Top Fifteen Movies/MyListItems/" + listItemToIncrement)
        var totalVotes: Number
        listItemDocument.get().addOnSuccessListener {
            totalVotes = it.getLong("totalVotes")!!
            listItemDocument.update("totalVotes", (totalVotes.toLong() + 1))
        }
    }
}