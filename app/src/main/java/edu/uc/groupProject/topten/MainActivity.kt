package edu.uc.groupProject.topten

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.uc.groupProject.topten.service.FirestoreService
import edu.uc.groupProject.topten.ui.main.MainFragment
import edu.uc.groupProject.topten.ui.main.MainViewModel
import edu.uc.groupProject.topten.ui.main.PastListsFragment
import edu.uc.groupProject.topten.ui.main.PrivateListFragment



/**
 * MainActivity class.
 */
class MainActivity : AppCompatActivity() {
    var mvm: MainViewModel = MainViewModel()
    private lateinit var bottomMenu: BottomNavigationView

    /**
     * onCreate function
     * @param savedInstanceState the saved instance state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commitNow()
        }

        /* val textView = findViewById<TextView>(R.id.textview)
        textView.setText("")

        mvm.fetchFirestoreList()

        mvm.list.observeForever {
            it.forEach {
                textView.append(it.title)
                textView.append("\n")
            }
        }

        */


        this.supportActionBar?.hide()
        supportActionBar?.setDisplayShowTitleEnabled(false)

        bottomMenu = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomMenu.selectedItemId = R.id.currentList
        bottomMenu.setOnNavigationItemSelectedListener { item: MenuItem ->

            when (item.itemId) {
                R.id.currentList -> {
                    changeFragment(MainFragment())
                    true
                }

                R.id.pastList -> {
                    changeFragment(PastListsFragment.newInstance())
                    true
                }

                R.id.privateList -> {
                    changeFragment(PrivateListFragment.newInstance())
                    true
                }

                else -> false
            }
        }

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val firestore = FirestoreService()
            val userName = findViewById<TextView>(R.id.txt_username)

            if (userName.text != "") {
                val test = firestore.getUserPoints()
                Log.d("TAG", test)

            } else {
                createSignInIntent()
            }
        }


    }

    private fun changeFragment(newFragment: Fragment) {
        supportFragmentManager?.beginTransaction()?.replace(R.id.container, newFragment)
            ?.commit()
    }

    private fun createSignInIntent() {

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo((R.drawable.top10logo))
                .setTheme((R.style.Theme_TopTen))
                .build(),
            RC_SIGN_IN
        )
    }

    companion object {

        private const val RC_SIGN_IN = 123
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val userUID = FirebaseAuth.getInstance().uid
            val db = FirebaseFirestore.getInstance()
            val name = FirebaseAuth.getInstance().currentUser.email
            var newUser: HashMap<String, Any>
            var points = 0
            db.collection("users")
                .document(userUID.toString())
                .get()
                .addOnSuccessListener { doc ->
                    if (!doc.exists()) {
                        points = 300
                        newUser = hashMapOf(
                            "username" to name,
                            "points" to points.toString()
                        )
                        if (userUID != null) {
                            db.collection("users").document(userUID).set(newUser)
                                .addOnSuccessListener {
                                    Log.d(
                                        "msg",
                                        "DocumentSnapshot successfully written!"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w(
                                        "err",
                                        "Error writing document",
                                        e
                                    )
                                }
                        }
                    }
                }
            val userName = findViewById<TextView>(R.id.txt_username)
            val userPoints = findViewById<TextView>(R.id.txt_points)
            userName.text = name.substringBefore("@").take(8)
            userPoints.text = "300"
            }
        }
}


