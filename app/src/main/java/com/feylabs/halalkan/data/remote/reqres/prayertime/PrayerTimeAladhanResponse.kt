package com.feylabs.halalkan.data.remote.reqres.prayertime


import com.google.gson.annotations.SerializedName

data class PrayerTimeAladhanResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("status")
    val status: String
) {
    data class Data(
        @SerializedName("date")
        val date: Date,
        @SerializedName("meta")
        val meta: Meta,
        @SerializedName("timings")
        val timings: Timings
    ) {
        data class Date(
            @SerializedName("gregorian")
            val gregorian: Gregorian,
            @SerializedName("hijri")
            val hijri: Hijri,
            @SerializedName("readable")
            val readable: String,
            @SerializedName("timestamp")
            val timestamp: String
        ) {
            data class Gregorian(
                @SerializedName("date")
                val date: String,
                @SerializedName("day")
                val day: String,
                @SerializedName("designation")
                val designation: Designation,
                @SerializedName("format")
                val format: String,
                @SerializedName("month")
                val month: Month,
                @SerializedName("weekday")
                val weekday: Weekday,
                @SerializedName("year")
                val year: String
            ) {
                data class Designation(
                    @SerializedName("abbreviated")
                    val abbreviated: String,
                    @SerializedName("expanded")
                    val expanded: String
                )

                data class Month(
                    @SerializedName("en")
                    val en: String,
                    @SerializedName("number")
                    val number: Int
                )

                data class Weekday(
                    @SerializedName("en")
                    val en: String
                )
            }

            data class Hijri(
                @SerializedName("date")
                val date: String,
                @SerializedName("day")
                val day: String,
                @SerializedName("designation")
                val designation: Designation,
                @SerializedName("format")
                val format: String,
                @SerializedName("holidays")
                val holidays: List<String>,
                @SerializedName("month")
                val month: Month,
                @SerializedName("weekday")
                val weekday: Weekday,
                @SerializedName("year")
                val year: String
            ) {
                data class Designation(
                    @SerializedName("abbreviated")
                    val abbreviated: String,
                    @SerializedName("expanded")
                    val expanded: String
                )

                data class Month(
                    @SerializedName("ar")
                    val ar: String,
                    @SerializedName("en")
                    val en: String,
                    @SerializedName("number")
                    val number: Int
                )

                data class Weekday(
                    @SerializedName("ar")
                    val ar: String,
                    @SerializedName("en")
                    val en: String
                )
            }
        }

        data class Meta(
            @SerializedName("latitude")
            val latitude: Double,
            @SerializedName("latitudeAdjustmentMethod")
            val latitudeAdjustmentMethod: String,
            @SerializedName("longitude")
            val longitude: Double,
            @SerializedName("method")
            val method: Method,
            @SerializedName("midnightMode")
            val midnightMode: String,
            @SerializedName("offset")
            val offset: Offset,
            @SerializedName("school")
            val school: String,
            @SerializedName("timezone")
            val timezone: String
        ) {
            data class Method(
                @SerializedName("id")
                val id: Int,
                @SerializedName("location")
                val location: Location,
                @SerializedName("name")
                val name: String,
                @SerializedName("params")
                val params: Params
            ) {
                data class Location(
                    @SerializedName("latitude")
                    val latitude: Double,
                    @SerializedName("longitude")
                    val longitude: Double
                )

                data class Params(
                    @SerializedName("Fajr")
                    val fajr: Int,
                    @SerializedName("Isha")
                    val isha: Int
                )
            }

            data class Offset(
                @SerializedName("Asr")
                val asr: Int,
                @SerializedName("Dhuhr")
                val dhuhr: Int,
                @SerializedName("Fajr")
                val fajr: Int,
                @SerializedName("Imsak")
                val imsak: Int,
                @SerializedName("Isha")
                val isha: Int,
                @SerializedName("Maghrib")
                val maghrib: Int,
                @SerializedName("Midnight")
                val midnight: Int,
                @SerializedName("Sunrise")
                val sunrise: Int,
                @SerializedName("Sunset")
                val sunset: Int
            )
        }

        data class Timings(
            @SerializedName("Asr")
            val asr: String,
            @SerializedName("Dhuhr")
            val dhuhr: String,
            @SerializedName("Fajr")
            val fajr: String,
            @SerializedName("Imsak")
            val imsak: String,
            @SerializedName("Isha")
            val isha: String,
            @SerializedName("Maghrib")
            val maghrib: String,
            @SerializedName("Midnight")
            val midnight: String,
            @SerializedName("Sunrise")
            val sunrise: String,
            @SerializedName("Sunset")
            val sunset: String
        )
    }
}