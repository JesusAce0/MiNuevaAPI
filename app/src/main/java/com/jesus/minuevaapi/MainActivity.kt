package com.jesus.minuevaapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.jesus.minuevaapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private val listaTasks = mutableListOf<TasksResponse>()
    //val retrofitTraer = RetrofitClient.consumirApi.getTraer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()

        val rvTasks = binding.rvTasks
        //val adapter = TaskAdapter() // Crea tu adaptador personalizado aquí
        val layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = adapter
        rvTasks.layoutManager = layoutManager


        val boton1: Button = binding.btnGet
        boton1.setOnClickListener{ingresarRut()}




    }

    private fun ingresarRut() {
        val ingresandoRut: EditText = binding.etRut
        //val mostrandoTareas: TextView = binding.lblMostrarTask

        val rut =ingresandoRut.text.toString()

        metodoGetTasks(rut)



    }
    private fun initRecyclerView() {
        adapter = TaskAdapter(listaTasks)
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = adapter
    }

    private fun metodoGetTasks(rut : String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val tasks = RetrofitClient.consumirApi.getTasks(rut)

                withContext(Dispatchers.Main) {
                    val adapter = TaskAdapter(tasks)
                    binding.rvTasks.adapter = adapter
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "aaaa", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
        }
    }

    private fun metodoGet(rut:String) {
        val retrofitTraer = RetrofitClient.consumirApi.getTraer(rut)
        retrofitTraer.enqueue(object : Callback<TasksResponse> {

            override fun onResponse(call: Call<TasksResponse>, response: Response<TasksResponse>) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val formattedJson = gson.toJson(responseBody)
                    binding.lblMostrarTask.text = formattedJson
                } else {
                    // Manejar el caso en que la respuesta es nula
                }
            }

            override fun onFailure(call: Call<TasksResponse>, t: Throwable) {

                Toast.makeText(this@MainActivity, "Error al consultar Api REST", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun metodoGetTODOS(rut : String) {
        val retrofitTraer = RetrofitClient.consumirApi.getTraerTodos(rut)
        retrofitTraer.enqueue(object : Callback<TasksResponse> {

            override fun onResponse(call: Call<TasksResponse>, response: Response<TasksResponse>) {
                if (response.isSuccessful) {
                    val tasks = response.body().toString() // Esto obtendrá el objeto Tasks deserializado
                    // Ahora puedes trabajar con el objeto tasks
                    binding.lblMostrarTask.text = tasks
                } else {
                    // Manejar una respuesta no exitosa aquí
                    Toast.makeText(this@MainActivity, "No se logro", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TasksResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error al consultar Api REST", Toast.LENGTH_SHORT).show()
            }

        })
    }
}