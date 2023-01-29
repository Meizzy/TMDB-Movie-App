package burujiyaseer.example.tmdbapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import burujiyaseer.example.tmdbapp.data.network.Resource
import burujiyaseer.example.tmdbapp.databinding.FragmentHomeBinding
import burujiyaseer.example.tmdbapp.ui.*
import burujiyaseer.example.tmdbapp.ui.base.BaseFragment
import burujiyaseer.example.tmdbapp.ui.base.MOVIE_TRANSFER
import burujiyaseer.example.tmdbapp.ui.details.MovieDetailsActivity


private const val TAG = "HomeFragment"

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
), RecyclerItemClickListener.OnRecyclerClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private val popularMovieAdapter = MovieAdapter(ArrayList())
    private val trendingMovieAdapter = MovieAdapter(ArrayList())


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMoviesList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addOnItemTouchListener(
                RecyclerItemClickListener(
                    requireContext(), binding.rvMoviesList, this@HomeFragment
                )
            )
            adapter = popularMovieAdapter
        }

        binding.rvTrendingMoviesList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addOnItemTouchListener(
                RecyclerItemClickListener(
                    requireContext(), binding.rvTrendingMoviesList, this@HomeFragment
                )
            )
            adapter = trendingMovieAdapter
        }

        viewModel.homeMoviesListLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                    if (it.isNetworkError)
                    requireActivity().snackbar("Check your network connection")
                    else
                        requireActivity().snackbar("${it.errorCode} : ${it.errorBody}")
                    binding.progressBar.hide()
                    popularMovieAdapter.loadNewData(ArrayList())
                }
                Resource.Loading -> binding.progressBar.show()
                is Resource.Success -> {
                    binding.progressBar.hide()
                    popularMovieAdapter.loadNewData(it.value)
                }
            }
        }

        viewModel.topRatedMoviesListLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                    binding.progressBar.hide()
                    trendingMovieAdapter.loadNewData(ArrayList())
                }
                Resource.Loading -> binding.progressBar.show()
                is Resource.Success -> {
                    binding.progressBar.hide()
                    trendingMovieAdapter.loadNewData(it.value)
                }
            }
        }
    }


    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, ".onItemClick: starts")
//        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), MovieDetailsActivity::class.java)
        requireActivity().snackbar("Normal tap at position $position")
        if (view.parent == binding.rvMoviesList) {
            intent.putExtra(MOVIE_TRANSFER, popularMovieAdapter.getPhoto(position))
        } else {
            intent.putExtra(MOVIE_TRANSFER, trendingMovieAdapter.getPhoto(position))
        }
        startActivity(intent)
    }

}

