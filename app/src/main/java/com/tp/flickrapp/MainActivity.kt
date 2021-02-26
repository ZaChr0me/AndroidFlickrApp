package com.tp.flickrapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //bind the GetImageOnClickListener to the click of the button
        findViewById<Button>(R.id.GetImageButton).setOnClickListener(GetImageOnClickListener(
            WeakReference(findViewById<ImageView>(R.id.image))
        ))
        //send the user to the ListActivity when clicking the button
        findViewById<Button>(R.id.GotoListButton).setOnClickListener { v ->
            val intent = Intent(applicationContext, ListActivity::class.java)
            startActivity(intent)
        }
    }
}

class GetImageOnClickListener(val imageView: WeakReference<ImageView>) : View.OnClickListener{
    override fun onClick(v: View?) {
        AsyncFlickrJSONData(imageView).execute("https://www.flickr.com/services/feeds/photos_public.gne?tags=trees&format=json")
    }
}