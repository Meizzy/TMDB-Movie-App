package burujiyaseer.example.tmdbapp.data.repositories

import burujiyaseer.example.tmdbapp.data.network.MovieApiInterface
import burujiyaseer.example.tmdbapp.data.network.MovieApiService
import burujiyaseer.example.tmdbapp.data.network.SafeApiCall

class Repository : SafeApiCall {

    private val movieApiService = MovieApiService()
    private val builder = movieApiService.buildApi(MovieApiInterface::class.java)

    suspend fun getPopularMovieList() = safeApiCall { builder.getMovieList("popular") }
    suspend fun getTopMovieList() = safeApiCall { builder.getMovieList("top_rated") }
    suspend fun getSearchList(query: String) = safeApiCall { builder.getSearchList(query) }
    suspend fun getSimilarMoviesList(name: String) = safeApiCall { builder.getSimilarList(name) }

}