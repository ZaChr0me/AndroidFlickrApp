package com.tp.flickrapp

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley

import java.util.*

class ListActivity : AppCompatActivity() {
    lateinit var adapter:MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        adapter=MyAdapter(applicationContext)
        findViewById<ListView>(R.id.ImageList).adapter=adapter

        AsyncFlickrJSONDataForList(adapter).execute("https://www.flickr.com/services/feeds/photos_public.gne?tags=trees&format=json")
    }
}
class MyAdapter(appContext: Context):BaseAdapter(){
    var vector:Vector<String> = Vector()
    var layoutInflater:LayoutInflater = LayoutInflater.from(appContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View
        if(convertView==null) {
            view = layoutInflater.inflate(R.layout.layout_bitmap, parent, false)
        }
        else{
            view=convertView
        }
        //view.findViewById<TextView>(R.id.textView).text=vector[position]
        Log.i("TEST",vector[position].plus(" ").plus(position))
        val repListener: Response.Listener<Bitmap> = Response.Listener { response ->  view!!.findViewById<ImageView>(R.id.imageView).setImageBitmap(response)}
        val errListener = Response.ErrorListener { _ ->  view!!.findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.ic_launcher_background)}
        val imageRequest: ImageRequest = ImageRequest(vector[position], repListener, 0, 0,ImageView.ScaleType.CENTER, null, errListener)
        val queue: RequestQueue = MySingleton.getInstance(view!!.context).addToRequestQueue(imageRequest)
        return view
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        return vector.size
    }

    fun add(url:String){
        vector.addElement(url)
    }
}
class MySingleton constructor(context:Context){
    companion object {
        @Volatile
        private var INSTANCE: MySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MySingleton(context).also {
                    INSTANCE = it
                }
            }
    }
    val imageLoader: ImageLoader by lazy {
        ImageLoader(requestQueue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(20)
                override fun getBitmap(url: String): Bitmap {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })
    }
    val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>): RequestQueue {
        requestQueue.add(req)
        return requestQueue
    }

}