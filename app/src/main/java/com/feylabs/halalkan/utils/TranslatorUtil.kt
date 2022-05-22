package com.feylabs.halalkan.utils

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import com.feylabs.halalkan.view.utilview.searchwithimage.SearchWithImageModel
import com.murgupluoglu.flagkit.FlagKit
import de.hdodenhof.circleimageview.CircleImageView

object TranslatorUtil {
    val flagMap = mutableMapOf<String, String>()


    fun setup() {
        //https://flagcdn.com/en/codes.json
        flagMap["ko"] = "kr"
        flagMap["id"] = "id"
        flagMap["id"] = "id"
        flagMap["sq"] = "al"
        flagMap["en"] = "gb" //england
        flagMap["ms"] = "my" //malaysia
        flagMap["ja"] = "jp" //japan
        flagMap["sa"] = "ar" //arabic
        flagMap["fr"] = "fr" //prancis
        flagMap["de"] = "fr" //german
        flagMap["tr"] = "tr" //turkey
        flagMap["zh-CN"] = "cn" //chinese simplified
        flagMap["zh-TW"] = "cn" //chinese traditional
        flagMap["hi"] = "in" //Hindustan a.k.a India
    }

    fun getCountryFlagCodeFromGoogle(code: String): String {
        setup()
        return flagMap[code] ?: code
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
            ),
            SearchWithImageModel(
                0, "Japan", "", "ja"
            ),
            SearchWithImageModel(
                0, "Portuguese", "", "pt"
            ),
            SearchWithImageModel(
                0, "Romanian", "", "ro"
            ),
            SearchWithImageModel(
                0, "Arabic", "", "ar"
            ),
            SearchWithImageModel(
                0, "Prancis", "", "fr"
            ),
            SearchWithImageModel(
                0, "Chinese Simplified", "", "zh-CN"
            ),
            SearchWithImageModel(
                0, "Chinese Traditional", "", "zh-CN"
            ),
            SearchWithImageModel(
                0, "Turkish", "", "tr"
            ),
            SearchWithImageModel(
                0, "Hindi", "", "hi"
            ),
        )

    fun loadFlagImage(context: Context, imgFlag2: CircleImageView, flag: String) {
        val image = FlagKit.getDrawable(
            context,
            getCountryFlagCodeFromGoogle(flag)
        )
        imgFlag2.setImageDrawable(image)
    }

}