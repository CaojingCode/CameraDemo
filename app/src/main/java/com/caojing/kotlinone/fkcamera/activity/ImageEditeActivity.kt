package com.caojing.kotlinone.fkcamera.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import com.blankj.utilcode.util.ImageUtils
import com.caojing.kotlinone.R
import com.caojing.kotlinone.fkcamera.filter.GPUImageFilterGroup
import com.caojing.kotlinone.fkcamera.utils.ImageTools
import jp.co.cyberagent.android.gpuimage.*
import kotlinx.android.synthetic.main.activity_img_edite.*

/**
 * Created by Caojing on 2018/9/11.
 *  你不是一个人在战斗
 */
class ImageEditeActivity : AppCompatActivity() {

    private var filePath: String = ""
    var progressOne = 60
    var progressTwo = 60
    var progressThree = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img_edite)
        filePath = intent.getStringExtra("filePath")
        iv_picedit2.setImageBitmap(ImageUtils.getBitmap(filePath))
        val bitmap = ImageTools.getGpuImage(this@ImageEditeActivity, ImageUtils.getBitmap(filePath), progressOne, progressTwo, progressThree)
        iv_picedit.setImageBitmap(bitmap)

    }

    override fun onDestroy() {
        super.onDestroy()
        ImageTools.clearFilter()
    }
}