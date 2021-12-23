package com.example.examen2parcial_b

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class NotaRecordatorioWidget: AppWidgetProvider() {
    @RequiresApi(Build.VERSION_CODES.O)
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
        @RequiresApi(Build.VERSION_CODES.O)
        fun actualizarWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int){
            Log.d(null,"Actualizar")
            //Recuperamos el mensaje personalizado para el widget actual
            var color: String?=null
            var fechaHora: String?=null
            val prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
            val tipo = prefs.getString("tipo_$widgetId","Tipo de Widget")
            var mensaje = prefs.getString("msg_$widgetId", "Hora Actual")
            if(tipo=="Nota"){
                color = prefs.getString("color_$widgetId", "Color")
            } else {
                fechaHora = prefs.getString("fecHor_$widgetId","2021-01-01T00:00:00.000")
            }

            //Obtenemos la lista de controles del widget actual
            val controles = RemoteViews(context.packageName,R.layout.nota_recordatorio_widget)

            //Actualizaremos el mensaje en el control del widget
            Log.d("Mensaje",mensaje.toString())
            Log.d("Tipo",tipo.toString())
            Log.d("Fecha",fechaHora.toString())
            if(fechaHora!=null){
                mensaje="Recordatorio\n"+fechaHora.toString().substring(0,16)+"\n\n"+mensaje
            }
            controles.setTextViewText(R.id.lblMensaje, mensaje)

            if(color!=null){//Significa que es una nota
                val col = when(color){
                    "Yellow"->Color.parseColor("#FFF9D71C")
                    "Beige"->Color.parseColor("#FFF5F5DC")
                    "Khaki"->Color.parseColor("#FFF0E68C")
                    "Green"->Color.parseColor("#FF90EE90")
                    "Blue"->Color.parseColor("#FFADD8E6")
                    else->R.color.grey
                }
                controles.setInt(R.id.widgetLayout,"setBackgroundColor", col)
            } else { //Significa que es un recordatorio
                //Obtenemos la hora actual
                val dateNow = LocalDateTime.now()
                Log.d("Fecha Actual",dateNow.toString())
                //Actualizamos la hora y color en el control del widget
                val dateRecordatorio = LocalDateTime.parse(fechaHora.toString())

                val diff = ChronoUnit.HOURS.between(dateNow, dateRecordatorio);
                Log.d("Diferencia",diff.toString())
                var colorWid=Color.BLACK
                if (diff>12){
                    colorWid=Color.GREEN
                } else if (diff<=12 && diff>1){
                    colorWid=Color.YELLOW
                } else if (diff<=1 && diff>=0){
                    colorWid=Color.argb(255,255,165,0)
                } else if (diff<0){
                    colorWid=Color.RED
                }
                controles.setInt(R.id.widgetLayout, "setBackgroundColor", colorWid)
            }
            //Notificamos al manager de la actualizacion del widget actual
            appWidgetManager.updateAppWidget(widgetId, controles)

        }
    }

}