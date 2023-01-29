package burujiyaseer.example.tmdbapp.ui.details

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

class DetailsViewModel : ViewModel() {
    private val repository = Repository()
    private val _moviesListLiveData = MutableLiveData<Resource<List<Movie>>>()
    val moviesListLiveData: LiveData<Resource<List<Movie>>>
        get() = _moviesListLiveData

    fun getSimilarMoviesList(movieName: String) = viewModelScope.launch {

        _moviesListLiveData.postValue(Resource.Loading)

        val moviesDeferred = withContext(Dispatchers.IO) {
            repository.getSimilarMoviesList(movieName)
        }
        val moviesList = mutableListOf<Movie>()

        if (moviesDeferred is Resource.Success) {
            moviesList.addAll(moviesDeferred.value.movies)
            _moviesListLiveData.postValue(Resource.Success(moviesList))
        } else {
            Resource.Failure(false, null, null)
        }
    }
}