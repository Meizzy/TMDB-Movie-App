package burujiyaseer.example.tmdbapp.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import burujiyaseer.example.tmdbapp.data.network.Movie
import burujiyaseer.example.tmdbapp.data.network.Resource
import burujiyaseer.example.tmdbapp.data.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "SearchViewModel"
class SearchViewModel : ViewModel() {
    private val repository = Repository()
    private val _moviesListLiveData = MutableLiveData<Resource<List<Movie>>>()
    val moviesListLiveData: LiveData<Resource<List<Movie>>>
        get() = _moviesListLiveData

    fun getQueriedMoviesList(query: String) = viewModelScope.launch {

        _moviesListLiveData.postValue(Resource.Loading)

        Log.d(TAG, "getQueriedMoviesList: Loading")
        val movies = withContext(Dispatchers.IO) {
            Log.d(TAG, "in coroutinesSCope")
            repository.getSearchList(query)
        }
        val moviesList = mutableListOf<Movie>()

        if (movies is Resource.Success) {
            Log.d(TAG, "success")
            moviesList.addAll(movies.value.movies)
            _moviesListLiveData.postValue(Resource.Success(moviesList))
        } else {
            Resource.Failure(false, null, null)
        }

    }
}