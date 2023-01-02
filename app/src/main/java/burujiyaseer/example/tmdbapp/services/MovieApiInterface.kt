package burujiyaseer.example.tmdbapp.services

import burujiyaseer.example.tmdbapp.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiInterface {

    @GET("/3/movie/{popular}?api_key=8c60639ec58078f765c18a8da8b280e0")
     fun getMovieList(@Path(value = "popular") parameter: String ): Call<MovieResponse>
    @GET("/3/search/movie/?api_key=8c60639ec58078f765c18a8da8b280e0")
     fun getSearchList(@Query(value = "query") parameter: String ): Call<MovieResponse>
     @GET("/3/movie/{popular}/recommendations?api_key=8c60639ec58078f765c18a8da8b280e0")
     fun getSimilarList(@Path(value = "popular") parameter: String ): Call<MovieResponse>
}