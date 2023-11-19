package com.kk.eventurmr.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.kk.data.Event
import com.kk.eventurmr.R

class EventAdapter(context: Context, events: List<Event>) : ArrayAdapter<Event>(context, 0, events) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val event = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)

        view.findViewById<TextView>(R.id.eventTitle).text = event?.name
        view.findViewById<TextView>(R.id.eventDescription).text = event?.description
        // ImageViewへのキャストを修正
        view.findViewById<ImageView>(R.id.eventMap) // ここは画像を設定する場所かもしれません

        return view
    }
}
