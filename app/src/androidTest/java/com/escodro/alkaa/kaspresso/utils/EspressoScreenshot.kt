package com.escodro.alkaa.kaspresso.utils


import android.util.Log
import androidx.test.runner.screenshot.Screenshot
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Сохраняет сриншот
 */
class EspressoScreenshot {
    private val screenshotFolder = File("/sdcard/screenshots")
    private val TAG = EspressoScreenshot::class.java.simpleName

    private fun prepareScreenshotPath() {
        try {
            screenshotFolder.mkdirs()
        } catch (ignored: Exception) {
            Log.e(TAG, "Failed to make screenshot folder $screenshotFolder")
        }
    }

    /**
     * Делает скриншот, сохраняет на устройстве
     *
     * @param [description] Имя файла скриншота
     * @return [File] Скриншот
     */
    fun takeScreenshot(description: String): File {
        prepareScreenshotPath()

        val capture = Screenshot.capture()

        val imageFile = File(screenshotFolder, "${description}.jpg")
        var out: BufferedOutputStream? = null
        try {
            Log.i(TAG, "Saving screenshot to " + imageFile.absolutePath)
            out = BufferedOutputStream(FileOutputStream(imageFile))
            capture.bitmap.compress(capture.format, 100, out)
            out.flush()
            Log.i(TAG, "Screenshot exists? " + imageFile.exists())
        } catch (ignored: Exception) {
            Log.e(TAG, ignored.toString())
            ignored.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (ignored: IOException) {
                Log.e(TAG, ignored.toString())
                ignored.printStackTrace()
            }
        }
        return imageFile
    }

    /**
     * Делает скриншот, сохраняет на устройстве
     *
     * @param [description] Имя файла скриншота
     * @return [String] Путь до скриншота
     */
    fun getScreenShotPath(description: String): String{
        return takeScreenshot(description).absolutePath
    }
}
