package burujiyaseer.example.tmdbapp.databases

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns

object MoviesContract {
//    internal const val TABLE_NAME = "LikedMovies"
    internal const val TABLE_NAME = "FavoritesMovies"
    /**
     * The URI to access the LikedMovies Table.
     */
    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)

    const val CONST_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$$CONTENT_AUTHORITY.$TABLE_NAME"

    //Movie fields
    object Columns {
        const val ID = BaseColumns._ID
        const val MOVIE_ID = "id"
        const val TITLE = "title"
        const val POSTER = "poster_path"
        const val RELEASE = "release_date"
        const val OVERVIEW = "overview"
        const val RATINGS = "vote_average"
    }
//    object NewColumns {
//        const val ID = BaseColumns._ID
//        const val MOVIE_ID = "id"
//    }

    fun getId(uri: Uri): Long {
        return ContentUris.parseId(uri)
    }
    fun buildUriFromId(id: Long): Uri {
        return ContentUris.withAppendedId(CONTENT_URI,id)
    }
}