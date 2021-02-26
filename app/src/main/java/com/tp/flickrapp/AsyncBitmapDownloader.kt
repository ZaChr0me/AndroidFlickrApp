package com.tp.flickrapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class AsyncBitmapDownloader(val image: WeakReference<ImageView>): AsyncTask<String, Void, Bitmap>() {
    override fun doInBackground(vararg params: String?): Bitmap {
        lateinit var bitmapResult:Bitmap
        try {
            var url:URL = URL(params[0])
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            try {
                //download the image from the url
                val input: InputStream = BufferedInputStream(urlConnection.inputStream)
                //make a bitmap from the received data
                bitmapResult = BitmapFactory.decodeStream(input)
            }finally {
                urlConnection.disconnect()
            }
        }catch (e: MalformedURLException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }

        return bitmapResult
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        //put image on ui
        image.get()?.setImageBitmap(result)
    }
}