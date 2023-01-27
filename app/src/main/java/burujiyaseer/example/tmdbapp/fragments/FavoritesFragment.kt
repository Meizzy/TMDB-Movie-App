package burujiyaseer.example.tmdbapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import burujiyaseer.example.tmdbapp.MOVIE_TRANSFER
import burujiyaseer.example.tmdbapp.MovieAdapter
import burujiyaseer.example.tmdbapp.MovieDetailsActivity
import burujiyaseer.example.tmdbapp.RecyclerItemClickListener
import burujiyaseer.example.tmdbapp.databases.MoviesContract
import burujiyaseer.example.tmdbapp.databinding.FragmentFavoritesBinding
import burujiyaseer.example.tmdbapp.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "FavoritesFragment"
class FavoritesFragment : Fragment(),
    RecyclerItemClickListener.OnRecyclerClickListener{

    private val movieAdapter = MovieAdapter(ArrayList())
    private val likedMovie = ArrayList<Movie>()
    private var _binding: FragmentFavoritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate starts")

        loadDBData()
        Log.d(TAG, "onCreate in coroutines")
        movieAdapter.loadNewData(likedMovie)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView starts")
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated starts")

        binding.rvFavMoviesList.layoutManager = GridLayoutManager(requireContext(),2)
        binding.rvFavMoviesList.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                binding.rvFavMoviesList,
                this
            )
        )
        binding.rvFavMoviesList.adapter = movieAdapter
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, ".onItemClick: starts")
//        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_TRANSFER, movieAdapter.getPhoto(position))
        startActivity(intent)
    }

    override fun onResume() {
        Log.d(TAG,"onResume")
        super.onResume()
    }

    private fun loadDBData() {
        val cursor =
            activity?.contentResolver?.query(MoviesContract.CONTENT_URI, null, null, null, null)
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
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
        }

    }
}