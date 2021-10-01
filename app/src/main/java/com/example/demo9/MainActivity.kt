package com.example.demo9

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var jsonApi: JsonApi
    var gson = Gson()
    lateinit var studentBody: StudentBody
    var students: ArrayList<Student> = ArrayList()
    lateinit var studentAdapter: StudentAdapter
    var name = ""
    var age = ""
    var last_id = 0
    var id = 0
    var update_position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*jsonApi = Retrofit.Builder().baseUrl("http://192.168.1.8/sample1/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(JsonApi::class.java)*/

        // For laravel
        jsonApi = Retrofit.Builder().baseUrl("http://192.168.1.8:8000/api/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(JsonApi::class.java)

        getAllStudents()
        handleWithEvents()

    }

    private fun handleWithEvents() {
        button_insert.setOnClickListener {
            name = edt_name.text.toString()
            age = edt_age.text.toString()

            jsonApi.insertStudent(name, age.toInt())
                .subscribeOn(Schedulers.io()) // io.reactivex.schedulers
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<Void>> {
                    override fun onSubscribe(d: Disposable) {
                        val customToast = CustomToast(this@MainActivity, "Insert student success !")
                        customToast.showToast()
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Error_insert", e.toString())
                    }

                    override fun onComplete() {
                        jsonApi.getLast().subscribeOn(Schedulers.io()) // io.reactivex.schedulers
                            .observeOn(AndroidSchedulers.mainThread()) // Quan sát
                            .subscribe(object :
                                Observer<Any> { //io.reactivex.disposables.Disposable, io.reactivex
                                override fun onComplete() {
                                    students.add(Student(last_id, name, age.toInt()))
                                    studentAdapter.notifyItemInserted(students.size - 1)
                                    recycler_students.scrollToPosition(students.size - 1)
                                }

                                override fun onSubscribe(d: Disposable) {
                                }

                                override fun onNext(t: Any) { // t equal with response
                                    Log.d("Response_get_last", t.toString())
                                    /*var jsonArray = JSONArray(gson.toJson(t))
                                    val jsonObject = jsonArray.getJSONObject(0)
                                    last_id = jsonObject.getString("id").toInt()*/
                                    var jsonObject = JSONObject(gson.toJson(t))
                                    last_id = jsonObject.getInt("id")
                                }

                                override fun onError(e: Throwable) {
                                    Log.d("Error", e.toString())
                                }

                            })
                    }

                    override fun onNext(t: Response<Void>) {

                    }
                })
        }

        button_update.setOnClickListener {
            name = edt_name.text.toString()
            age = edt_age.text.toString()

            jsonApi.updateStudent(id, name, age.toInt())
                .subscribeOn(Schedulers.io()) // io.reactivex.schedulers
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :
                    Observer<Response<Void>> { //io.reactivex.disposables.Disposable, io.reactivex
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Response<Void>) {
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Error_update", e.toString())
                    }

                    override fun onComplete() {
                        Toast.makeText(
                            this@MainActivity,
                            "Update 1 field success !",
                            Toast.LENGTH_SHORT
                        ).show()
                        students[update_position] = Student(id, name, age.toInt())
                        studentAdapter.notifyItemChanged(update_position)
                    }
                })
        }

        button_delete.setOnClickListener {
            jsonApi.deleteOne(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<Void>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Response<Void>) {
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Error_delete", e.toString())
                    }

                    override fun onComplete() {
                        Toast.makeText(
                            this@MainActivity,
                            "Delete 1 field success !",
                            Toast.LENGTH_SHORT
                        ).show()
                        students.removeAt(update_position)
                        studentAdapter.notifyItemRemoved(update_position)
                    }
                })
        }

        img_deleteAll.setOnClickListener {
            var alertDialog = AlertDialog.Builder(this@MainActivity)
            alertDialog.setTitle("Delete all")
            alertDialog.setMessage("Do you want to delete all student ?")
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton("OK") { dialog, which ->
                jsonApi.deleteAll().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<Response<Void>> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(t: Response<Void>) {
                        }

                        override fun onError(e: Throwable) {
                            Log.d("Error_delete", e.toString())
                        }

                        override fun onComplete() {
                            Toast.makeText(
                                this@MainActivity,
                                "Deleted all field !",
                                Toast.LENGTH_SHORT
                            ).show()
                            students.clear()
                            studentAdapter.notifyDataSetChanged()
                            edt_name.text = Editable.Factory.getInstance().newEditable("")
                            edt_age.text = Editable.Factory.getInstance().newEditable("")
                        }
                    })
                alertDialog.setCancelable(true)
            }
            alertDialog.setNegativeButton("CANCEL") {dialog, which ->
                alertDialog.setCancelable(true)
                dialog.dismiss()
            }
            alertDialog.show()

        }
    }

    private fun getAllStudents() {
        recycler_students.setHasFixedSize(true)
        var layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.VERTICAL, false
        )
        val itemDecorator =
            DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.divider_custom
            )!!
        )
        recycler_students.addItemDecoration(itemDecorator)
        recycler_students.layoutManager = layoutManager

        jsonApi.getAllStudents().subscribeOn(Schedulers.io()) // io.reactivex.schedulers
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Any> { //io.reactivex.disposables.Disposable, io.reactivex
                override fun onComplete() {
                    /*studentBody.body.forEachIndexed { index, student ->
                        students.add(Student(student.id, student.name, student.age))
                    }*/
                    studentAdapter = StudentAdapter(students)
                    recycler_students.adapter = studentAdapter

                    studentAdapter.setOnItemClickListener(object : StudentAdapter.ClickListener {
                        override fun onItemClick(position: Int, v: View) {
                            var item = students[position]
                            id = item.id
                            edt_name.text = Editable.Factory.getInstance().newEditable(item.name)
                            edt_age.text =
                                Editable.Factory.getInstance().newEditable(item.age.toString())
                            update_position = position

                        }
                    })
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Any) { // t equal with response
                    Log.d("Response", t.toString())
                    /*var jsonObject = JSONObject(gson.toJson(t))
                    studentBody = gson.fromJson(jsonObject.toString(), StudentBody::class.java)*/
                    var jsonArray = JSONArray(gson.toJson(t))
                    for (i in 0 until jsonArray.length()) {
                        var jsonObject = jsonArray.getJSONObject(i)
                        students.add(Student(jsonObject.getInt("id"), jsonObject.getString("name"),
                        jsonObject.getInt("age")))
                    }

                }

                override fun onError(e: Throwable) {
                    Log.d("Error_get_all", e.toString())
                }

            })
    }
}

