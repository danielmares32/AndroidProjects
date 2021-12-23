package com.example.examen2parcial_b

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.util.*

class WidgetConfig: Activity() {
    private var widgetId = 0
    private lateinit var txtMensaje: EditText
    private lateinit var btnNota: Button
    private lateinit var btnRecordatorio: Button
    private var btnColors = mutableListOf<Button>()
    private lateinit var btnAceptar: Button
    private lateinit var btnCancelar: Button
    private lateinit var editDia:EditText
    private lateinit var editHora:EditText
    private var fecha=""
    private var hora=""
    private var tipo=""
    private var color=""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.widget_config)

        //Obtenemos el Intent que ha lanzado esta ventana
        // y recuperamos sus parametros
        val intentOrigen=intent
        val params=intentOrigen.extras

        //Obtenemos el ID del widget que se está configurando
        widgetId=params!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

        //Establecemos el resultado por defecto (si se pulsa el boton 'Atras' del telefono sera este el resultado devuelto)
        setResult(RESULT_CANCELED)

        //Obtenemos la referencia a los controles de la pantalla
        btnAceptar = findViewById(R.id.btnAceptar)
        btnCancelar = findViewById(R.id.btnCancelar)
        txtMensaje = findViewById(R.id.editText)
        btnNota = findViewById(R.id.rbNota)
        btnRecordatorio = findViewById(R.id.rbRecordatorio)
        btnColors.add(findViewById(R.id.btnYellow))
        btnColors.add(findViewById(R.id.btnKhaki))
        btnColors.add(findViewById(R.id.btnBeige))
        btnColors.add(findViewById(R.id.btnGreen))
        btnColors.add(findViewById(R.id.btnBlue))
        editDia = findViewById(R.id.etDate)
        editHora = findViewById(R.id.etTime)

        //Implementacion Tipo
        btnNota.setOnClickListener {
            tipo="Nota"
            val txtColor:TextView=findViewById(R.id.textColor)
            val colorLinear:LinearLayout=findViewById(R.id.colorLinear)
            val txtFecha:TextView=findViewById(R.id.txtFecha)
            txtColor.visibility=View.VISIBLE
            colorLinear.visibility=View.VISIBLE
            editDia.visibility=View.GONE
            editHora.visibility=View.GONE
            txtFecha.visibility=View.GONE
        }
        btnRecordatorio.setOnClickListener {
            tipo="Recordatorio"
            val txtColor:TextView=findViewById(R.id.textColor)
            val colorLinear:LinearLayout=findViewById(R.id.colorLinear)
            txtColor.visibility=View.GONE
            colorLinear.visibility=View.GONE
            val txtFecha:TextView=findViewById(R.id.txtFecha)
            editDia.visibility=View.VISIBLE
            editHora.visibility=View.VISIBLE
            txtFecha.visibility=View.VISIBLE

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            editDia.setOnClickListener {
                val dateDialog=DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, theMonth, theDay ->
                    val m=theMonth+1
                    editDia.setText(""+year+"-"+m+"-"+theDay)
                },year,month,day)
                dateDialog.show()
            }
            editHora.setOnClickListener{
                val timeDialog=TimePickerDialog(this,TimePickerDialog.OnTimeSetListener { timePicker, theHour, theMinute ->
                    editHora.setText(""+theHour+":"+theMinute+":00.000")
                },hour,minute,false)
                timeDialog.show()
            }
        }
        //Implementacion Color
        var selected=""
        btnColors.forEach { col: Button ->
            col.setOnClickListener {
                btnColors.forEach {
                    if(it.text.toString()=="S"){
                        it.text=selected
                        it.textSize=12f
                    }
                }
                color=col.text.toString()
                Log.d("Color",col.text.toString())
                col.text="S"
                col.textSize=20f
                selected=color
            }
        }
        //Implementacion del boton "Cancelar"
        btnCancelar.setOnClickListener {
            finish()
        }

        //Implementacion del boton "Aceptar"
        btnAceptar.setOnClickListener {
            //Guardamos el mensaje personalizado en las preferencias
            val prefs = getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            if(tipo=="Nota") {
                editor.putString("color_$widgetId", color)
            } else {
                var fecHor=editDia.text.toString()+"T"+editHora.text.toString()
                editor.putString("fecHor_$widgetId", fecHor)
            }
            editor.putString("msg_$widgetId",txtMensaje.text.toString())
            editor.putString("tipo_$widgetId",tipo)
            editor.commit()

            //Actualizamos el widget tras la configuracion
            val appWidgetManager = AppWidgetManager.getInstance(this@WidgetConfig)
            NotaRecordatorioWidget.actualizarWidget(this, appWidgetManager, widgetId)

            //Devolvemos como resultado: Aceptar (RESULT_OK)
            val resultado = Intent()
            resultado.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            setResult(RESULT_OK, resultado)
            finish()

        }

    }
}