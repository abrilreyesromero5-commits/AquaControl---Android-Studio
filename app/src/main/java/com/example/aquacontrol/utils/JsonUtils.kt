package com.example.aquacontrol.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.InputStreamReader


data class Locality(
    val nombre: String,
    val latitud: Double,
    val longitud: Double
)

data class Municipality(
    val nombre: String,
    val localidades: List<Locality>
)

data class State(
    val nombre: String,
    val municipios: List<Municipality>
)

data class Republic(
    @SerializedName("estados")
    val states: List<State>
)

object JsonUtils {

    fun loadRepublicFromAssets(context: Context, fileName: String = "republica.json"): Republic? {
        return try {
            val inputStream = context.assets.open(fileName)
            val reader = InputStreamReader(inputStream)
            Gson().fromJson(reader, Republic::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}