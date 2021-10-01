package com.example.demo9

import com.google.gson.annotations.SerializedName

class StudentBody {
    @SerializedName("students")
    var body: ArrayList<Student> = ArrayList()
}