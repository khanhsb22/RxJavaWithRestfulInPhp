package com.example.demo9

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface JsonApi {
    /*@GET("getAll.php")
    fun getAllStudents(): Observable<Any>

    @FormUrlEncoded
    @POST("insert.php")
    fun insertStudent(@Field("name") name: String, @Field("age") age: Int): Observable<Response<Void>>

    @GET("getLast.php")
    fun getLast(): Observable<Any>

    @PUT("update.php/{id}")
    fun updateStudent(@Path("id") id: Int, @Body s: Student)
    : Observable<Response<Void>>

    @DELETE("delete.php")
    fun deleteOne(@Query("id") id: Int): Observable<Response<Void>>

    @DELETE("deleteAll.php")
    fun deleteAll(): Observable<Response<Void>>*/

    // For laravel
    @GET("getAllStudents")
    fun getAllStudents(): Observable<Any>

    @FormUrlEncoded
    @POST("addStudent")
    fun insertStudent(@Field("name") name: String, @Field("age") age: Int): Observable<Response<Void>>

    @GET("getLast")
    fun getLast(): Observable<Any>

    @FormUrlEncoded
    @PUT("updateStudent")
    fun updateStudent(@Field("id") id: Int, @Field("name") name: String,
                      @Field("age") age: Int): Observable<Response<Void>>

    @DELETE("deleteStudent/{id}")
    fun deleteOne(@Path("id") id: Int): Observable<Response<Void>>

    @DELETE("deleteAllStudent")
    fun deleteAll(): Observable<Response<Void>>
}