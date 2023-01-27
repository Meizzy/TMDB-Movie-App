package burujiyaseer.example.tmdbapp.databases

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log

/**
 * Provider for the TMDB app.
 * This is the only class that knows about [AppDatabase]
 **/

private const val TAG = "AppProvider"

const val CONTENT_AUTHORITY = "burujiyaseer.example.tmdbapp.databases.provider"

private const val MOVIES = 100
private const val MOVIES_ID = 101

val CONTENT_AUTHORITY_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")
class AppProvider: ContentProvider(){

    private val uriMatcher by lazy { buildUriMatcher() }

    private fun buildUriMatcher() : UriMatcher {
        Log.d(TAG, "buildUriMatcher starts")
        val matcher = UriMatcher(UriMatcher.NO_MATCH)

        //e.g. content://burujiyaseer.example.tmdbapp.databases.provider/FavoritesMovies
        matcher.addURI(CONTENT_AUTHORITY,MoviesContract.TABLE_NAME, MOVIES)

        //e.g. content://burujiyaseer.example.tmdbapp.databases.provider/FavoritesMovies/5
        matcher.addURI(CONTENT_AUTHORITY,"${MoviesContract.TABLE_NAME}/#", MOVIES_ID)

        return matcher
    }
    override fun onCreate(): Boolean {
        Log.d(TAG,"onCreate: starts")
        return true
    }

    override fun getType(uri: Uri): String {
       return when (uriMatcher.match(uri)) {
            MOVIES -> MoviesContract.CONST_TYPE

            MOVIES_ID -> MoviesContract.CONTENT_ITEM_TYPE
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        Log.d(TAG, "query: called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "query: matches $match")
        val queryBuilder = SQLiteQueryBuilder()

        when (match) {
            MOVIES -> queryBuilder.tables = MoviesContract.TABLE_NAME

            MOVIES_ID -> {
                queryBuilder.tables = MoviesContract.TABLE_NAME
                val movieId = MoviesContract.getId(uri)
                queryBuilder.appendWhere("${MoviesContract.Columns.ID} = ")       // <-- change method
                queryBuilder.appendWhereEscapeString("$movieId")       // <-- change method
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        val context =
            context ?: throw NullPointerException("In query function.  Context can't be null here!")
        val db = AppDatabase.getInstance(context).readableDatabase

        return queryBuilder.query(db, projection, p2, p3, null, null, p4)
    }

    override fun insert(uri: Uri, p1: ContentValues?): Uri {

        Log.d(TAG, "insert: called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "insert: matches $match")

        val recordId: Long
        val returnUri: Uri

        when (match) {
            MOVIES -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                recordId = db.insert(MoviesContract.TABLE_NAME,null,p1)
                if (recordId != -1L) {
                    returnUri = MoviesContract.buildUriFromId(recordId)
                } else {
                    throw SQLException("Failed to insert, Uri was $uri")
                }
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        Log.d(TAG, "Exiting insert, returning $returnUri")
        return returnUri
    }

    override fun update(uri: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        Log.d(TAG, "update: called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "update: matches $match")

        val count: Int
        var selectionCriteria: String

        when (match) {
            MOVIES -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                count = db.update(MoviesContract.TABLE_NAME, p1, p2, p3)
                }
            MOVIES_ID ->{
                val db = AppDatabase.getInstance(context!!).writableDatabase
                val id = MoviesContract.getId(uri)
                selectionCriteria = "${MoviesContract.Columns.ID} = $id"
                if (p2 != null && p2.isNotEmpty())  {
                    selectionCriteria += " AND ($p2)"
                }
                count = db.update(MoviesContract.TABLE_NAME, p1, selectionCriteria, p3)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        Log.d(TAG, "Exiting update, returning $count")
        return count
    }

    override fun delete(uri: Uri, p1: String?, p2: Array<out String>?): Int {
        Log.d(TAG, "delete: called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "delete: matches $match")

        val count: Int
        var selectionCriteria: String

        when (match) {
            MOVIES -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                count = db.delete(MoviesContract.TABLE_NAME, p1, p2)
            }
            MOVIES_ID ->{
                val db = AppDatabase.getInstance(context!!).writableDatabase
                val id = MoviesContract.getId(uri)
                selectionCriteria = "${MoviesContract.Columns.ID} = $id"
                if (p1 != null && p1.isNotEmpty())  {
                    selectionCriteria += " AND ($p1)"
                }
                count = db.delete(MoviesContract.TABLE_NAME,  selectionCriteria, p2)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        Log.d(TAG, "Exiting delete, returning $count")
        return count
    }
}