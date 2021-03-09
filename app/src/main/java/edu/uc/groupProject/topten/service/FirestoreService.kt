package edu.uc.groupProject.topten.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import edu.uc.groupProject.topten.DTO.ListItem


class FirestoreService {
    private var list: MutableLiveData<ArrayList<ListItem>> = MutableLiveData<ArrayList<ListItem>>()
    private var allItems: ArrayList<ListItem> = ArrayList<ListItem>()

    private fun getList(){
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("lists")
                    .document("Movies")
                    .collection("allMovies")
                    .document("Fight Club")

        docRef.get()
        .addOnSuccessListener { document ->
            if (document != null) {
                Log.d("Eureka!", "Got document")

                var listItem: ListItem = ListItem(
                    document.getString("title").toString(),
                    document.getString("description").toString(),
                    document.getLong("totalVotes")!!.toInt()
                )

                allItems.add(listItem)
                list.value = allItems
            } else {
                Log.d("meh :(", "No such document")
            }
        }
        .addOnFailureListener { exception ->
            Log.d("Error", "get failed with ", exception)
        }
    }

    fun fetchList(): MutableLiveData<ArrayList<ListItem>> {
        getList()
        return list
    }
}