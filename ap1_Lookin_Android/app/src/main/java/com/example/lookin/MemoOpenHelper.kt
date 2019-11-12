package com.example.lookin

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MemoOpenHelper
    (context: Context) : SQLiteOpenHelper(context, DBName, null, VERSION) {

    // データベースが作成された時に実行される処理
    override fun onCreate(db: SQLiteDatabase) {
        // 処理を記述
        db.execSQL(
            "CREATE TABLE MEMO_TABLE (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "uuid TEXT, " +
                    "body TEXT)"
        )

    }


    // データベースをバージョンアップした時に実行される処理
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 処理を記述
        db.execSQL("DROP TABLE IF EXISTS MEMO_TABLE")

        // 新しくテーブルを作成する
        onCreate(db)
    }

    // データベースが開かれた時に実行される処理

    companion object {

        // データベース自体の名前(テーブル名ではない)
        private val DBName = "MEMO_DB"
        // データベースのバージョン(2,3と挙げていくとonUpgradeメソッドが実行される)
        private val VERSION = 1
    }
}
