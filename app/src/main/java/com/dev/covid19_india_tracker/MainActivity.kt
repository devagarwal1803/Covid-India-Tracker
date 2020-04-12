package com.dev.covid19_india_tracker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {

    lateinit var stateAdapter: StateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header,list,false))

        fetResults()
    }
    private fun fetResults() {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { Client.api.execute() }
            if(response.isSuccessful){
//                Log.i("info",response.body?.string())
                val data = Gson().fromJson(response.body?.string(),Response::class.java)
                launch(Dispatchers.Main){
                    bindCombinedData(data.statewise?.get(0))
                    bindStateWiseData(data.statewise.subList(1,data.statewise.size))
                }
            }
        }
    }

    private fun bindStateWiseData(subList: List<StatewiseItem>) {
        stateAdapter = StateAdapter(subList)
        list.adapter = stateAdapter

    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun bindCombinedData(data: StatewiseItem) {
        val lastUpdatedTime:String? = data.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yy hh:mm:ss")
        lastUpdatedTv.text = "Last updated \n ${getTimeAgo(simpleDateFormat.parse(lastUpdatedTime))}"
        confirmedTv.text = data.confirmed
        recoveredTv.text = data.recovered
        Log.i("active",data.active.toString())
        activeTv.text = data.active
        deathTv.text = data.deaths
    }



    @SuppressLint("SimpleDateFormat")
    fun getTimeAgo(past: Date): String {
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

        return when {
            seconds < 60 -> {
                "Few seconds ago..."
            }
            minutes < 60 -> {
                "$minutes minutes ago.."
            }
            hours < 24 -> {
                "$hours hour ${minutes % 60} min ago"
            }
            else -> {
                SimpleDateFormat("dd/MM/yy hh:mm:ss").format(past).toString()
            }
        }
    }
}
