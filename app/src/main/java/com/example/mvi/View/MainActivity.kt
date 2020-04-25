package com.example.mvi.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mvi.Model.MainView
import com.example.mvi.R
import com.example.mvi.Utils.DataSource
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*

class MainActivity : MviActivity<MainView, MainPresenter>(), MainView {

    var imageList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageList = createImageList()
    }

    private fun createImageList() =
        listOf("https://i.pinimg.com/originals/7a/d1/e3/7ad1e3626c7dbf08e5c0d2c7dd744076.jpg",
            "https://www.mordeo.org/files/uploads/2018/03/Avengers-Infinity-War-2018-950x1689.jpg",
            "https://wallpapers.moviemania.io/phone/movie/299534/89d58e/avengers-endgame-phone-wallpaper.jpg?w=1536&h=2732",
            "https://i.pinimg.com/originals/26/80/70/2680702031e0fd44872b67a56616483e.jpg",
            "https://wallpaperplay.com/walls/full/a/0/7/119622.jpg",
            "http://getwallpapers.com/wallpaper/full/0/7/5/459085.jpg");


    override fun createPresenter() = MainPresenter(DataSource(imageList!!))

    override fun getImageIntent() = RxView.clicks(btn_get_data).map { getRandomINRange(0, imageList!!.size - 1) }

    private fun getRandomINRange(min: Int, max: Int): Int {
        if (min >= max){
            throw IllegalArgumentException("max must be greater than min")
        }
        val r = Random()
        return r.nextInt((max - min) + 1) + min
    }

    override fun render(viewState: MainViewState) {
        //Here is process change state in display view

        if (viewState.isLoading){
            progress_bar.visibility = View.VISIBLE
            recycler_data.visibility = View.GONE
            btn_get_data.isEnabled = false
        } else if(viewState.error != null){
            progress_bar.visibility = View.GONE
            recycler_data.visibility = View.GONE
            btn_get_data.isEnabled = true
            Toast.makeText(this, viewState.error?.message, Toast.LENGTH_SHORT).show()
        } else if(viewState.isImageViewShow){

            btn_get_data.isEnabled = true

            Picasso.get().load(viewState.imageLink).fetch(object: Callback{
                override fun onSuccess() {
                    recycler_data.alpha = 0f
                    Picasso.get().load(viewState.imageLink).into(recycler_data)
                    recycler_data.animate().setDuration(300).alpha(1f).start()
                    progress_bar.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    progress_bar.visibility = View.GONE
                    recycler_data.visibility = View.VISIBLE
                }
            })
        }
    }
}
