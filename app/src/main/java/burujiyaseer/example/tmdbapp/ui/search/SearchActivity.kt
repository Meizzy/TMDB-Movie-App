package burujiyaseer.example.tmdbapp.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import burujiyaseer.example.tmdbapp.R
import burujiyaseer.example.tmdbapp.data.network.Resource
import burujiyaseer.example.tmdbapp.databinding.ActivitySearchBinding
import burujiyaseer.example.tmdbapp.ui.MovieAdapter
import burujiyaseer.example.tmdbapp.ui.RecyclerItemClickListener
import burujiyaseer.example.tmdbapp.ui.base.MOVIE_TRANSFER
import burujiyaseer.example.tmdbapp.ui.details.MovieDetailsActivity
import burujiyaseer.example.tmdbapp.ui.hide
import burujiyaseer.example.tmdbapp.ui.show

private const val TAG = "SearchActivity"

class SearchActivity : AppCompatActivity(), RecyclerItemClickListener.OnRecyclerClickListener {
    private var searchView: SearchView? = null
//    val toolbar: Toolbar

    private val movieAdapter = MovieAdapter(ArrayList())
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, ".onCreate: starts")
        super.onCreate(savedInstanceState)


        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.bottomNavigation.setOnItemSelectedListener(setCurrentActivityListener)

        binding.includeSearchResults.rvMoviesList.apply {
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
            addOnItemTouchListener(
                RecyclerItemClickListener(
                    this@SearchActivity,
                    binding.includeSearchResults.rvMoviesList,
                    this@SearchActivity
                )
            )
            adapter = movieAdapter
        }
        viewModel.moviesListLiveData.observe(this) {
            when (it) {
                is Resource.Failure -> {
                    binding.includeSearchResults.progressBar.hide()
                    movieAdapter.loadNewData(ArrayList())
                }
                Resource.Loading -> binding.includeSearchResults.progressBar.show()
                is Resource.Success -> {
                    binding.includeSearchResults.progressBar.hide()
                    movieAdapter.loadNewData(it.value)
                }
            }
        }

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
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, ".onQueryTextSubmit: called")

//                val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
//                sharedPref.edit().putString(MOVIE_QUERY, query).apply()
                searchView?.clearFocus()
                if ((query != null) && query.isNotEmpty()) {
                    viewModel.getQueriedMoviesList(query)
                    binding.includeSearchResults.tvSearch.text =
                        resources.getString(R.string.search_results)
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


    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, ".onItemClick: starts")
//        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_TRANSFER, movieAdapter.getPhoto(position))
        startActivity(intent)
    }

}