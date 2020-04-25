package com.example.mvi.Utils

import io.reactivex.Observable

class DataSource(var imageList: List<String>) {
    fun getImageLinkFromList(index: Int) = Observable.just(imageList[index])
}