package com.kk.eventurmr.list

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils.substring
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.kk.data.Event
import com.kk.data.TimeUtil
import com.kk.eventurmr.DetailActivity
import com.kk.eventurmr.R

class EventAdapter(context: Context, events: List<Event>) :
    ArrayAdapter<Event>(context, 0, events) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val event = getItem(position)
        val view =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        val date = TimeUtil.getDateString(event?.dateTime)
        val lastSpaceIndex = event?.description?.substring(0, 30)?.lastIndexOf(' ')

        view.findViewById<TextView>(R.id.eventTitle).text = event?.name
        view.findViewById<TextView>(R.id.eventDescription).text =
            lastSpaceIndex?.let { event?.description?.substring(0, it) }
        view.findViewById<TextView>(R.id.eventDate).text = date
        val eventMapImageView =view.findViewById<ImageView>(R.id.eventMap)
        eventMapImageView.isFocusable = false
        eventMapImageView.setOnClickListener {
            val uri = Uri.parse("geo:0,0?q=${event?.location}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
        return view
    }
}
