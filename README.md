# android-sqlite-demo

## 简介

本 demo 展示 Android 中 SQLite 数据库的基本用法，包括创建表、插入数据、查询数据、更新数据和删除数据。

## 基本原理

SQLite 是 Android 内置的轻量级关系型数据库，它是一个嵌入式的 SQL 引擎，不需要单独的数据库服务器进程。

SQLite 的特点：
- 零配置：不需要安装或配置
- 嵌入式：数据库存储在单个文件中
- 事务支持：支持 ACID 事务
- 跨平台：支持多种操作系统

## 启动和使用

### 环境要求
- Android Studio 3.0+
- JDK 1.8+
- Android SDK 28

### 安装和运行
1. 用 Android Studio 打开此项目
2. 连接 Android 设备或启动模拟器
3. 点击 Run 运行项目

## 教程

### SQLiteOpenHelper

SQLiteOpenHelper 是 Android 提供的数据库帮助类，用于管理数据库的创建和升级。

```kotlin
class MyDatabaseHelper(
    context: Context,
    name: String,        // 数据库名
    factory: CursorFactory?,
    version: Int         // 数据库版本
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        // 首次创建数据库时调用
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // 数据库升级时调用
    }
}
```

### 获取数据库实例

```kotlin
// 获取可写的数据库（用于增删改）
val db = dbHelper.writableDatabase

// 获取可读的数据库（用于查询）
val db = dbHelper.readableDatabase
```

### 创建表

```kotlin
db.execSQL("""
    CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        age INTEGER
    )
""".trimIndent())
```

### 插入数据

使用 ContentValues：

```kotlin
val values = ContentValues().apply {
    put("name", "张三")
    put("age", 25)
}
db.insert("users", null, values)
```

### 查询数据

```kotlin
val cursor = db.query(
    "users",                           // 表名
    arrayOf("id", "name", "age"),      // 列名数组
    null,                              // WHERE 条件
    null,                              // WHERE 参数
    null,                              // GROUP BY
    null,                              // HAVING
    "id DESC"                          // ORDER BY
)

while (cursor.moveToNext()) {
    val id = cursor.getInt(cursor.getColumnIndex("id"))
    val name = cursor.getString(cursor.getColumnIndex("name"))
    val age = cursor.getInt(cursor.getColumnIndex("age"))
}
cursor.close()
```

### 更新数据

```kotlin
val values = ContentValues().apply {
    put("age", 35)
}
val count = db.update("users", values, "name = ?", arrayOf("李四"))
```

### 删除数据

```kotlin
val count = db.delete("users", "name = ?", arrayOf("张三"))
```

### 注意事项

1. **主线程阻塞**：数据库操作是耗时操作，不要在主线程执行
2. **游标关闭**：使用完 Cursor 后一定要调用 close() 关闭
3. **SQL 注入**：使用参数化查询，避免 SQL 注入
4. **事务**：多个操作需要原子性时，使用事务
5. **现代替代方案**：推荐使用 Room 替代原生 SQLite，Room 提供了更简洁的 API 和编译时检查
