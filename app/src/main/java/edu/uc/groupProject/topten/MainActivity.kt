package edu.uc.groupProject.topten

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.groupProject.topten.DTO.TestDTO
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.Service.StrawpollService
import edu.uc.groupProject.topten.ui.main.MainFragment
import edu.uc.groupProject.topten.ui.main.PastListsFragment
import edu.uc.groupProject.topten.ui.main.PrivateListFragment
import org.w3c.dom.Text

import java.util.*

/**
 * MainActivity class.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var bottomMenu:BottomNavigationView
    //private val btn_click_me = findViewById<ImageButton>(R.id.btn_Vote)
    /**
     * onCreate function
     * @param savedInstanceState the saved instance state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createSignInIntent()
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment())
                    .commitNow()
        }

        this.supportActionBar?.hide();
        supportActionBar?.setDisplayShowTitleEnabled(false)

        bottomMenu = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomMenu.selectedItemId = R.id.currentList
        bottomMenu.setOnNavigationItemSelectedListener { item: MenuItem ->

            when(item.itemId){
                R.id.currentList->{
                    changeFragment(MainFragment())
                    true
                }

                R.id.pastList->{
                    changeFragment(PastListsFragment.newInstance())
                    true
                }

                R.id.privateList->{
                    changeFragment(PrivateListFragment.newInstance())
                    true
                }

                else -> false
            }
        }

    }

    private fun changeFragment(newFragment:Fragment){
        supportFragmentManager?.beginTransaction()?.replace(R.id.container,newFragment)?.commit()
    }
    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }
    companion object {

        private const val RC_SIGN_IN = 123
    }
}