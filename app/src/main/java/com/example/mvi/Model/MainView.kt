package com.example.mvi.Model

import com.example.mvi.View.MainViewState
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface MainView: MvpView {
    fun getImageIntent(): Observable<Int>

    fun render(viewState: MainViewState)
}