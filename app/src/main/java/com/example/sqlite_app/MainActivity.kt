package com.example.sqlite_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val db by lazy { DBHelper(this)  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        create.setOnClickListener {
            db.insertData(User(name = "Ekrem",country = "Turkey",age = 20))
            db.insertData(User(name = "David",country = "Germany",age = 30))
            db.insertData(User(name = "Chuck",country = "England",age = 25))
        }
        read.setOnClickListener {
            showData(db.readData())
        }
        update.setOnClickListener {
            db.updateAge(5)
        }
        delete.setOnClickListener {
            db.deleteAllData()
        }

    }
    fun showData(list:MutableList<User>){
        txtData.text = ""
        list.forEach {
            txtData.text = txtData.text.toString() + "\n" + it.name + " " + it.country + " "+ it.age
    }
}}