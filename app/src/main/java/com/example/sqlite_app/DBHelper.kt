package com.example.sqlite_app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.lang.Exception

class DBHelper(val context: Context) : SQLiteOpenHelper(context,DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION) {
    private val TABLE_NAME="User"
    private val COL_ID = "id"
    private val COL_NAME = "name"
    private val COL_COUNTRY = "country"
    private val COL_AGE = "age"
    companion object {
        private val DATABASE_NAME = "SQLITE_DATABASE"//database adı
        private val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = " CREATE TABLE  " + TABLE_NAME+"("+
                 COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                //çift tırnaktan sonra bir boşluk olmak zorunda eğer boşluk koymazsanız tablo oluşmaz.
                 COL_NAME + " VARCHAR(256), "+
                 COL_COUNTRY +" VARCHAR(256), "+
                 COL_AGE + " INTEGER ) "
        db?.execSQL(createTable)

        //onCreate de tablomuzu oluşturduk.
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //onUpgrade;
        //daha önceden aynı isimde bir tablo oluşmuşsa onu silip bunu ekliyor.
    }

    fun insertData(user:User){
        //veri kaydetme işlemini yapacağız.
        val sqliteDB = this.writableDatabase
        try {
            //try ,catch : try içerisine veri kaydetme metotlarımızı yazıyoruz .Çünkü try sayesinde herhangi bir hata olduğunda uygulamanın kapanmasını önlemiş oluyoruz.
            val contentValues = ContentValues()
            contentValues.put(COL_NAME , user.name)
            contentValues.put(COL_COUNTRY, user.country)
            contentValues.put(COL_AGE, user.age)

            val result = sqliteDB.insert(TABLE_NAME,null,contentValues)

            Toast.makeText(context,if(result != -1L) "Kayıt Başarılı" else "Kayıt yapılamadı.", Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    @SuppressLint("Range")
    fun readData():MutableList<User>{
        val userList = mutableListOf<User>()
        val sqliteDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = sqliteDB.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                val user = User()
                user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                user.name = result.getString(result.getColumnIndex(COL_NAME))
                user.country = result.getString(result.getColumnIndex(COL_COUNTRY))
                user.age = result.getString(result.getColumnIndex(COL_AGE)).toInt()
                userList.add(user)
            }while (result.moveToNext())
            //moveToNext=="bir sonrakine geç anlamında"
        }
        result.close()
        sqliteDB.close()
        return userList
    }
    fun deleteAllData(){
        val sqliteDB = this.writableDatabase
        sqliteDB.delete(TABLE_NAME,null,null)
        sqliteDB.close()

    }

    @SuppressLint("Range")
    fun updateAge(age:Int) {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                val cv = ContentValues()
                cv.put(COL_AGE,(result.getInt(result.getColumnIndex(COL_AGE))+age))
                db.update(TABLE_NAME,cv, "$COL_ID=? AND $COL_NAME=?",
                    arrayOf(result.getString(result.getColumnIndex(COL_ID)),
                        result.getString(result.getColumnIndex(COL_NAME))))
            }while (result.moveToNext())
        }

        result.close()
        db.close()
    }

}