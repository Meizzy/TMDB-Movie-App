package burujiyaseer.example.tmdbapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import burujiyaseer.example.tmdbapp.databinding.ActivityMainBinding


private const val TAG = "MainActivity"

class MainActivity : BaseActivity() {


    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate starts")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        binding.bottomNavigation.setupWithNavController(navController)
        setSupportActionBar(binding.toolbar)



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


//    private fun testUpdate()    {
//        val values = ContentValues().apply {
//            put(MoviesContract.Columns.MOVIE_ID, "Movie update 1")
//            put(MoviesContract.Columns.TITLE, "Update Movie title")
//            put(MoviesContract.Columns.RATINGS, "8.9")
//            put(MoviesContract.Columns.OVERVIEW, "New Movie using Content Provider update")
//        }
//        val movieUri = MoviesContract.buildUriFromId(2)
//        val rowsAffected = contentResolver.update(movieUri, values,null,null)
//        Log.d(TAG, "Number rows affected is $rowsAffected")
//    }
    override fun onResume() {
    Log.d(TAG, "onResume starts")
        super.onResume()
    }
}