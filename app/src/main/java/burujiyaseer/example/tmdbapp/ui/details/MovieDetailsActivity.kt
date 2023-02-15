package burujiyaseer.example.tmdbapp.ui.details

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import burujiyaseer.example.tmdbapp.R
import burujiyaseer.example.tmdbapp.data.databases.MoviesContract
import burujiyaseer.example.tmdbapp.data.network.Movie
import burujiyaseer.example.tmdbapp.data.network.Resource
import burujiyaseer.example.tmdbapp.databinding.ActivityMovieDetailsBinding
import burujiyaseer.example.tmdbapp.ui.MovieAdapter
import burujiyaseer.example.tmdbapp.ui.RecyclerItemClickListener
import burujiyaseer.example.tmdbapp.ui.base.MOVIE_TRANSFER
import burujiyaseer.example.tmdbapp.ui.snackbar
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val TAG = "MovieDetailsAct"

class MovieDetailsActivity : AppCompatActivity(),
    RecyclerItemClickListener.OnRecyclerClickListener {


    //    private val listener get() = _listener!!
    private val similarMovieAdapter = MovieAdapter(ArrayList())
    private lateinit var binding: ActivityMovieDetailsBinding
    private val imageBase = "https://image.tmdb.org/t/p/w500/"
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.d(TAG, ".onCreate starts")
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        activateToolbar(true)
        binding.includeDetails.backButton.setOnClickListener {
            finish()
        }
        viewModel.moviesListLiveData.observe(this) {
            when (it) {
                is Resource.Failure -> {
                    similarMovieAdapter.loadNewData(ArrayList())
                }
                Resource.Loading -> this.snackbar("Loading")
                is Resource.Success -> {
                    similarMovieAdapter.loadNewData(it.value)
                }
            }
        }

        binding.includeDetails.rvSimilarMoviesList.apply {
            layoutManager =
                LinearLayoutManager(
                    this@MovieDetailsActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

            addOnItemTouchListener(
                RecyclerItemClickListener(
                    this@MovieDetailsActivity,
                    binding.includeDetails.rvSimilarMoviesList,
                    this@MovieDetailsActivity
                )
            )
            adapter = similarMovieAdapter
        }

        val movie = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(MOVIE_TRANSFER, Movie::class.java) as Movie
        } else {
            intent.getParcelableExtra(MOVIE_TRANSFER)

        }

        if (movie?.id != null) {
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                if (isInDB(movie)) {
                    binding.includeDetails.favoriteBtn.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_red_favorite_24, null)
                }
            }

            setMovies(movie)

            viewModel.getSimilarMoviesList(movie.id)

            binding.includeDetails.favoriteBtn.setOnClickListener {
                if (!isInDB(movie)) {
                    binding.includeDetails.favoriteBtn.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_red_favorite_24, null)
                    lifecycle.coroutineScope.launch(Dispatchers.IO) {
                        like(movie)
                    }
                    Log.d(TAG, "Liked")
                } else {
                    binding.includeDetails.favoriteBtn.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_baseline_favorite_border_24,
                        null
                    )
                    lifecycle.coroutineScope.launch(Dispatchers.IO) {
                        unlike(movie)
                    }
                    Log.d(TAG, "Unliked")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else
            super.onOptionsItemSelected(item)
    }

    private fun setMovies(movie: Movie) {
        binding.includeDetails.movieTitle.text =
            resources.getString(R.string.movie_title_text, movie.title)
        binding.includeDetails.movieReleaseDate.text =
            resources.getString(R.string.movie_release_date_text, movie.release, movie.ratings)
        binding.includeDetails.movieDetails.text = movie.overview
        Glide.with(binding.includeDetails.moviePoster.context).load(imageBase + movie.poster)
            .into(binding.includeDetails.moviePoster)
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, ".onItemClick: starts")
//        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_TRANSFER, similarMovieAdapter.getPhoto(position))
        startActivity(intent)
    }

    private fun like(movie: Movie) {
        val values = ContentValues().apply {
            put(MoviesContract.Columns.MOVIE_ID, movie.id)
            put(MoviesContract.Columns.TITLE, movie.title)
            put(MoviesContract.Columns.RATINGS, movie.ratings)
            put(MoviesContract.Columns.POSTER, movie.poster)
            put(MoviesContract.Columns.RELEASE, movie.release)
            put(MoviesContract.Columns.OVERVIEW, movie.overview)
        }

        val uri = contentResolver.insert(MoviesContract.CONTENT_URI, values)
        Log.d(TAG, "New row id (in uri) is $uri")
        Log.d(TAG, "id (in uri) is ${uri?.let { MoviesContract.getId(it) }}")
    }

    private fun unlike(movie: Movie) {
        val movieUri = MoviesContract.CONTENT_URI
        val selectionCriteria = "${MoviesContract.Columns.MOVIE_ID} = ${movie.id}"
        val rowsAffected = contentResolver.delete(movieUri, selectionCriteria, null)
        Log.d(TAG, "Number rows deleted is $rowsAffected")
    }

    private fun isInDB(movie: Movie): Boolean {
        val likedMovie = ArrayList<Movie>()
        val cursor =
            contentResolver?.query(MoviesContract.CONTENT_URI, null, null, null, null)
        Log.d(TAG, "****************************")


        cursor?.use {
            while (it.moveToNext()) {
                //cycle through all the records
                with(cursor) {
//                    val id = getLong(0)
                    val movieID = getString(1)
                    val title = getString(2)
                    val poster = getString(3)
                    val release = getString(4)
                    val ratings = getString(5)
                    val overview = getString(6)
                    likedMovie.add(Movie(movieID, title, poster, release, overview, ratings))
                }
            }
        }
        return likedMovie.contains(movie)

    }

}