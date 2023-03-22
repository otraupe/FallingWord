package com.otraupe.fallingwords.data.repository

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.otraupe.fallingwords.R
import com.otraupe.fallingwords.data.model.Pairing
import com.otraupe.fallingwords.util.JsonUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.reflect.Type
import javax.inject.Inject
import kotlin.random.Random


const val PAIRINGS_FILE = "words.json"

class LocalPairingRepository @Inject constructor(@ApplicationContext val appContext: Context) {

    private val dataset: List<Pairing>
    private val datasetSize: Int

    init {
        var pairingsList: List<Pairing> = listOf()
        val jsonString = JsonUtils.getJsonFromAssetsFile(appContext, PAIRINGS_FILE)
        if (jsonString == null) {
            Toast.makeText(appContext, R.string.app_pairings_assets_not_found, Toast.LENGTH_SHORT).show()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        val gson = Gson()
        val listParingsType: Type = object : TypeToken<List<Pairing?>?>() {}.type
        try {
            pairingsList = gson.fromJson(jsonString, listParingsType)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Toast.makeText(appContext, R.string.app_pairings_assets_illegal_format, Toast.LENGTH_SHORT).show()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        dataset = pairingsList
        datasetSize = pairingsList.size
    }

    fun getPairing(correct: Boolean): Pairing {
        return when (correct) {
            true -> getRandomCorrectPairing()
            false -> getRandomIncorrectPairing()
        }
    }

    private fun getRandomCorrectPairing(): Pairing {
        return dataset[randomIndex()]
    }

    private fun getRandomIncorrectPairing(): Pairing {
        val firstIndex = randomIndex()
        var secondIndex = firstIndex
        while (secondIndex == firstIndex) {
           secondIndex = randomIndex()
        }
        return Pairing(dataset[firstIndex].text_eng, dataset[secondIndex].text_spa)
    }

    private fun randomIndex(): Int {
        return Random.nextInt(datasetSize)
    }
}