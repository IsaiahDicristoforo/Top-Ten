package edu.uc.groupProject.topten.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import edu.uc.groupProject.topten.dto.ListItem
import edu.uc.groupProject.topten.dto.TestDTO
import java.util.*
import kotlin.collections.ArrayList

class FirestoreService {
    var list: MutableLiveData<ArrayList<ListItem>> = MutableLiveData<ArrayList<ListItem>>()

    /**
     * gets the list items from firebase
     */
    fun fetchList(): MutableLiveData<ArrayList<ListItem>> {
        val db = FirebaseFirestore.getInstance()

        db.collection("lists").document("Movies").collection("allMovies")
            .addSnapshotListener{
                snapshot, e ->
                if(e != null){

                    Log.w("Error", "Listen Failed", e)
                    return@addSnapshotListener

                }
                if(snapshot != null){
                   // if(snapshot.getDocumentChanges().size > 1 ){
                        val allListItems = ArrayList<ListItem>()
                        val documents = snapshot.documents

                        documents.forEach{
                            val listItem :ListItem = ListItem(it.getString("title")!!, "Test", it.get("totalVotes").toString()!!)
                            allListItems.add(listItem!!)
                        }

                        list.value = allListItems;

                    }
            //   }
            }


        return list
    }

    //TEST
    fun fetchDocument(): MutableLiveData<ArrayList<ListItem>> {
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("lists").document("Movies").collection("allMovies").document("Forrest Gump")

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val allListItems = ArrayList<ListItem>()

                    var listItem: ListItem = ListItem(document.getString("title").toString(),
                        document.getString("description").toString(),
                        document.getString("totalVotes").toString())

                    allListItems.add(listItem)
                    list.value = allListItems
                }
            }
            .addOnFailureListener { exception ->
                Log.d("error", "get failed with ", exception)
            }

        return list
    }

    /**
     * Updates user vote in firebase
     */
    fun addListItemVote(listItemToIncrement: String) {
        val db = FirebaseFirestore.getInstance()

        var listItemDocument = db.document("lists/Movies/allMovies/" + listItemToIncrement)
        var totalVotes: Number
        listItemDocument.get().addOnSuccessListener {
            totalVotes = it.getLong("totalVotes")!!
            listItemDocument.update("totalVotes", (totalVotes.toLong() + 1))
        }
    }

    /**
     * Writes the list to the Firebase Database. Currently writes the mocked data below to the database.
     */
    fun writeListToDatabase(){
        val db = FirebaseFirestore.getInstance()

        var testList = TestDTO("Top Fifteen Movies", false, true, true, Date())
        var  listsReference = (db.collection("lists").document(testList.listName))

        listsReference.set(testList).addOnSuccessListener {
            Log.d("Firebase", "document saved");
        }.addOnFailureListener{
            Log.d("Firebase", "Save Failed");
        }

        var listItemCollectionReference = listsReference.collection("MyListItems")
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
    fun getUserPoints(): String{
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("users").document("testuser")
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
}