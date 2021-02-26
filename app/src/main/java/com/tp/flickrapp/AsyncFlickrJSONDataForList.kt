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

class AsyncFlickrJSONDataForList(val adapter: MyAdapter): AsyncTask<String, Void, JSONObject>() {
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
        //add each image url to the adapter
        for (i in 1 until result.getJSONArray("items").length()){
            adapter.add(result.getJSONArray("items").getJSONObject(i).getJSONObject("media").get("m").toString())
        }
        adapter.notifyDataSetChanged()
    }

    fun readStream(input:InputStream):String{
        var reader: BufferedReader = BufferedReader(InputStreamReader(input, StandardCharsets.UTF_8),1000)
        var returnVal:String=reader.lines().collect(joining("\n"));
        input.close()
        return returnVal
    }
}