package com.example.lookin


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Observable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout.HORIZONTAL
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.info1.*
import kotlinx.android.synthetic.main.info3.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


class Info1Activity : AppCompatActivity(){


    private val retrofit: Retrofit by lazy { createRetrofit() }
    private val getClient: Client by lazy { retrofit.create(Client::class.java) }
    private val disposable = CompositeDisposable()

    private val onImageClick = { photo: IntArray , category: String ->
        val intent = Info2Activity.createIntent(this, photo, category)
        startActivity(intent)
    }

    private val onImageClick2 = { resId: Int ->
        val intent = Info3Activity.createIntent2(this, resId)
        startActivity(intent)
    }


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                 setContentView(R.layout.info1)


        val MoodView = findViewById<View>(R.id.MoodView) as RecyclerView
        val FoodView = findViewById<View>(R.id.FoodView) as RecyclerView
        val DrinkView = findViewById<View>(R.id.DrinkView) as RecyclerView
        val DessertView = findViewById<View>(R.id.FourView) as RecyclerView


        val MoodPhoto = intArrayOf(
            R.drawable.mood11, R.drawable.mood19, R.drawable.mood12, R.drawable.mood14,
            R.drawable.mood19, R.drawable.mood14, R.drawable.mood11
        )

        val FoodPhoto = intArrayOf(
            R.drawable.food15, R.drawable.food14, R.drawable.food13, R.drawable.food11,
            R.drawable.food14, R.drawable.food15, R.drawable.food14)

        val DrinkPhoto = intArrayOf(
            R.drawable.drink11, R.drawable.drink12, R.drawable.drink13, R.drawable.drink14,
            R.drawable.drink14, R.drawable.drink16, R.drawable.drink15,  R.drawable.drink18,
            R.drawable.drink19, R.drawable.drink15, R.drawable.drink
        )

        val DessertPhoto = intArrayOf(
            R.drawable.dessert11, R.drawable.dessert12, R.drawable.dessert13, R.drawable.dessert14,
            R.drawable.dessert17, R.drawable.dessert18, R.drawable.dessert15, R.drawable.dessert16,
            R.drawable.dessert19
        )

        val MoodManager = GridLayoutManager(this, 1, HORIZONTAL, false)
        val FoodManager = GridLayoutManager(this, 1, HORIZONTAL, false)
        val DrinkManager = GridLayoutManager(this, 1, HORIZONTAL, false)
        val DessertManager = GridLayoutManager(this, 1, HORIZONTAL, false)

        MoodView.apply {
            layoutManager = MoodManager
            adapter = InfoAdapter(MoodPhoto, this@Info1Activity, onImageClick2)
        }
        FoodView.apply {
            layoutManager = FoodManager
            adapter = InfoAdapter(FoodPhoto, this@Info1Activity, onImageClick2)
        }

        DrinkView.apply {
            layoutManager = DrinkManager
            adapter = InfoAdapter(DrinkPhoto, this@Info1Activity, onImageClick2)
        }

        DessertView.apply {
            layoutManager =DessertManager
            adapter = InfoAdapter(DessertPhoto, this@Info1Activity, onImageClick2)
        }

        requestItem()

        closeButton.setOnClickListener {
            finish()
        }

        categoryMood.setOnClickListener {
            onImageClick(MoodPhoto, "mood")
        }
        categoryFood.setOnClickListener {
            onImageClick(FoodPhoto, "food")
        }

        categoryDrink.setOnClickListener {
            onImageClick(DrinkPhoto, "drink")
        }

        categoryDessert.setOnClickListener {
            onImageClick(DessertPhoto, "dessert")
        }



    }

    fun requestItem(){
        getClient.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                shop_name.text = data.Store.name
                shop_runtime.text = data.Store.businessHours

            }){
                Log.e(Info1Activity::class.java.simpleName, it.toString())
            }.addTo(disposable)
    }

    companion object {
        private const val PHOTO: String = "photo"

        fun CreateIntent2(activity: Activity, @DrawableRes photo: Int) =
            Intent(activity, Info1Activity::class.java).apply {
                putExtra(PHOTO, photo)
            }

        private const val BASE_URL = "https://jsondata.okiba.me"
    }

    interface Client {
        // APIのエンドポイントごとにメソッドを定義する
        @GET("/v1/json/TyhBg191111143912")
        fun getData(): io.reactivex.Observable<Data>
    }

    // retrofit(APIを叩くライブラリ)のインスタンス生成
    private fun createRetrofit(): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }


    // メモリリーク対策
    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }



}
