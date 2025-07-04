package com.example.madcamp1.data

import android.content.Context

object AlbumData {
    fun getAlbumList(context: Context): List<Album> {
        val numbers = listOf(
            32111, 32112, 32113, 32114, 32115, 32116,
            32117, 32118, 32119, 32120, 32121, 32122, 32123
        )

        return numbers.map { number ->
            createAlbum(context, number)
        }
    }

    private fun createAlbum(context: Context, number: Int): Album {
        val images = mutableListOf<Int>()
        var i = 1
        while (true) {
            val resName = "img_${number}_${i}"
            val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
            if (resId == 0) break
            images.add(resId)
            i++
        }
        return Album(
            id = number,
            title = "백준 $number 번 문제",
            images = images
        )
    }
}