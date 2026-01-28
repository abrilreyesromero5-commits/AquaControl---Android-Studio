package com.example.aquacontrol.utils

import android.content.Context
import android.util.JsonReader
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.InputStreamReader

data class Locality(
    val nombre: String,
    val latitud: Double,
    val longitud: Double
)

typealias MunicipalityMap = Map<String, List<Locality>>
typealias StateMap = Map<String, MunicipalityMap>

fun loadJsonFromAssets(context: Context, fileName: String): StateMap {
    val inputStream = context.assets.open(fileName)
    val reader = JsonReader(InputStreamReader(inputStream, "UTF-8"))

    val stateMap = mutableMapOf<String, MunicipalityMap>()

    reader.beginObject()
    while (reader.hasNext()) {
        val name = reader.nextName()
        if (name == "estados") {
            reader.beginArray()
            while (reader.hasNext()) {
                reader.beginObject()
                var estadoNombre = ""
                val municipioMap = mutableMapOf<String, List<Locality>>()

                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "nombre" -> estadoNombre = reader.nextString()
                        "municipios" -> {
                            reader.beginArray()
                            while (reader.hasNext()) {
                                reader.beginObject()
                                var municipioNombre = ""
                                val localidades = mutableListOf<Locality>()

                                while (reader.hasNext()) {
                                    when (reader.nextName()) {
                                        "nombre" -> municipioNombre = reader.nextString()
                                        "localidades" -> {
                                            reader.beginArray()
                                            while (reader.hasNext()) {
                                                var locNombre = ""
                                                var latitud = 0.0
                                                var longitud = 0.0
                                                reader.beginObject()
                                                while (reader.hasNext()) {
                                                    when (reader.nextName()) {
                                                        "nombre" -> locNombre = reader.nextString()
                                                        "latitud" -> latitud = reader.nextDouble()
                                                        "longitud" -> longitud = reader.nextDouble()
                                                        else -> reader.skipValue()
                                                    }
                                                }
                                                reader.endObject()
                                                localidades.add(Locality(locNombre, latitud, longitud))
                                            }
                                            reader.endArray()
                                        }
                                        else -> reader.skipValue()
                                    }
                                }
                                reader.endObject()
                                municipioMap[municipioNombre] = localidades
                            }
                            reader.endArray()
                        }
                        else -> reader.skipValue()
                    }
                }
                reader.endObject()
                stateMap[estadoNombre] = municipioMap
            }
            reader.endArray()
        } else {
            reader.skipValue()
        }
    }
    reader.endObject()
    reader.close()

    return stateMap
}

