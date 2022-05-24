package com.feylabs.halalkan.utils

import java.math.RoundingMode
import java.text.DecimalFormat

class NumberUtil {

    companion object{

        fun Double.round(decimals: Int = 2): Double = this

        fun Double.roundOffDecimal(): String {
//            try{
                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.CEILING
                return df.format(this).toString()
//            }catch (e:Exception){
//            }
        }
    }
}