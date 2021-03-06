package edu.uc.groupProject.topten.service

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.uc.groupProject.topten.dto.ListItem
import edu.uc.groupProject.topten.dto.PollsCallback
import edu.uc.groupProject.topten.dto.TopTenList
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Handles all of the main database logic for the top-ten app
 */
class FirestoreService {
    var listIncrementTime: Long = 0
    var listOfLists: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    var arrayOfLists: ArrayList<String> = ArrayList<String>()
    var list: MutableLiveData<ArrayList<ListItem>> = MutableLiveData<ArrayList<ListItem>>()
    var currentList = ""
    var listedItem = ""
    var pastListSelected = ""

    /**
     * fetchListNames function.
     * Responsible for fetching the names of the Firebase lists. Does not fetch the items inside
     * of each list, only the names of the lists.
     */
    fun fetchListNames(){
        val db = FirebaseFirestore.getInstance()
        var listItemCollection = db.collection("lists")
        var theCollection = db.collection("lists").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        listedItem = document.id
                        arrayOfLists.add(document.id)
                        listOfLists.value = arrayOfLists
                    }
                } else {
                    Log.d("ERROR", "Error getting documents: ", task.exception)
                }
            }
    }


    /**
     * Fetches the current list from firebase. The current list is the list where the isActive field in the collection of lists is set to true.
     * @param generateNewList Whether or not to generate a new active list by setting the isActive field on a list to true.
     */
    fun fetchList(generateNewList: Boolean): MutableLiveData<ArrayList<ListItem>> {
        val db = FirebaseFirestore.getInstance()

        var theCollection = db.collection("lists").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val myList: MutableList<String> = ArrayList()
                    for (document in task.result!!) {

                        myList.add(document.id)

                        if (document.getBoolean("active") == true && !generateNewList) { //Only one list will have the "active" field set to true in the database. The document
                            currentList = document.id
                            break
                        }

                    }

                    if (generateNewList) {
                        db.document("lists/$currentList").update("active", false)

                        if (myList.indexOf(currentList) == myList.size - 1) {
                            resetExpirationDateOnAllLists(listIncrementTime.toInt())
                            currentList = myList[0]
                        } else {
                            currentList = myList[myList.indexOf(currentList) + 1]
                        }

                        db.document("lists/$currentList").update("active", true)
                    }

                    db.collection("lists").document(currentList).collection("listItems")
                        .addSnapshotListener { snapshot, e ->
                            if (e != null) {

                                Log.w("Error", "Listen Failed", e)
                                return@addSnapshotListener

                            }

                            if (snapshot != null) {
                                var allListItems = ArrayList<ListItem>()
                                val documents = snapshot.documents
                                documents.forEach {
                                    val listItem: ListItem = ListItem(
                                        it.getLong("id")!!.toInt(),
                                        it.getString("title")!!,
                                        "Test",
                                        it.getLong("totalVotes")!!.toInt()
                                    )

                                    allListItems.add(listItem)
                                }
                                allListItems = sortListItemsByVoteDesc((allListItems))

                                list.value = allListItems

                            }
                        }

                } else {
                    Log.d("ERROR", "Error getting documents: ", task.exception)
                }

            }
        return list
    }

    /**
     * Fetches list items from the firebase - angled more toward the Past List Tab.
     * Notably has a string parameter rather than a boolean.
     */
    fun fetchPastList(listTitle:String): MutableLiveData<ArrayList<ListItem>> {
        val db = FirebaseFirestore.getInstance()

        db.collection("lists").document(listTitle).collection("listItems")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("Error", "Listen Failed", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    var allListItems = ArrayList<ListItem>()
                    val documents = snapshot.documents
                    documents.forEach {
                        val listItem: ListItem = ListItem(
                            it.getLong("id")!!.toInt(),
                            it.getString("title")!!,
                            "Test",
                            it.getLong("totalVotes")!!.toInt())

                        allListItems.add(listItem)
                    }
                    allListItems = sortListItemsByVoteDesc((allListItems))

                    list.value = allListItems

                }
            }
        return list
    }


    //TEST
    fun fetchDocument(): MutableLiveData<ArrayList<ListItem>> {
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("lists").document("Movies").collection("allMovies")
            .document("Forrest Gump")

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val allListItems = ArrayList<ListItem>()

                    var listItem: ListItem = ListItem(
                        document.getLong("id")!!.toInt(), document.getString(
                            "title"
                        ).toString(),
                        document.getString("description").toString(),
                        document.getString("totalVotes")!!.toInt()
                    )

                    allListItems.add(listItem)


                    list.value = allListItems
                }
            }
            .addOnFailureListener { exception ->
                Log.d("error", "get failed with ", exception)
            }

        return list
    }


    fun sortListItemsByVoteDesc(listItemsToSort: ArrayList<ListItem>): ArrayList<ListItem> {
        return ArrayList(listItemsToSort.sortedWith(compareByDescending { it.totalVotes.toInt() }))
    }


    /**
     * Updates user vote in firebase
     */
    fun addListItemVote(listItemToIncrement: String) {
        val db = FirebaseFirestore.getInstance()
        var listItemDocument =
            db.document("lists/" + currentList + "/listItems/" + listItemToIncrement)
        var totalVotes: Number
        listItemDocument.get().addOnSuccessListener {
            totalVotes = it.getLong("totalVotes")!!
            listItemDocument.update("totalVotes", (totalVotes.toLong() + 1))
        }
    }

    /**
     * Writes the list to the Firebase Database. Currently writes the mocked data below to the database.
     */
    fun writeListToDatabase(listToAdd: TopTenList) {
        val db = FirebaseFirestore.getInstance()

        var listsReference = (db.collection("lists").document(listToAdd.title))

        listsReference.set(listToAdd).addOnSuccessListener {
            Log.d("Firebase", "document saved")
        }.addOnFailureListener {
            Log.d("Firebase", "Save Failed")
        }


        var listItemCollectionReference = listsReference.collection("listItems")

        var arrayOfListItemsToAdd: Array<ListItem> = listToAdd.listItems.toTypedArray()

        for (item in arrayOfListItemsToAdd) {
            listItemCollectionReference.document(item.title).set(item)
        }


    }

    /**
     * Provide the user name from the firebase database\
     */
    fun getUserName(): String {
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("users").document("testuser")
        var userName = ""

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

    /**
     * Provide the user points from the firebase database
     */
    fun getUserPoints(): String {
        val db = FirebaseFirestore.getInstance()
        val userUID = FirebaseAuth.getInstance().uid

        val docRef = db.collection("users").document(userUID.toString())
        var userPoints = ""

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

    fun resetExpirationDateOnAllLists(timeIncrementInMilli: Int) {
        val db = FirebaseFirestore.getInstance()

        var calendar: Calendar = Calendar.getInstance()

        var allLists = db.collection("lists").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val myList: MutableList<String> = ArrayList()
                for (document in task.result!!) {
                    calendar.add(Calendar.MILLISECOND, timeIncrementInMilli)

                    db.document("lists/" + document.id).update("expireDate", calendar.time)
                }
            }
        }
    }


    fun getPollQuestions(uid: String, pollsCallback: PollsCallback) {
        val db = FirebaseFirestore.getInstance()

        var path:String = "polls_api/" + uid + "/questionIds"

            db.collection(path).get().addOnCompleteListener() { task ->
                if(task.isSuccessful)
                {
                    val questionIds = ArrayList<Int>()

                    for(document in task.result!!)
                    {
                        val id = document.id.toInt()
                        questionIds.add(id)
                    }
                        pollsCallback.onCallback(questionIds)
                }
            }
    }

    fun setUserPoll(uid: String, questionId: Int) {
        val db = FirebaseFirestore.getInstance()
        var path:String = "polls_api"


        val items = HashMap<String, Any>()
        items.put(questionId.toString(), questionId)

        db.collection(path).document(uid).collection("questionIds").document(questionId.toString()).set(items)
    }

    /**
     * Gets the UID of the user who is currently logged in.
     */
    fun getUID(): String {
        val userUID = FirebaseAuth.getInstance().uid
        return userUID.toString()
    }

}