package burujiyaseer.example.tmdbapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import burujiyaseer.example.tmdbapp.*
import burujiyaseer.example.tmdbapp.databinding.FragmentHomeBinding
import burujiyaseer.example.tmdbapp.models.Movie


private const val TAG = "HomeFragment"
class HomeFragment : Fragment(),
    RecyclerItemClickListener.OnRecyclerClickListener,
    GetRawApiData.OnDataAvailable {

    private val popularMovieAdapter = MovieAdapter(ArrayList())
    private val trendingMovieAdapter = MovieAdapter(ArrayList())
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate starts")


        val getRawApiData = GetRawApiData(this)
        getRawApiData.popularData()
        getRawApiData.trendingData()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView starts")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated starts")

        binding.rvMoviesList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTrendingMoviesList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvMoviesList.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                binding.rvMoviesList,
                this
            )
        )

        binding.rvTrendingMoviesList.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                binding.rvTrendingMoviesList,
                this
            )
        )
        binding.rvMoviesList.adapter = popularMovieAdapter
        binding.rvTrendingMoviesList.adapter = trendingMovieAdapter

    }

    override fun onPopularDataAvailable(data: List<Movie>) {
        Log.d(TAG, "onDataAvailable starts with $data")
        popularMovieAdapter.loadNewData(data)
        Log.d(TAG, "onDataAvailable ends")
    }

    override fun onTrendingDataAvailable(data: List<Movie>) {
        Log.d(TAG, "onDataAvailable starts with $data")
        trendingMovieAdapter.loadNewData(data)
        Log.d(TAG, "onDataAvailable ends")
    }


    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, ".onItemClick: starts")
//        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), MovieDetailsActivity::class.java)
        if (view.parent == binding.rvMoviesList){
            intent.putExtra(MOVIE_TRANSFER, popularMovieAdapter.getPhoto(position))
        }
        else {
            intent.putExtra(MOVIE_TRANSFER, trendingMovieAdapter.getPhoto(position))
        }
        startActivity(intent)
    }

}

