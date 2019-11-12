package com.example.lookin


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_context.*

import java.io.IOException
import java.util.*

class touko : AppCompatActivity() {

    // MemoOpenHelperクラスを定義
    internal var helper: MemoOpenHelper? = null
    // 新規フラグ
    internal var newFlag = false
    // id
    internal var id = ""
    var PICK_IMAGE_MULTIPLE = 1
    lateinit var imagePath: String
    var imagesPathList: MutableList<String> = arrayListOf()
    private var context: Context? = null
    private val READ_REQUEST_CODE = 42
    private val RESULT_CAMERA = 1001

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_context)


        // データベースから値を取得する
        if (helper == null) {
            helper = MemoOpenHelper(this@touko)
        }


        // ListActivityからインテントを取得
        val intent = this.intent
        // 値を取得
        id = intent.getStringExtra("id")
        // 画面に表示
        if (id == "") {
            // 新規作成の場合
            newFlag = true
        } else {
            // 編集の場合 データベースから値を取得して表示
            // データベースを取得する
            val db = helper!!.writableDatabase
            try {
                // rawQueryというSELECT専用メソッドを使用してデータを取得する
                val c = db.rawQuery("select body from MEMO_TABLE where uuid = '$id'", null)
                // Cursorの先頭行があるかどうか確認
                var next = c.moveToFirst()
                // 取得した全ての行を取得
                while (next) {
                    // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
                    val dispBody = c.getString(0)
                    val body = findViewById<View>(R.id.body) as EditText
                    body.setText(dispBody, TextView.BufferType.NORMAL)
                    next = c.moveToNext()
                }
            } finally {
                // finallyは、tryの中で例外が発生した時でも必ず実行される
                // dbを開いたら確実にclose
                db.close()
            }
        }

        /**
         * 登録ボタン処理
         */
        // idがregisterのボタンを取得
        val registerButton = findViewById<View>(R.id.imageButton2) as ImageButton
        // clickイベント追加

        registerButton.setOnClickListener {
            // 入力内容を取得する
            val body = findViewById<View>(R.id.body) as EditText

            val bodyStr = body.text.toString()

            // データベースに保存する
            val db = helper!!.writableDatabase
            try {
                if (newFlag) {
                    // 新規作成の場合
                    // 新しくuuidを発行する
                    id = UUID.randomUUID().toString()
                    // INSERT
                    db.execSQL("insert into MEMO_TABLE(uuid, body) VALUES('$id', '$bodyStr')")
                } else {
                    // UPDATE
                    db.execSQL("update MEMO_TABLE set body = '$bodyStr' where uuid = '$id'")

                }
            } finally {
                // finallyは、tryの中で例外が発生した時でも必ず実行される
                // dbを開いたら確実にclose
                db.close()
            }
            // 保存後に一覧へ戻る
            val intent = Intent(this@touko, com.example.lookin.Genre::class.java)
            startActivity(intent)

        }
        val backButton = findViewById<View>(R.id.imageButton) as ImageButton
        backButton.setOnClickListener{
            val intent = Intent(this@touko, com.example.lookin.ImageActivity::class.java)
            intent.putExtra("id", "")
            startActivity(intent)
        }

        /**
         * 戻るボタン処理
         */
        // idがbackのボタンを取得


        fab2.setOnClickListener {
            val ivCamera = findViewById<ImageView>(R.id.imageView)
            onCameraImageClick(ivCamera)

        }



        fab.setOnClickListener {
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_MULTIPLE);

        }


    }





    private var _imageUri: Uri? = null
    @SuppressLint("MissingSuperCall")


    fun onCameraImageClick(view: View){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permissions, 2000)
            return
        }

        val dataFormat = SimpleDateFormat("yyyMMddHHmmss")
        val now = Date()
        val nowStr  =dataFormat.format(now)
        val filename = "UseCameraActivityPhoto_${nowStr}.jpg"
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

        _imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri)
        startActivityForResult(intent, 200)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val imageView = findViewById<ImageView>(R.id.imageView)
            onCameraImageClick(imageView)
        }
    }
    @SuppressLint("MissingSuperCall")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode ==PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK) {
            var uri: Uri?
            if (resultData != null) {
                uri = resultData.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imageView.setImageBitmap(bitmap)

                    Log.d("onActivityResult", uri.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        if(requestCode == 200 && resultCode == Activity.RESULT_OK) {
            //撮影した画像のbitmapdata取得
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageURI(_imageUri)

        }

    }

}