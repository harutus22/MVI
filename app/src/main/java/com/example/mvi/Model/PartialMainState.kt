package com.example.mvi.Model

interface PartialMainState {
    class Loading: PartialMainState{}
    class GotImageLink(link: String): PartialMainState{
        var imageLink = link
    }

    class Error(er: Throwable): PartialMainState{
        var error: Throwable = er
    }
}