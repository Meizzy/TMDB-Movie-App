package burujiyaseer.example.tmdbapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import burujiyaseer.example.tmdbapp.databinding.MovieItemBinding
import burujiyaseer.example.tmdbapp.models.Movie
import com.bumptech.glide.Glide

class MovieViewHolder( val itemBinding: MovieItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
    private val imageBase = "https://image.tmdb.org/t/p/w500/"

    fun bindMovie(movie : Movie, context: Context){

        itemBinding.movieTitle.text = movie.title
        itemBinding.movieRatings.text = movie.ratings
        itemBinding.movieReleaseDate.text = movie.release
        Glide.with(itemBinding.moviePoster.context).load(imageBase + movie.poster).into(itemBinding.moviePoster)
        if (movie.ratings?.toFloat()!! > 7.0){
            itemBinding.movieRatings.background = ResourcesCompat.getDrawable(context.resources,R.drawable.background_ratings,null)
        }
        else if (movie.ratings.toFloat() > 5.0){
            itemBinding.movieRatings.background = ResourcesCompat.getDrawable(context.resources,R.drawable.background_average_ratings,null)
        }
        else{
            itemBinding.movieRatings.background = ResourcesCompat.getDrawable(context.resources,R.drawable.background_bad_ratings,null)
        }
    }
}

class MovieAdapter(
    private var movies : List<Movie>
) : RecyclerView.Adapter<MovieViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemBinding = MovieItemBinding.inflate( LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemBinding)
    }

    fun getPhoto(position: Int): Movie? {
        return if (movies.isNotEmpty()) movies[position] else null
    }

    fun loadNewData(movieList : List<Movie>){
        movies = movieList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if(movies.isNotEmpty()) movies.size else 1

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if(movies.isEmpty())    {
            holder.itemBinding.moviePoster.setImageResource(R.drawable.placeholder)
            holder.itemBinding.movieTitle.setText(R.string.failed_string)
            holder.itemBinding.movieRatings.visibility = View.GONE
            holder.itemBinding.movieReleaseDate.visibility = View.GONE
        }else   {
            holder.itemBinding.movieRatings.visibility = View.VISIBLE
            holder.itemBinding.movieReleaseDate.visibility = View.VISIBLE
            holder.bindMovie(movies[position],holder.itemView.context)
    }
    }


}