//package com.caojing.kotlinone.fkcamera.activity
//
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
//import android.util.Log
//import android.widget.SeekBar
//import cn.co.willow.android.ultimate.gpuimage.core_config.FilterConfig
//import cn.co.willow.android.ultimate.gpuimage.core_render_filter.GPUImageFilterGroup
//import cn.co.willow.android.ultimate.gpuimage.core_render_filter.base_filter.GPUImageExposureFilter
//import cn.co.willow.android.ultimate.gpuimage.core_render_filter.base_filter.GPUImageHighlightShadowFilter
//import cn.co.willow.android.ultimate.gpuimage.core_render_filter.base_filter.GPUImageLevelsFilter
//import com.blankj.utilcode.util.FileUtils
//import com.caojing.kotlinone.R
//import com.caojing.kotlinone.fkcamera.utils.MyPureImageManager
//import kotlinx.android.synthetic.main.activity_pic_edite.*
//
///**
// * 图片编辑页面
// * Created by Caojing on 2018/8/27.
// *  你不是一个人在战斗
// */
//class PicEditeActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
//
//
//    private var filePath: String = ""
//    private var mBitmap: Bitmap? = null
//    private var exposureFilter: GPUImageExposureFilter = GPUImageExposureFilter(0.0f)
//    private var shadowFilter: GPUImageHighlightShadowFilter = GPUImageHighlightShadowFilter(0.5f, 1f)
//    var levelsFilter: GPUImageLevelsFilter = GPUImageLevelsFilter()
//    var imageFilterGroup: GPUImageFilterGroup? = GPUImageFilterGroup()
//
//    var imageManager: MyPureImageManager? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_pic_edite)
//        imageManager = MyPureImageManager.init(this)
//        imageFilterGroup!!.addFilter(exposureFilter)
//        imageFilterGroup!!.addFilter(shadowFilter)
//        imageFilterGroup!!.addFilter(levelsFilter)
//        initView()
//    }
//
//    private fun initView() {
//        Log.d("ce","")
//        filePath = intent.getStringExtra("filePath")
//        seekbar_edit_one.setOnSeekBarChangeListener(this)
//        seekbar_edit_two.setOnSeekBarChangeListener(this)
//        seekbar_edit_three.setOnSeekBarChangeListener(this)
//        // 开始纯图片处理
//        imageManager!!.setGLSurfaceView(iv_picedit)
//                .setScaleType(FilterConfig.ScaleType.CENTER_INSIDE)
//                .setImage(FileUtils.getFileByPath(filePath))
//                .setFilter(imageFilterGroup)
//        edit_saveimage.setOnClickListener {
//            imageManager!!.TakePicture()
//        }
//        edit_winoptimizer.setOnClickListener {
//            seekbar_edit_one.progress=60
//            seekbar_edit_two.progress=30
//            seekbar_edit_three.progress=10
//        }
//        edit_clear.setOnClickListener {
//            seekbar_edit_one.progress=50
//            seekbar_edit_two.progress=50
//            seekbar_edit_three.progress=0
//        }
//    }
//
//    override fun onStopTrackingTouch(p0: SeekBar?) {
//    }
//
//    override fun onStartTrackingTouch(seekbar: SeekBar?) {
//
//    }
//
//    override fun onProgressChanged(seekbar: SeekBar?, progress: Int, p2: Boolean) {
//        if (seekbar!!.id == R.id.seekbar_edit_one) {
//            exposureFilter.setExposure((-1 + 2 * (progress / 100.0)).toFloat())
//        } else if (seekbar.id == R.id.seekbar_edit_two) {
//            shadowFilter.setShadows((progress / 100.0).toFloat())
//        } else if (seekbar.id == R.id.seekbar_edit_three) {
//            levelsFilter.setMin((progress / 200.0).toFloat(), 1.0f, 1.0f, 0.0f, 1.0f)
//        }
//        imageManager!!.requestRender()
//
//    }
//
//
//}