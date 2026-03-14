package com.example.demo

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    // 数据库帮助类
    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 创建数据库帮助类
        dbHelper = MyDatabaseHelper(this, "demo.db", null, 1)
        textViewResult = findViewById(R.id.textViewResult)

        // 创建表按钮
        findViewById<Button>(R.id.buttonCreateTable).setOnClickListener {
            createTable()
        }

        // 插入数据按钮
        findViewById<Button>(R.id.buttonInsert).setOnClickListener {
            insertData()
        }

        // 查询数据按钮
        findViewById<Button>(R.id.buttonQuery).setOnClickListener {
            queryData()
        }

        // 删除数据按钮
        findViewById<Button>(R.id.buttonDelete).setOnClickListener {
            deleteData()
        }

        // 更新数据按钮
        findViewById<Button>(R.id.buttonUpdate).setOnClickListener {
            updateData()
        }
    }

    // 创建表
    private fun createTable() {
        val db = dbHelper.writableDatabase

        // 使用 SQL 语句创建表
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                age INTEGER
            )
        """.trimIndent())

        textViewResult.text = "表创建成功!"
    }

    // 插入数据
    private fun insertData() {
        val db = dbHelper.writableDatabase

        // 方法1：使用 ContentValues
        val values = ContentValues().apply {
            put("name", "张三")
            put("age", 25)
        }
        db.insert("users", null, values)

        val values2 = ContentValues().apply {
            put("name", "李四")
            put("age", 30)
        }
        db.insert("users", null, values2)

        textViewResult.text = "插入数据成功!"
    }

    // 查询数据
    private fun queryData() {
        val db = dbHelper.readableDatabase

        // 查询数据
        // 参数: 表名, 列名数组, WHERE 条件, WHERE 参数, GROUP BY, HAVING, ORDER BY
        val cursor: Cursor = db.query(
            "users",
            arrayOf("id", "name", "age"),
            null,
            null,
            null,
            null,
            "id DESC"
        )

        val sb = StringBuilder()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val age = cursor.getInt(cursor.getColumnIndex("age"))
            sb.append("ID: $id, 姓名: $name, 年龄: $age\n")
        }
        cursor.close()

        textViewResult.text = if (sb.isEmpty()) "没有数据" else sb.toString()
    }

    // 删除数据
    private fun deleteData() {
        val db = dbHelper.writableDatabase

        // 删除 name 为"张三"的数据
        val count = db.delete("users", "name = ?", arrayOf("张三"))

        textViewResult.text = "删除了 $count 条数据"
    }

    // 更新数据
    private fun updateData() {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("age", 35)
        }

        // 更新 name 为"李四"的数据
        val count = db.update("users", values, "name = ?", arrayOf("李四"))

        textViewResult.text = "更新了 $count 条数据"
    }

    // 数据库帮助类
    class MyDatabaseHelper(
        context: android.content.Context,
        name: String,
        factory: SQLiteDatabase.CursorFactory?,
        version: Int
    ) : android.database.sqlite.SQLiteOpenHelper(context, name, factory, version) {

        // 创建数据库时调用
        override fun onCreate(db: SQLiteDatabase?) {
            // 在这里创建表
            // 注意：不要在这里调用 execSQL 创建表，因为 onCreate 只在数据库首次创建时调用
        }

        // 数据库升级时调用
        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            // 处理数据库升级
        }
    }
}
