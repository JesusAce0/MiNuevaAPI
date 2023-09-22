package com.jesus.minuevaapi

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ConsumirApi {
    @GET("todos")
    suspend fun getTasks(@Query("rut") rut : String) : List<TasksResponse>

    @POST("events") // Reemplaza "endpoint" con tu URL de API
    suspend fun postTasks(@Body taskData: TasksResponse): Response<ResponseBody>

    @PUT("/todos/{todo_id}")
    suspend fun updateTask(@Path("todo_id") todoId: String, @Body updatedData: TasksResponse): Response<Void>

    @DELETE("/todos/{todo_id}")
    suspend fun deleteTask(@Path("todo_id") todoId: String): Response<Void>

}
