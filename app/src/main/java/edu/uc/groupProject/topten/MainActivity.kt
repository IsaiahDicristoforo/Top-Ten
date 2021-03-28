package edu.uc.groupProject.topten

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.okhttp.internal.Internal.instance
import edu.uc.groupProject.topten.ui.main.MainFragment
import edu.uc.groupProject.topten.ui.main.MainViewModel
import edu.uc.groupProject.topten.ui.main.PastListsFragment
import edu.uc.groupProject.topten.ui.main.PrivateListFragment

/**
 * MainActivity class.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var bottomMenu:BottomNavigationView

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
}