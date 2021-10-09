package com.example.practica6_b

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewParent
import android.widget.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imgCara: ImageView = findViewById(R.id.imgFace)
        val imgCuerpo: ImageView = findViewById(R.id.imgBody)
        val caras = arrayOf("Messi","Obama","Zuckerberg")
        val cuerpos = arrayOf("Traje 1","Traje 2","Traje 3")
        val adaptador1 = ArrayAdapter(this, android.R.layout.simple_spinner_item,caras)
        val adaptador2 = ArrayAdapter(this, android.R.layout.simple_spinner_item,cuerpos)

        val cmbCaras: Spinner = findViewById(R.id.cmbFace)
        val cmbCuerpos: Spinner = findViewById(R.id.cmbBody)

        cmbCaras.adapter = adaptador1
        cmbCuerpos.adapter = adaptador2

        cmbCaras.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val pos = parent.getItemAtPosition(position)
                when(pos){
                    "Messi" -> {
                        imgCara.setImageResource(R.drawable.messi)
                    }
                    "Obama" -> {
                        imgCara.setImageResource(R.drawable.obama)
                    }
                    "Zuckerberg" -> {
                        imgCara.setImageResource(R.drawable.zuck)
                    }
                }

            }
        }
        cmbCuerpos.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val pos = parent.getItemAtPosition(position)
                when(pos){
                    "Traje 1" -> {
                        imgCuerpo.setImageResource(R.drawable.traje2)
                    }
                    "Traje 2" -> {
                        imgCuerpo.setImageResource(R.drawable.traje3)
                    }
                    "Traje 3" -> {
                        imgCuerpo.setImageResource(R.drawable.traje4)
                    }
                }

            }
        }
    }
}