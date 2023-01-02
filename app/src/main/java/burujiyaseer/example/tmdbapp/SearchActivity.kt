package burujiyaseer.example.tmdbapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import burujiyaseer.example.tmdbapp.databinding.ActivitySearchBinding
import burujiyaseer.example.tmdbapp.models.Movie

private const val TAG = "SearchActivity"
class SearchActivity : BaseActivity(),
    RecyclerItemClickListener.OnRecyclerClickListener,
    GetRawApiData.OnDataAvailable {
    private var searchView: SearchView? = null
//    val toolbar: Toolbar

    private val movieAdapter = MovieAdapter(ArrayList())
    private lateinit var binding:ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, ".onCreate: starts")
        super.onCreate(savedInstanceState)


        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.bottomNavigation.setOnItemSelectedListener(setCurrentActivityListener)

        binding.includeSearchResults.rvMoviesList.layoutManager = GridLayoutManager(this,2)
        binding.includeSearchResults.rvMoviesList.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                binding.includeSearchResults.rvMoviesList,
                this
            )
        )


//        toolbar.setNavigationOnClickListener{
//            finish()
//        }
        Log.d(TAG, ".onCreate: ends")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, ".onCreateOptionsMenu: starts")
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        searchView?.setSearchableInfo(searchableInfo)
//        Log.d(TAG, ".onCreateOptionsMenu: $componentName")
//        Log.d(TAG, ".onCreateOptionsMenu: hint is ${searchView?.queryHint}")
//        Log.d(TAG, ".onCreateOptionsMenu: $searchableInfo")

        searchView?.isIconified = false
        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, ".onQueryTextSubmit: called")

//                val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
//                sharedPref.edit().putString(MOVIE_QUERY, query).apply()
                searchView?.clearFocus()
                if ((query != null) && query.isNotEmpty()) {
                    val getRawApiData = GetRawApiData(this@SearchActivity)
                    getRawApiData.getSearchMovieData(query)
                        binding.includeSearchResults.tvSearch.text = resources.getString(R.string.search_results)
                        binding.includeSearchResults.rvMoviesList.adapter = movieAdapter
                    }
                return true
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

//        searchView?.setOnCloseListener {
//            finish()
//            false
//        }

        Log.d(TAG, ".onCreateOptionsMenu: returning")
        return true
    }

    override fun onStop() {
        Log.d(TAG, "onStop called")
        super.onStop()
    }

    override fun onSearchDataAvailable(data: List<Movie>) {
        Log.d(TAG,"onDataAvailable starts with $data")
        movieAdapter.loadNewData(data)
        Log.d(TAG,"onDataAvailable ends")    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, ".onItemClick: starts")
//        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_TRANSFER, movieAdapter.getPhoto(position))
        startActivity(intent)
    }

}