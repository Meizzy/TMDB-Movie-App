package burujiyaseer.example.tmdbapp

import android.util.Log
import burujiyaseer.example.tmdbapp.models.Movie
import burujiyaseer.example.tmdbapp.models.MovieResponse
import burujiyaseer.example.tmdbapp.services.MovieApiInterface
import burujiyaseer.example.tmdbapp.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "GetApiData"

class GetRawApiData(private val listener: OnDataAvailable) {

    interface OnDataAvailable{
        fun onPopularDataAvailable(data: List<Movie>)   {}
        fun onTrendingDataAvailable(data: List<Movie>)  {}
        fun onSearchDataAvailable(data: List<Movie>)    {}
    }

    private val popularMovie = ArrayList<Movie>()
    private val trendingMovie = ArrayList<Movie>()
    private val searchMovie = ArrayList<Movie>()

    fun popularData() = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        .getMovieList("popular")
        .enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: failed to get response with $t")
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                popularMovie.addAll(response.body()!!.movies)
                listener.onPopularDataAvailable(popularMovie)
            }
        })

    fun trendingData() = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        .getMovieList("top_rated")
        .enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: failed to get response with $t")
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                trendingMovie.addAll(response.body()!!.movies)
                listener.onTrendingDataAvailable(trendingMovie)
            }
        })




    fun getSearchMovieData(parameter: String){
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getSearchList(parameter).enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: failed to get response with $t")
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                searchMovie.addAll(response.body()!!.movies)
                listener.onSearchDataAvailable(searchMovie)
            }

        })
    }
}