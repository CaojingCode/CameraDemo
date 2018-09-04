package com.caojing.kotlinone

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.Flash
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private var cameraTypeAdapter: CameraTypeAdapter? = null

    private val types: Array<String> = arrayOf("客厅", "卧室", "厨房", "餐厅", "卫生间", "室内图", "户型草图", "外景图")

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        if (p0 != null) {
            if (p0.id == R.id.seekbar_one) {
                val cameraOptions = camera_view.cameraOptions
                val minValue = cameraOptions!!.exposureCorrectionMinValue
                val maxValue = cameraOptions.exposureCorrectionMaxValue
                val progressValue = (maxValue - minValue) * p1 / 100
                camera_view.exposureCorrection = minValue + progressValue
                LogUtils.d("progressValue", camera_view.exposureCorrection.toString())
            }
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        camera_view.flash = Flash.AUTO
        camera_view.startAutoFocus(0f, 0f)
        iv_takepic.setOnClickListener {
            camera_view.capturePicture()
        }
        camera_view.addCameraListener(object : CameraListener() {

            override fun onCameraError(exception: CameraException) {
                toast("打开相机失败" + exception.message)
            }

            override fun onPictureTaken(jpeg: ByteArray?) {
                iv_camera.setImageBitmap(ImageUtils.bytes2Bitmap(jpeg))
                FileIOUtils.writeFileFromBytesByStream(BaseUtils.getFilePath(), jpeg)
            }
        })
        cameraTypeAdapter = CameraTypeAdapter()
        rl_cameratype.layoutManager = LinearLayoutManager(this)
        rl_cameratype.adapter = cameraTypeAdapter
        cameraTypeAdapter!!.setNewData(CameraData(0))
        initSeekBar()
    }

    /**
     * 初始化seekBar
     */
    private fun initSeekBar() {
        seekbar_one.setOnSeekBarChangeListener(this)
        iv_camera.setOnClickListener {
            startActivity(Intent(this, CameraPicactivtiy::class.java))
        }
    }


    /**
     * 相机拍照类型集合数据
     */
    private fun CameraData(selecteNum: Int): List<CameraTypeBean> {
        val cameraTypes: MutableList<CameraTypeBean> = ArrayList()
        for (i in types.indices) {
            val cameraTypeBean = CameraTypeBean()
            cameraTypeBean.typeName = types[i]
            cameraTypeBean.selecte = i == selecteNum
            cameraTypes.add(cameraTypeBean)
        }
        return cameraTypes
    }

    /**
     * 相机照片保存路径
     */
    fun getFilePath(): String {
        return BaseUtils.filedir + "/" + TimeUtils.getNowMills() + ".jpg"
    }


    private fun toast(message: String, det: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, det).show()
    }


    override fun onResume() {
        super.onResume()
        camera_view.start()
    }

    override fun onPause() {
        super.onPause()
        camera_view.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera_view.destroy()
    }
}
