package com.example.myapplication2

internal class Test {
    private fun smth(i: Int) {
        var i = i
        when (i) {
            0 -> {
                i = i + 5
                i = i * 2
                i = i * 10
            }
            1 -> {
                i = i * 2
                i = i * 10
            }
            2 -> i = i * 10
        }
    }
}