package com.example.examenparcial1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class CalcularActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular)

        val ingresoTotal: TextView = findViewById(R.id.ingresoTotal)
        val limInferior: TextView = findViewById(R.id.limInferior)
        val tasa: TextView = findViewById(R.id.tasa)
        val impMarginal: TextView = findViewById(R.id.impuestoMarginal)
        val cuotaFija: TextView = findViewById(R.id.cuotaFija)
        val impuestoRetener: TextView = findViewById(R.id.impuesto)
        val percepcionEfectiva: TextView = findViewById(R.id.percepcion)

        val ingreso = intent.getStringExtra("ingreso")
        val periodo = intent.getStringExtra("periodo")

        if(ingreso != null && periodo != null){
            val calculo = Isr(ingreso.toString().toDouble(),periodo.toString())
            calculo.calcular()
            ingresoTotal.text = round2Decimals(ingreso.toDouble())
            limInferior.text = calculo.limiteInferior.toString()
            tasa.text = calculo.tasa.toString()
            impMarginal.text = round2Decimals(calculo.impuestoMarginal)
            cuotaFija.text = calculo.cuotaFija.toString()
            impuestoRetener.text = round2Decimals(calculo.impuesto)
            percepcionEfectiva.text = round2Decimals(ingreso.toDouble()-calculo.impuesto)
        }
    }

    fun round2Decimals(number: Double): String {
        return String.format("%.1f",number)
    }
}

class Isr(val ingreso:Double, val periodo: String){
    var tasa: Double = 0.0
    var impuestoMarginal: Double = 0.0
    var limiteInferior: Double = 0.0
    var cuotaFija: Double = 0.0
    var impuesto: Double = 0.0
    val tasas = listOf<Double>(1.92, 6.4, 10.88, 16.0, 17.92, 21.36, 23.52, 30.0, 32.0, 34.0, 35.0)

    fun cuentas(cuotasFijas: List<Double>, limitesInferiores: List<Double>){
        var menor = false
        for (i in 0..9){
            if(ingreso>=limitesInferiores[i] && ingreso<limitesInferiores[i+1]) {
                tasa = tasas[i]
                limiteInferior = limitesInferiores[i]
                impuestoMarginal = (ingreso - limitesInferiores[i]) * tasa/100
                cuotaFija = cuotasFijas[i]
                impuesto = impuestoMarginal + cuotaFija
                menor = true
                break
            }
        }
        if(!menor){
            tasa =  tasas[tasas.size-1]
            limiteInferior = limitesInferiores[limitesInferiores.size-1]
            impuestoMarginal = (ingreso - limitesInferiores[limitesInferiores.size-1]) * tasa/100
            cuotaFija = cuotasFijas[cuotasFijas.size-1]
            impuesto = impuestoMarginal + cuotaFija
        }
    }

    fun calcular() {
        when(periodo){
            "Diario" -> diario()
            "Semanal" -> semanal()
            "Decenal" -> decenal()
            "Quincenal" -> quincenal()
            "Mensual" -> mensual()
        }
    }

    fun mensual(){
        val cuotasFijas = listOf<Double>(0.0,12.38,321.26,772.1,1022.01,1417.12,4323.58,7980.73,19582.83,28245.36,101876.9)
        val limitesInferiores = listOf<Double>(0.01,644.59,5470.93,9614.67,11176.63,13381.48,26988.51,42537.59,81211.26,108281.68,324845.02)
        cuentas(cuotasFijas,limitesInferiores)
    }

    fun quincenal(){
        val cuotasFijas = listOf<Double>(0.0,6.15,158.55,381.0,504.3,699.3,2133.3,3937.8,9662.55,13936.8,50268.15)
        val limitesInferiores = listOf<Double>(0.01,318.01,2699.41,4744.06,5514.76,6602.71,13316.71,20988.91,40071.31,53428.51,160285.36)
        cuentas(cuotasFijas,limitesInferiores)
    }

    fun decenal(){
        val cuotasFijas = listOf<Double>(0.0,4.1,105.7,254.0,336.2,466.2,1422.2,2625.2,6441.7,9291.2,33512.1)
        val limitesInferiores = listOf<Double>(0.01,212.01,1799.61,3162.71,3676.51,4401.81,8877.81,13992.61,26714.21,35619.01,106856.91)
        cuentas(cuotasFijas,limitesInferiores)
    }

    fun semanal(){
        val cuotasFijas = listOf<Double>(0.0,2.87,73.99,177.80,235.34,326.34,995.54,1837.64,4509.19,6503.84,23458.47)
        val limitesInferiores = listOf<Double>(0.01,148.41,1259.73,2213.9,2573.56,3081.27,6214.47,9794.83,18699.95,24933.31,74799.84)
        cuentas(cuotasFijas,limitesInferiores)
    }

    fun diario(){
        val cuotasFijas = listOf<Double>(0.0, 0.41, 10.57, 25.40, 33.62, 46.62, 142.22, 262.52, 644.17, 929.12, 3351.21)
        val limitesInferiores = listOf<Double>(0.01, 21.21, 179.97, 316.28, 367.66, 440.19, 887.79, 1399.27, 2671.43, 3561.91, 10685.70)
        cuentas(cuotasFijas,limitesInferiores)
    }

}