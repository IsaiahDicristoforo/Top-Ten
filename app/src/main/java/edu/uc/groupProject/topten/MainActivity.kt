package edu.uc.groupProject.topten

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.groupProject.topten.DTO.TestDTO
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

        var testList = TestDTO("Top Ten Movies", false, true, true, Date())

        firestore.collection("lists").document("Top Ten Movies").set(testList).addOnSuccessListener {
            Log.d("Firebase", "document saved");
        }.addOnFailureListener{
            Log.d("Firebase", "Save Failed");
        }
    }




}