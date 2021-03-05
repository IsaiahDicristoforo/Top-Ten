package edu.uc.groupProject.topten

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.uc.groupProject.topten.ui.main.MainFragment

/*  By Benjamin Gomori Code Review
    The ViewModels should be the centralized location for feeding activities and fragments with live and firebase data.
    Thus I removed all of the functions that write to or call the database
*/

/**
 * MainActivity class. Contains mocked data to test functionality.
 */
class MainActivity : AppCompatActivity() {

    /**
     * onCreate function
     * @param savedInstanceState the saved instance state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }

        this.supportActionBar?.hide();
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}