package com.otraupe.fallingwords.util

import android.content.Context
import java.io.IOException
import java.io.InputStream

class JsonUtils {
    companion object {
        fun getJsonFromAssetsFile(context: Context, fileName: String): String? {
            var jsonString: String? = null
            try {
                val inputStream: InputStream = context.assets.open(fileName)
                val size = inputStream.available();
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                jsonString = String(buffer, Charsets.UTF_8);
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return jsonString
        }
    }
}