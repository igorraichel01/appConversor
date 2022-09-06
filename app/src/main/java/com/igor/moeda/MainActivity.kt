package com.igor.moeda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var  result: TextView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        result = findViewById<TextView>(R.id.text_result)

        val buttonConverter = findViewById<Button>(R.id.btn_converter)
        buttonConverter.setOnClickListener{
            converter()
        }
    }
    private fun converter(){
        val selectedCurrency = findViewById<RadioGroup>(R.id.radio_group)

        val checked = selectedCurrency.checkedRadioButtonId

        val currency = when(checked){
            R.id.radio_usd -> "USD"
            R.id.radio_eur -> "EUR"
            else           -> "BTC"

        }
        val editField = findViewById<EditText>(R.id.edit_field)
        val value = editField.text.toString()

        if(value.isEmpty())
            return
        result.text = value
        result.visibility = View.VISIBLE

        Thread{
            //processamento em paralelo
            val url= URL("https://economia.awesomeapi.com.br/last/USD-BRL,EUR-BRL,BTC-BRL")
            val conn = url.openConnection() as HttpsURLConnection

            try{
                 val data = conn.inputStream.bufferedReader().readText()
                 val obj = JSONObject(data)

                runOnUiThread {

                    val res = obj.getJSONObject("${currency}BRL")
                    val res1 =res.getDouble("bid")

                    result.text = "R$${"%.3f".format(value.toDouble() * res1) }"
                    result.visibility =View.VISIBLE
                }

            }finally{
                conn.disconnect()
            }
        }.start()
    }
}


