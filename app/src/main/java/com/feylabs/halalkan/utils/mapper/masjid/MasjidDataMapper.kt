package com.feylabs.halalkan.utils.mapper.masjid

import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.view.prayer.PrayerRoomListUIModel

object MasjidDataMapper {


    fun MasjidResponseWithoutPagination.toListMasjidUI(): MutableList<PrayerRoomListUIModel> {

        val listMasjidUI = mutableListOf<PrayerRoomListUIModel>()

        this.data.forEachIndexed { index, data ->
            listMasjidUI.add(
                PrayerRoomListUIModel(
                    title = data.name,
                    categoryTop = "SELF CERTIFIED",
                    categoryMiddle = "General Restaurant",
                    address = "35F, Lotte Hotel Seoul, 1 Sogong-dong, Jung-gu, Seoul, South Korea",
                    image = data.img,
                    id = data.id.toString(),
                    distance = "5Km",
                    star = "3.5",
                    rating = "2.0"
                )
            )
        }


        return listMasjidUI
    }

}