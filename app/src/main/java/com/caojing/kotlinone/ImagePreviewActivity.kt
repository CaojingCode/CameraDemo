package com.caojing.kotlinone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.caojing.kotlinone.fkcamera.utils.BaseUtils
import kotlinx.android.synthetic.main.image_preview_layout.*
import me.kareluo.imaging.IMGEditActivity
import java.io.File
import java.util.*

/**
 * Created by Caojing on 2019/6/18.
 *  你不是一个人在战斗
 */
class ImagePreviewActivity : AppCompatActivity() {

    var filePath: String = ""

    lateinit var mImageFile:File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_preview_layout)
        filePath = intent.getStringExtra("filePath")
        BaseUtils.showNormalImageNoCache(filePath, ivPreview, R.mipmap.house_list_default_pic)

        btnEdit.setOnClickListener {
            onChooseImages(filePath)
        }
    }


    private fun onChooseImages(path: String) {
         mImageFile = File(cacheDir, UUID.randomUUID().toString() + ".jpg")

        startActivityForResult(
                Intent(this, IMGEditActivity::class.java)
                        .putExtra(IMGEditActivity.EXTRA_IMAGE_PATH, path)
                        .putExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH, mImageFile.absolutePath),
                100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            onImageEditDone()
        }
    }

    private fun onImageEditDone() {
        BaseUtils.showNormalImageNoCache(mImageFile.absolutePath, ivNewPreview, R.mipmap.house_list_default_pic)
    }
}