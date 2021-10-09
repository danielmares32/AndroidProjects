package com.example.practica15

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WordOfTheDay: AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        //Iteramos la lista de widgets en ejecucion
        for(i in appWidgetIds.indices){
            //ID del Widget
            val widgetId = appWidgetIds[i]
            actualizarWidget(context,appWidgetManager, widgetId)
        }
    }

    companion object{
        fun actualizarWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int){
            Log.d(null,"Actualizar")
            //Recuperamos el mensaje personalizado para el widget actual
            val prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
            val mensaje = prefs.getString("msg_$widgetId", "Word Of The Day")

            //Obtenemos la lista de controles del widget actual
            val controles = RemoteViews(context.packageName,R.layout.word_of_the_day)

            //Actualizaremos el mensaje en el control del widget
            controles.setTextViewText(R.id.Lbl1, mensaje)

            //Obtenemos la palabra
            val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://random-words-api.vercel.app/").addConverterFactory(GsonConverterFactory.create()).build()
            val service = retrofit.create<ApiService>(ApiService::class.java)
            service.getAllWords().enqueue(object: Callback<List<Word>>{
                override fun onResponse(call: Call<List<Word>>, response: Response<List<Word>>) {
                    val palabra = response.body()!!.get(0)
                    controles.setTextViewText(R.id.word, palabra.word)
                    controles.setTextViewText(R.id.definition,palabra.definition)
                    Log.d("Response",palabra.toString())
                    appWidgetManager.updateAppWidget(widgetId, controles)
                }

                override fun onFailure(call: Call<List<Word>>, t: Throwable) {
                    t?.printStackTrace()
                }
            })
        }

    }
}