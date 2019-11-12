@file:Suppress("DEPRECATION")

package com.example.lookin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_image.*
import java.io.IOException

@Suppress("DEPRECATION")

class ImageActivity : AppCompatActivity() {

    private lateinit var arFragment: ImageArFragment
    private var shouldAddModel: Boolean = true
    private  var btnRenderable :ViewRenderable?=null
    var ID = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        var count = 0
        arFragment = sceneformFragment.let { it as ImageArFragment }
        arFragment.planeDiscoveryController.hide()
        arFragment.planeDiscoveryController.setInstructionView(null)
        arFragment.arSceneView.scene.addOnUpdateListener {
            val frame = arFragment.arSceneView.arFrame
            val augmentedImages: Collection<AugmentedImage>? =
                frame?.getUpdatedTrackables(AugmentedImage::class.java)

           // if (count % 2 == 0) {
                augmentedImages?.forEach {
                    if (it.trackingState == TrackingState.TRACKING && shouldAddModel) {
                       // count++
                        if (it.name == "shop1") {

                            ID = 0
                            /*Toast.makeText(applicationContext, "炎 ID =" + ID, Toast.LENGTH_SHORT)
                                .show()*/
                            placeObject(arFragment, it.createAnchor(it.centerPose), Uri.parse("model.sfb"))

                        }
                        if (it.name == "shop2") {
                            ID = 1
                            /*Toast.makeText(applicationContext, "田中 ID = " + ID, Toast.LENGTH_SHORT)
                                .show()*/
                            placeObject(arFragment, it.createAnchor(it.centerPose), Uri.parse("model.sfb"))
                        }
                    }
                }
            }
        //}

        val rButton = findViewById<View>(R.id.imageButton3) as ImageButton
        rButton.setOnClickListener{
            val intent = Intent(this@ImageActivity, com.example.lookin.touko::class.java)
            intent.putExtra("id", "")
            startActivity(intent)
        }
    }

    fun setupAugmentedImageDb(config: Config, session: Session){
        val bitmap: Bitmap = loadAugmentedImage() ?: return
        val bitmap1: Bitmap = loadAugmentedImage1() ?: return
        val database = AugmentedImageDatabase(session)
        database.addImage("shop1", bitmap)
        database.addImage("shop2", bitmap1)
        config.augmentedImageDatabase = database
    }

    //冗長感極まっとる　治したい
    private fun loadAugmentedImage(): Bitmap? {
            try {
                val inputStream = assets.open("shop1.jpg")
                return BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                Log.d("imageLoad", "io exception while loading", e)
            }
        return null
    }

    private fun loadAugmentedImage1(): Bitmap? {
        try {
            val inputStream = assets.open("shop2.jpg")
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            Log.d("imageLoad", "io exception while loading", e)
        }
        return null
    }

    // FIXME: copied from MainActivity
    @RequiresApi(Build.VERSION_CODES.N)
    private fun placeObject(fragment: ArFragment, anchor: Anchor, model: Uri) {
        ModelRenderable.builder()
                .setSource(fragment.context, model)
                .build()
                .thenAccept {
                    addNodeToScene(fragment, anchor, it)

                }
                .exceptionally {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(it.message).setTitle("Error")
                    val dialog = builder.create()
                    dialog.show()

                    return@exceptionally null
                }
    }

    private fun addNodeToScene(fragment: ArFragment, anchor: Anchor, renderable: Renderable) {
        val anchorNode = AnchorNode(anchor)
        val node = TransformableNode(fragment.transformationSystem).apply{
            setParent(anchorNode)
            fragment.arSceneView.scene.addChild(anchorNode)

            localRotation= Quaternion.lookRotation(Vector3.down(), Vector3.up())//
        }
        node.renderable = renderable
        node.localRotation= Quaternion.lookRotation(Vector3.down(), Vector3.up())
        node.select()
        addName(anchorNode,node," ");
    }




    private fun addName(anchorNode: AnchorNode, node: TransformableNode, name: String) {
        ViewRenderable.builder().setView(this,R.layout.content_main)
                .build()
                .thenAccept { ViewRenderable ->
                    val nameView = TransformableNode(arFragment.transformationSystem).apply {
                        localRotation= Quaternion.lookRotation(Vector3.down(), Vector3.up())
                    }
                    nameView.localPosition = Vector3(0f,node.localPosition.y+0f,0f)
                    nameView.localRotation= Quaternion.lookRotation(Vector3.down(), Vector3.up())
                    nameView.setParent(anchorNode)
                    nameView.renderable = ViewRenderable
                    nameView.select()

                    val intent = Intent(this, Info1Activity::class.java)
                    val txt_name =ViewRenderable.view as TextView

                    txt_name.apply {
                        //当たり判定の調整
                        setTextColor(Color.parseColor("#00000000"))
                        text=name
                        setBackgroundColor(Color.parseColor("#00000000"))
                        setPadding(10,30,10,10)
                        layoutParams.apply {
                            height = 50
                            width = 20
                        }
                        setOnClickListener{
                            startActivity(intent)
                        }
                        requestLayout()
                    }
                }
    }
}