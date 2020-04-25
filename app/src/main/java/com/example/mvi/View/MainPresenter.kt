package com.example.mvi.View

import com.example.mvi.Model.MainView
import com.example.mvi.Model.PartialMainState
import com.example.mvi.Utils.DataSource
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(var dataSource: DataSource): MviBasePresenter<MainView, MainViewState>() {


    override fun bindIntents() {
        val gotData = intent(MainView::getImageIntent)
            .switchMap{index -> dataSource?.getImageLinkFromList(index)
                ?.map { imageLink -> PartialMainState.GotImageLink(imageLink) as PartialMainState }}
            .startWith(PartialMainState.Loading())
            .onErrorReturn { error -> PartialMainState.Error(error)}
            .subscribeOn(Schedulers.io())

        val initState = MainViewState(false, false, "", null)

        val initIntent = gotData.observeOn(AndroidSchedulers.mainThread())

        subscribeViewState(initIntent.scan(initState, this::viewStateReducer), MainView::render)
    }

    fun viewStateReducer(prevState: MainViewState, changedState: PartialMainState): MainViewState{
        val newState = prevState
        if (changedState is PartialMainState.Loading){
            newState.isLoading = true
            newState.isImageViewShow = false
        }

        if (changedState is PartialMainState.GotImageLink){
            newState.isLoading = false
            newState.isImageViewShow = true
            newState.imageLink = changedState.imageLink
        }

        if (changedState is PartialMainState.Error){
            newState.isLoading = false
            newState.isImageViewShow = false
            newState.error = changedState.error
        }

        return newState
    }
}