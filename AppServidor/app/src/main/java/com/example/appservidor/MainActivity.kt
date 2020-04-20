package com.example.appservidor

//App conexion con el Servidor - Guardar datos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.lang.Exception
import java.net.URI.create


class MainActivity : AppCompatActivity() {
    lateinit var nom : EditText
    lateinit var ape : EditText
    lateinit var eda : EditText
    lateinit var but : RadioButton
    lateinit var gua : Button
    lateinit var mos : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nom = findViewById(R.id.nombre)
        ape = findViewById(R.id.apellido)
        eda = findViewById(R.id.edad)
        but = findViewById(R.id.radio)

        gua = findViewById(R.id.guardar)

        gua.setOnClickListener {
            var nombre=nom.text.toString()
            var apellido=ape.text.toString()
            var edad=eda.text.toString()
            var ver=but.isChecked.toString()

            Toast.makeText(applicationContext,"Los datos se gardaron en el servidor", Toast.LENGTH_SHORT).show()
            //println("datos"+nombre+"  "+ape)

            //Se invoca al subproceso NomClas.execute(//parametros a enviar tipo String si se recive como string)
            subproceso(this).execute(nombre, apellido, edad, ver)
          // var sus=susEro.toString()

            //Crear una funcion dentro de una funcion para limpiar las cajas de texto - se ponen los variables que se declararon al principio
            fun EditText.clear(){text.clear()}
            nom.clear()
            nom.clear()
            ape.clear()
            eda.clear()
            but.isChecked=false
        }
    }

    //funcion en donde se realiza la peticion a servidor
    fun run (no:String, ap:String, ed:String, bu:String){
        var nam=no
        var pel=ap
        var da=ed.toInt()
        var bt=bu.toBoolean()
        //println(nam+pel+da+bt)

        val client = OkHttpClient()

        //Creamos un objeto - los que esta en " " se utiliza en el php
        val json=JSONObject()
        json.put("nombre",nam)
        json.put("apellido",pel)
        json.put("edad",da)
        json.put("radio",bt)
        //println(json)

        //Dirección del servidor
        val url="http://10.5.49.14/android_servidor/conexion_servidor.php"
        //Para indicar el tipo de texto
        val mediaType="application/json; charset=utf-8".toMediaType()

        //Para formatear y convertir el tipo de datos request aceptado para el body
        val body=json.toString().toRequestBody(mediaType)
        //println(body)

        //Para contruir la petición
        val request=Request.Builder()
            .url(url)
            .post(body)
            .build()
            //println(request.toString())

        //Ejecutar la petición
        client.newCall(request).execute().use { response ->
            var res=response.isSuccessful
            if (!res){
                println("Los datos se han enviado $response.body!!.string()")
            }else{
                println("No se han guardado los datos "+response.body!!.string())
            }
        }
    }

    //Se crea una clase interna
    internal class subproceso(param:MainActivity) : AsyncTask<String, Void, String>(){//este es el subproceso con asyncTask
        //Se realiza un metodo constructor e inicializa con init{}
        var para:MainActivity
        init {
            this.para = param
        }

        //params recibe el array que se le envio de en onclick al invocar subproceso().execute(//parametros a enviar)
        override fun doInBackground(vararg params: String?): String {
            //Realiza una llamada a la funcion en donde se hace la peticion a servidor Objeto.Metodo(//parametros a enviar segun el tipo que se reciva)
            try {
                var ma= (params[0]).toString()
                var ma1=(params[1]).toString()
                var ma2=(params[2]).toString()
                var ma3=(params[3]).toString()
                println(ma+ma1+ma2+ma3)

                para.run(ma,ma1,ma2,ma3)
            }catch (e: Exception){
                //e.printStackTrace()
                println("Hay Un Error Baboso "+e.toString())
            }
        return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }
    }

}