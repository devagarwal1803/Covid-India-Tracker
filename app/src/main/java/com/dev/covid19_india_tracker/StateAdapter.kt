package com.dev.covid19_india_tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.android.synthetic.main.item_list.view.confirmedTv
import kotlinx.android.synthetic.main.item_list.view.deathTv
import kotlinx.android.synthetic.main.item_list.view.recoveredTv

class StateAdapter(val list: List<StatewiseItem>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_list,parent,false)
        val item = list[position]
        view.confirmedTv.text=item.confirmed
//        view.activeTv.text=item.active
        view.recoveredTv.text=item.recovered
        view.deathTv.text=item.deaths

        return view;
    }

    override fun getItem(position: Int) = list[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = list.size

}