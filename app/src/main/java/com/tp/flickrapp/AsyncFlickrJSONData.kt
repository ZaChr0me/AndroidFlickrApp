package com.tp.flickrapp

import android.os.AsyncTask
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import org.json.JSONObject
import java.io.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors.joining

class AsyncFlickrJSONData(val image: WeakReference<ImageView>): AsyncTask<String, Void, JSONObject>() {
    override fun doInBackground(vararg params: String?): JSONObject? {

        var jsonResult: JSONObject?=null
        var url: URL? = null
        try {
            url = URL(params[0])
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            try {
                val `in`: InputStream = BufferedInputStream(urlConnection.getInputStream())
                val s: String = readStream(`in`)
                Log.i("JFL", s)
                jsonResult= JSONObject(s.substringAfter("(").substringBeforeLast(")"))
            } finally {

                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return jsonResult
    }

    override fun onPostExecute(result:JSONObject){
        super.onPostExecute(result)
        Log.i("JFF",result.toString())
        //get the first image from the flickr feed
        var link:String=result.getJSONArray("items").getJSONObject(0).getJSONObject("media").get("m").toString();
        //use the bitmap downloader to get and show the image
        AsyncBitmapDownloader(image).execute(link)
    }

    fun readStream(input:InputStream):String{
        var reader: BufferedReader = BufferedReader(InputStreamReader(input, StandardCharsets.UTF_8),1000)
        var returnVal:String=reader.lines().collect(joining("\n"));
        input.close()
        return returnVal
    }
}