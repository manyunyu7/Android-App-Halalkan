package com.feylabs.halalkan.utils

import com.feylabs.halalkan.view.utilview.searchwithimage.SearchWithImageModel

object TranslatorUtil {
    val flagMap = mutableMapOf<String, String>()


    fun setup(){
        //https://flagcdn.com/en/codes.json
        flagMap["ko"] = "kr"
        flagMap["id"] = "id"
        flagMap["id"] = "id"
        flagMap["sq"] = "al"
    }

    fun getLanguageArray() =
        mutableListOf<SearchWithImageModel>(
            SearchWithImageModel(
                0, "Afrikaans", "", "af"
            ),
            SearchWithImageModel(
                0, "Albanian", "", "sq"
            ),
            SearchWithImageModel(
                0, "Amharic", "", "am"
            ),
            SearchWithImageModel(
                0, "English", "", "en"
            ),
            SearchWithImageModel(
                0, "Indonesia", "", "id"
            ),
            SearchWithImageModel(
                0, "Italian", "", "it"
            ),
            SearchWithImageModel(
                0, "Korean", "", "ko"
            ),
            SearchWithImageModel(
                0, "Malay", "", "ms"
            ),
            SearchWithImageModel(
                0, "Russian", "", "ru"
            ),
            SearchWithImageModel(
                0, "Vietnamese", "", "vi"
            )
        )

}