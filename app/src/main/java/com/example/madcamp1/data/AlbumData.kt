package com.example.madcamp1.data

import android.content.Context

object AlbumData {
    fun getAlbumList(context: Context): List<Album> {
        val numbers = listOf(
            // UCPC 2024
            32111, 32112, 32113, 32114, 32115, 32116,
            32117, 32118, 32119, 32120, 32121, 32122, 32123,

            // UCPC 2022
            25384, 25385, 25386, 25387, 25388, 25389, 25390,
            25391, 25392, 25393, 25394, 25395, 25396,

            // UCPC 2021
            22873, 22874, 22875, 22876, 22877, 22878, 22879,
            22880, 22881, 22882, 22883, 22884
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