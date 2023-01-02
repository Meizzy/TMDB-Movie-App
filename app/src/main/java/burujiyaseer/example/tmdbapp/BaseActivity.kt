package burujiyaseer.example.tmdbapp

import androidx.appcompat.app.AppCompatActivity


internal const val MOVIE_TRANSFER = "MOVIE_TRANSFER"

open class BaseActivity: AppCompatActivity() {


//    internal val setCurrentActivityListener   = NavigationBarView.OnItemSelectedListener {
//        when(it.itemId) {
//            R.id.home -> {
//                Log.d(TAG,"setCurrentActivityListener: home clicked")
//                finish()
//                return@OnItemSelectedListener true
//            }
//            R.id.action_search -> {
//                Log.d(TAG,"setCurrentActivityListener: search clicked")
//                startActivity(Intent(this, SearchActivity::class.java))
//                return@OnItemSelectedListener true
//            }
//            R.id.favorite -> {
//                Log.d(TAG,"setCurrentActivityListener: favorite clicked")
//                startActivity(Intent(this, SearchActivity::class.java))
//                return@OnItemSelectedListener true
//            }
//        }
//        false
//    }

}