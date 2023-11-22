package com.kk.eventurmr.list

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils.substring
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.kk.data.AppDatabase
import com.kk.data.Event
import com.kk.data.Favorite
import com.kk.data.TimeUtil
import com.kk.data.UserId
import com.kk.eventurmr.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventAdapter(context: Context, events: List<Event>, private val db: AppDatabase) :
    ArrayAdapter<Event>(context, 0, events) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val event = getItem(position)
        val view =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        val date = TimeUtil.getDateString(event?.dateTime)
        //val lastSpaceIndex = event?.description?.substring(0, 30)?.lastIndexOf(' ')
        var description = event?.description
        if (description != null) {
            if (description.length > 30) {
               val count = description.substring(0, 30)?.lastIndexOf(' ')
                description = count?.let { description!!.substring(0, it) }
            }
        }
        view.findViewById<TextView>(R.id.eventTitle).text = event?.name
        view.findViewById<TextView>(R.id.eventDescription).text = description
        view.findViewById<TextView>(R.id.eventDate).text = date
        val eventMapImageView = view.findViewById<ImageView>(R.id.eventMap)
        eventMapImageView.isFocusable = false
        eventMapImageView.setOnClickListener {
//            val uri = Uri.parse("geo:0,0?q=${event?.location}")
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            context.startActivity(intent)
            Intent(Intent.ACTION_VIEW).also {
                it.data = Uri.parse(event?.location)
                context.startActivity(it)
            }
        }
        val eventFavoriteImageView = view.findViewById<ImageView>(R.id.eventFavorite)
        eventFavoriteImageView.isFocusable = false

        // Set initial favorite state
        eventFavoriteImageView.setBackgroundResource(if (event?.isfavorite == true) R.drawable.ic_favorite_selected else R.drawable.ic_favorite)
        eventFavoriteImageView.setOnClickListener {
            Log.d("EventAdapter", "eventFavoriteImageView clicked")
            if (event?.isfavorite == false) {
                // Update to favorite
                Log.d("EventAdapter", "${event.id} is favorite")
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        db.eventDao().updateUnFavorite(event.id)
                        val id = db.favoriteDao().getNextId()+1
                        val uid = UserId.id
                        val eid = event.id
                        val favorite = Favorite(id,uid, eid)
                        db.favoriteDao().insertFavorite(favorite)
                        db.eventDao().updateFavorite(event.id)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                eventFavoriteImageView.setBackgroundResource(R.drawable.ic_favorite_selected)
            } else if(event?.isfavorite == true) {
                // Update to unfavorite
                Log.d("EventAdapter", "${event.id} is  not favorite")
                CoroutineScope(Dispatchers.IO).launch {
                    val eids = db.favoriteDao().getEidByUid(UserId.id)
                    for(eid in eids) {
                        if(eid == event.id) {
                            db.favoriteDao().dropFavorite(UserId.id, eid)
                        }
                    }
                    db.eventDao().updateFavorite(event.id)
                }
                eventFavoriteImageView.setBackgroundResource(R.drawable.ic_favorite)
            }
            event?.isfavorite = !event?.isfavorite!!
        }
        return view
    }
}
