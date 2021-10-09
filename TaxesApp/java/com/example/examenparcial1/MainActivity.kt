package com.example.examenparcial1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val periodo: Spinner = findViewById(R.id.spinnerPeriodo)
        val ingresos: EditText = findViewById(R.id.ingresos)
        val adaptador = ArrayAdapter.createFromResource(this, R.array.periodos, R.layout.spinner_item)
        val btnCalculo: Button = findViewById(R.id.btnCalculo)

        periodo.adapter = adaptador
        btnCalculo.setOnClickListener {
            print("Creando intent ingresos: ${ingresos.text} y periodo: ${periodo.selectedItem.toString()}")
            val intent = Intent(this, CalcularActivity:: class.java).apply {
                putExtra("ingreso", ingresos.text.toString())
                putExtra("periodo", periodo.selectedItem.toString())
            }
            startActivity(intent)
        }
    }
}