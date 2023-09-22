package com.jesus.minuevaapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.jesus.minuevaapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private val listaTasks = mutableListOf<TasksResponse>()

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


        val JAboton1: Button = binding.btnGet
        JAboton1.setOnClickListener{ingresarRut()}

        val JAboton2: Button = binding.btnCreate
        JAboton2.setOnClickListener { ingresarNuevaTarea()}

        val JAboton3: Button = binding.btnUpdate
        JAboton3.setOnClickListener { ingresarUpdateTarea() }

        val JAboton4: Button = binding.btnDelete
        JAboton4.setOnClickListener { ingresarBorrarNuevaTarea() }



    }


    private fun ingresarBorrarNuevaTarea() {
        val JAtraerRut: String = binding.etRut.text.toString().trim()
        val JAtraerID: String = binding.txtIDTask.text.toString().trim()

        if (JAtraerRut.isEmpty()) {
            Toast.makeText(this, "Ingrese un rut para buscar tareas", Toast.LENGTH_SHORT).show()
        } else if (JAtraerID.isEmpty()) {
            Toast.makeText(this, "Ingrese un ID de localizar su tarea", Toast.LENGTH_SHORT).show()
        } else{
            metodoDeleteTask(JAtraerID)
            limpiarEntradas()
        }


    }
    private fun ingresarUpdateTarea() {
        val JAtraerRut: String = binding.etRut.text.toString().trim()
        val JAtraerID: String = binding.txtIDTask.text.toString()
        val JAtraerTitulo: String = binding.txtTitleTask.text.toString()
        val JAtraerDescripcion: String = binding.txtDescriptionTask.text.toString()

        if (JAtraerRut.isEmpty()) {
            Toast.makeText(this, "Ingrese un rut para buscar tareas", Toast.LENGTH_SHORT).show()
        } else if (JAtraerID.isEmpty()) {
            Toast.makeText(this, "Ingrese un ID de localizar su tarea", Toast.LENGTH_SHORT).show()
        } else if (JAtraerTitulo.isEmpty()) {
            Toast.makeText(this, "Ingrese un titulo nuevo de tarea", Toast.LENGTH_SHORT).show()
        } else if (JAtraerDescripcion.isEmpty()) {
            Toast.makeText(this, "Ingrese una nueva descripcion de tarea", Toast.LENGTH_SHORT)
                .show()
        } else {
            val taskData = TasksResponse(
                null,
                JAtraerDescripcion,
                JAtraerRut,
                JAtraerTitulo
            )
            metodoUpdateTask(JAtraerID, taskData)
            limpiarEntradas()

        }
    }

    private fun ingresarNuevaTarea() {
        val traerRut :String = binding.etRut.text.toString().trim()
        val traerID :String = binding.txtIDTask.text.toString()
        val traerTitulo : String = binding.txtTitleTask.text.toString()
        val traerDescripcion : String = binding.txtDescriptionTask.text.toString()

        if (traerRut.isEmpty()) {
            Toast.makeText(this, "Ingrese un rut" , Toast.LENGTH_SHORT).show()
        }else if (traerID.isEmpty()) {
            Toast.makeText(this, "Ingrese un ID de tarea" , Toast.LENGTH_SHORT).show()
        }else if (traerTitulo.isEmpty()) {
            Toast.makeText(this, "Ingrese un titulo de tarea" , Toast.LENGTH_SHORT).show()
        }else{
            val taskData = TasksResponse(
                traerID,
                traerDescripcion,
                traerRut,
                traerTitulo
            )
            metodoPostTasks(taskData)
            limpiarEntradas()
        }

    }

    private fun ingresarRut() {
        val ingresandoRut: EditText = binding.etRut
        val rut =ingresandoRut.text.toString()

        metodoGetTasks(rut)
    }
    private fun initRecyclerView() {
        adapter = TaskAdapter(listaTasks)
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = adapter
    }

    private fun metodoDeleteTask(taskId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.consumirApi.deleteTask(taskId)

                if (response.isSuccessful) {
                    // La solicitud de eliminación fue exitosa
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            "Tarea eliminada con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // La solicitud no fue exitosa, manejar errores según el código de estado HTTP
                    val statusCode = response.code()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            "Error al eliminar: $statusCode",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Manejar errores de red u otros errores aquí
            }
        }
    }


    private fun metodoUpdateTask(taskId: String, updatedData: TasksResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.consumirApi.updateTask(taskId, updatedData)

                if (response.isSuccessful) {
                    // La solicitud de actualización fue exitosa
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            "Tarea actualizada con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // La solicitud no fue exitosa, manejar errores según el código de estado HTTP
                    val statusCode = response.code()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            "Error al actualizar: $statusCode",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Manejar errores de red u otros errores aquí
            }
        }
    }


    private fun metodoPostTasks(taskData: TasksResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.consumirApi.postTasks(taskData)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Tarea ingresda con exito", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // La solicitud no fue exitosa, manejar errores según el código de estado HTTP
                    val statusCode = response.code()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Error: $statusCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Manejar errores de red u otros errores aquí
            }
        }
    }
    private fun metodoGetTasks(rut : String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val tasks = RetrofitClient.consumirApi.getTasks(rut)

                tasks
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

    private fun limpiarEntradas () {
        binding.txtIDTask.text.clear()
        binding.txtTitleTask.text.clear()
        binding.txtDescriptionTask.text.clear()
    }

}