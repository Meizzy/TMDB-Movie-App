package burujiyaseer.example.tmdbapp.databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
/**
 * Basic database class for the application
 *
 * The only class that should use this is [AppProvider].
 */
private const val TAG = "AppDatabase"

private const val DATABASE_NAME = "Favorites.db"
private const val DATABASE_VERSION = 5

internal class AppDatabase private constructor(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "onCreate: starts")
        val sSQL = """CREATE TABLE ${MoviesContract.TABLE_NAME} (
            ${MoviesContract.Columns.ID} INTEGER PRIMARY KEY NOT NULL,
            ${MoviesContract.Columns.MOVIE_ID} TEXT NOT NULL,
            ${MoviesContract.Columns.TITLE} TEXT NOT NULL,
            ${MoviesContract.Columns.POSTER} TEXT,
            ${MoviesContract.Columns.RELEASE} TEXT,
            ${MoviesContract.Columns.RATINGS} TEXT,
            ${MoviesContract.Columns.OVERVIEW} TEXT);
        """.trimMargin()
        Log.d(TAG, sSQL)
        db.execSQL(sSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        Log.d(TAG, "onUpgrade: starts")
        when(p1)    {
            4 -> {//upgrade logic from version 1
                val sSQL = """CREATE TABLE ${MoviesContract.TABLE_NAME} (
            ${MoviesContract.Columns.ID} INTEGER PRIMARY KEY NOT NULL,
            ${MoviesContract.Columns.MOVIE_ID} TEXT NOT NULL,
            ${MoviesContract.Columns.TITLE} TEXT NOT NULL,
            ${MoviesContract.Columns.POSTER} TEXT,
            ${MoviesContract.Columns.RELEASE} TEXT,
            ${MoviesContract.Columns.RATINGS} TEXT,
            ${MoviesContract.Columns.OVERVIEW} TEXT);
        """.trimMargin()
                Log.d(TAG, sSQL)
                db.execSQL(sSQL)
            }
            else -> throw IllegalStateException("onUpgrade() with unknown newVersion: $p2")
        }
    }
    companion object : SingletonHolder<AppDatabase, Context>(::AppDatabase)
}