package com.example.aplicacionmovilessoftware

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BBaseDeDatos.inicializarArreglo()

        val botonCicloVida = findViewById<Button>(R.id.btn_ir_ciclo_vida)
        botonCicloVida
                .setOnClickListener {
                    irAActividad(ACicloVida::class.java)
                }

        val botonListView = findViewById<Button>(R.id.btn_ir_list_view)
        botonListView
            .setOnClickListener {
                irAActividad(BListView::class.java)
            }

        val botonIrCIntentExplicitoParametros = findViewById<Button>(R.id.btn_ir_intent_explicito_parametros)
        botonIrCIntentExplicitoParametros
            .setOnClickListener {
                val intentExplicito = Intent(
                        this,
                        CIntentExplicitoParametros::class.java
                )
                intentExplicito.putExtra("nombre", "Carlos")
                intentExplicito.putExtra("apellido", "Mantilla")
                intentExplicito.putExtra("edad", 28)

                val ligaPokemon = DLiga("Liga Kanto", "Kanto")
                val ash = BEntrenador("Ash", "Pueblo Paleta", ligaPokemon)

                intentExplicito.putExtra("entrenador", ash)

                startActivityForResult(intentExplicito, 102)



/*                val parametros = arrayListOf<Pair<String,*>>(
                    Pair("nombre","Carlos"),
                    Pair("apellido","Mantilla"),
                    Pair("edad", 28)
                )
                irAActividad(CIntentExplicitoParametros::class.java, parametros)
*/
            }
        EBaseDeDatos.TablaUsuario = ESqliteHelperUsuario(this)
        if (EBaseDeDatos.TablaUsuario != null){
            val usuarioEncontrado = EBaseDeDatos.TablaUsuario?.consultarUsuarioPorId(1)

            Log.i(
                    "bdd","ID: ${usuarioEncontrado?.id} Nombre: ${usuarioEncontrado?.nombre} " +
                    "Descripcion: ${usuarioEncontrado?.descripcion}"
            )

            if (usuarioEncontrado?.id == 0){
                val resultadoCrear = EBaseDeDatos.TablaUsuario
                ?.crearUsuarioFormulario("Carlos","Estudiante")
                if (resultadoCrear != null){
                    Log.i("bdd", "Se creo correctamente")
            }else{
                    Log.i("bdd", "Hubo errores")
                }

            }else{
                val resultadoActualizar = EBaseDeDatos.TablaUsuario
                        ?.actualizarUsuarioFormulario(
                                "Vicente",
                                Date().time.toString(),
                                1
                        )
                if (resultadoActualizar != null){
                    if (resultadoActualizar){
                        Log.i("bdd", "Se actualiz??")
                    }else{
                        Log.i("bdd", "Errores")
                    }
                }
            }
        }


        val botonIrFIntentConRespuesta = findViewById<Button>(
            R.id.btn_ir_intent_con_respuesta
        )
        botonIrFIntentConRespuesta
            .setOnClickListener {
                irAActividad(
                    FIntentConRespuesta::class.java
                )
            }

        val botonIrRecyclerView = findViewById<Button>(
            R.id.btn_ir_recycler_view
        )
        botonIrRecyclerView
            .setOnClickListener {
                irAActividad(
                    GRecyclerView::class.java
                )
            }
    }  // fin onCreate


    fun irAActividad(
        clase: Class<*>,
        parametros: ArrayList<Pair<String,*>>?=null,
        codigo: Int? = null
    ) {
        val intentExplicito = Intent(
                this,
                clase
        )
        if (parametros != null){
            parametros.forEach {
                val nombreVariable = it.first
                val valorVariable = it.second

                var tipoDato = false

                tipoDato = it.second is String
                if (tipoDato == true){
                    intentExplicito.putExtra(nombreVariable, valorVariable as String)
                    tipoDato = false
                }

                tipoDato = it.second is Int
                if (tipoDato == true){
                    intentExplicito.putExtra(nombreVariable, valorVariable as Int)
                    tipoDato = false
                }

            }
        }
        if (codigo != null){
            startActivityForResult(intentExplicito, codigo)
        }else{
            startActivity(intentExplicito)
        }

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            102 -> {
                if (resultCode == RESULT_OK){

                    if (data != null){

                        val nombre = data.getStringExtra("nombre")
                        val edad = data.getIntExtra("edad",0)

                        Log.i("intent-explicito","Nombre: ${nombre} Edad: ${edad}")
                    }else{
                        //CODIGO SIN REDULTADO OK
                    }

                }else{
                    Log.i("intent-explicito","Usuario no llen?? los datos")
                }
            }
        }
    }
}