package com.example.mvi.View

data class MainViewState(
    var isLoading: Boolean,
    var isImageViewShow: Boolean,
    var imageLink: String,
    var error: Throwable?
)