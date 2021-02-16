package edu.uc.groupProject.topten

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.groupProject.topten.DTO.TestDTO
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.ui.main.MainFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }


        writeListToDatabase()





    }

    fun writeListToDatabase(){

        lateinit var firestore : FirebaseFirestore
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

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

    fun createSampleListItems():Array<ListItem>{

        var sampleListItems = arrayOf(
                ListItem("The Dark Knight", "A movie about Batman", 100),
                ListItem("The Return of the King", "A movie about a ring and some eagles", 150),
                ListItem("The Empire Strikes Back", "A movie about some light wands and parent issues", 200)

        )
        return sampleListItems
    }




}