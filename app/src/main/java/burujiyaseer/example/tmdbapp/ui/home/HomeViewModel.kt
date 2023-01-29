package burujiyaseer.example.tmdbapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import burujiyaseer.example.tmdbapp.data.network.Movie
import burujiyaseer.example.tmdbapp.data.network.Resource
import burujiyaseer.example.tmdbapp.data.repositories.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG  = "HomeViewModel"
class HomeViewModel : ViewModel() {

    private val repository = Repository()
    private val _homeMoviesListLiveData = MutableLiveData<Resource<List<Movie>>>()
    val homeMoviesListLiveData: LiveData<Resource<List<Movie>>>
        get() = _homeMoviesListLiveData

    private val _topRatedMoviesListLiveData = MutableLiveData<Resource<List<Movie>>>()
    val topRatedMoviesListLiveData: LiveData<Resource<List<Movie>>>
        get() = _topRatedMoviesListLiveData

    init {
        getMoviesList()
    }

    private fun getMoviesList() = viewModelScope.launch {
        _homeMoviesListLiveData.postValue(Resource.Loading)
        _topRatedMoviesListLiveData.postValue(Resource.Loading)
        Log.d(TAG, "getMoviesList: Loading")

        val popularMoviesDeferred = async { repository.getPopularMovieList() }
        val topRatedMoviesDeferred = async { repository.getTopMovieList() }

        val popularMovies = popularMoviesDeferred.await()
        val topRatedMovies = topRatedMoviesDeferred.await()
        Log.d(TAG, "popular movies is popularMovies and Resource.success is $topRatedMovies")

        val homePopularMoviesList = mutableListOf<Movie>()
        val homeTopRatedMoviesList = mutableListOf<Movie>()


        if (popularMovies is Resource.Success && topRatedMovies is Resource.Success) {
            Log.d(TAG, "success")
            homePopularMoviesList.addAll(popularMovies.value.movies)
            homeTopRatedMoviesList.addAll(topRatedMovies.value.movies)
            _homeMoviesListLiveData.postValue(Resource.Success(homePopularMoviesList))
            _topRatedMoviesListLiveData.postValue(Resource.Success(homeTopRatedMoviesList))
        } else {
            _homeMoviesListLiveData.postValue(popularMovies as Resource.Failure)
            _topRatedMoviesListLiveData.postValue(topRatedMovies as Resource.Failure)
        }
    }

}