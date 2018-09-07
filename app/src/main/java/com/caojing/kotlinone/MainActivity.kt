package com.caojing.kotlinone

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.Flash
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private var cameraTypeAdapter: CameraTypeAdapter? = null
    private var boostLineAdapter: BoostLineAdapter? = null
    private var boostFlag = false
    private val types: Array<String> = arrayOf("客厅", "卧室", "厨房", "餐厅", "卫生间", "室内图", "户型草图", "外景图")
    private val typesBoostLine: Array<String> = arrayOf("5面墙", "4面墙", "3面墙", "水平仪")
    private var isOpenLight: Boolean = true
    private var cameraFlag : Int = 0

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
        initAdapter()
        initSeekBar()
        initListener()
        cameraFlag = 0
        GravityUtil.getInstance().init(this, callBack)
        GravityUtil.getInstance().start(this)
        view_house.setViseLine(true)
        setVisblePosition(0)
    }

    var callBack: GravityCallBack = GravityCallBack {

    }

    private fun initAdapter() {
        cameraTypeAdapter = CameraTypeAdapter()
        rl_cameratype.layoutManager = LinearLayoutManager(this)
        rl_cameratype.adapter = cameraTypeAdapter
        cameraTypeAdapter!!.setNewData(CameraData(0))
        cameraTypeAdapter!!.setOnItemClickListener { adapter, view, position ->

            cameraTypeAdapter!!.setPosition(position)
            cameraTypeAdapter!!.notifyDataSetChanged()
            var type: String = cameraTypeAdapter!!.data.get(position).typeName
            when (type) {
                "客厅" -> {
                    view_house.visibility = View.VISIBLE
                    view_house.setViseLine(true)
                    setVisblePosition(0)
                    boostLineAdapter!!.data[3].selecte = true
                    boostLineAdapter!!.notifyDataSetChanged()
                }
                "卧室" -> {
                    view_house.visibility = View.VISIBLE
                    view_house.setViseLine(true)
                    setVisblePosition(1)
                    boostLineAdapter!!.data[3].selecte = true
                    boostLineAdapter!!.notifyDataSetChanged()
                }
                "户型草图" -> {
                    visbleBoostLine()
                }
                "外景图" -> {
                    visbleBoostLine()
                }
                "室内图" -> {
                    visbleBoostLine()
                }
                else -> {
                    view_house.visibility = View.VISIBLE
                    view_house.setViseLine(true)
                    setVisblePosition(2)
                    boostLineAdapter!!.data[3].selecte = true
                    boostLineAdapter!!.notifyDataSetChanged()
                }
            }
        }
        boostLineAdapter = BoostLineAdapter()
        boostLineRecycleView.layoutManager = LinearLayoutManager(this)
        boostLineRecycleView.adapter = boostLineAdapter
        boostLineAdapter!!.setNewData(BoostLineData())
        boostLineAdapter!!.setOnItemClickListener { adapter, view, position ->
            if (position < 3) {
                setVisblePosition(position)
//                boostLineAdapter!!.position = position
//                boostLineAdapter!!.notifyDataSetChanged()
            } else {
                boostLineAdapter!!.data[position].selecte = !boostLineAdapter!!.data[position].selecte
                boostLineAdapter!!.notifyDataSetChanged()
                if (boostLineAdapter!!.data[position].selecte) {
                    view_house.setViseLine(true)
                } else {
                    view_house.setViseLine(false)
                }
            }

        }
    }

    private fun initListener() {
        boostLineText.setOnClickListener {
            if (boostFlag) {
                boostFlag = false
                boostLineRecycleView.visibility = View.GONE
            } else {
                boostFlag = true
                boostLineRecycleView.visibility = View.VISIBLE
            }
        }
        camera_light.setOnClickListener {
            isOpenLight = !isOpenLight
            if (isOpenLight) {
                camera_view.flash = Flash.AUTO
                camera_light.setImageResource(R.mipmap.light_auto)
            } else {
                camera_view.flash = Flash.OFF
                camera_light.setImageResource(R.mipmap.light_close)
            }
        }
        camera_location.setOnClickListener {
            switchBoostLine()
        }
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

    private fun BoostLineData(): List<CameraTypeBean> {
        var boostList: MutableList<CameraTypeBean> = ArrayList()
        for (i in typesBoostLine.indices) {
            val bean = CameraTypeBean()
            bean.typeName = typesBoostLine[i]
            bean.selecte = true
            boostList.add(bean)
        }
        return boostList
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
        GravityUtil.getInstance().stop()
        RxBus.getInstance().unregisterMain(Constants.RX_JAVA_TYPE_LINE)
        RxBus.getInstance().unregisterMain(Constants.RX_JAVA_TYPE_CAMERA_SHOOT)
    }

    private fun visbleBoostLine() {
        view_house.setViseLine(false)
        view_house.visibility = View.GONE
        boostLineAdapter!!.position = -1
        boostLineAdapter!!.data[3].selecte = false
        boostLineAdapter!!.notifyDataSetChanged()
    }

    private fun switchBoostLine() {
        var position: Int = boostLineAdapter!!.position
        when (position) {
            0 -> {
                when (cameraFlag) {
                    0 -> {
                        cameraFlag = 2
                    }
                    1 -> {
                        cameraFlag = 0
                    }
                    2 -> {
                        cameraFlag = 1
                    }
                }
            }
            else -> {
                when (cameraFlag) {
                    0 -> {
                        cameraFlag = 2
                    }
                    1 -> {
                        cameraFlag = 2
                    }
                    2 -> {
                        cameraFlag = 1
                    }
                }
            }
        }
        setBoostLine(position)
    }

    private fun setBoostLine(position: Int) {
        when (position) {
            0 -> {
                when (cameraFlag) {
                    0 -> {
                        camera_location.setImageResource(R.mipmap.mirror_center)
                        view_house.setBackgroundResource(R.mipmap.five_center)
                    }
                    1 -> {
                        camera_location.setImageResource(R.mipmap.mirror_left)
                        view_house.setBackgroundResource(R.mipmap.five_left)
                    }
                    2 -> {
                        camera_location.setImageResource(R.mipmap.mirror_right)
                        view_house.setBackgroundResource(R.mipmap.five_right)
                    }
                }
            }
            1 -> {
                when (cameraFlag) {
                    0 -> {
                        cameraFlag = 1
                        camera_location.setImageResource(R.mipmap.mirror_left)
                        view_house.setBackgroundResource(R.mipmap.four_left)
                    }
                    1 -> {
                        camera_location.setImageResource(R.mipmap.mirror_left)
                        view_house.setBackgroundResource(R.mipmap.four_left)
                    }
                    2 -> {
                        camera_location.setImageResource(R.mipmap.mirror_right)
                        view_house.setBackgroundResource(R.mipmap.four_right)
                    }
                }
            }
            2 -> {
                when (cameraFlag) {
                    0 -> {
                        cameraFlag = 1
                        camera_location.setImageResource(R.mipmap.mirror_left)
                        view_house.setBackgroundResource(R.mipmap.three_left)
                    }
                    1 -> {
                        camera_location.setImageResource(R.mipmap.mirror_left)
                        view_house.setBackgroundResource(R.mipmap.three_left)
                    }
                    2 -> {
                        camera_location.setImageResource(R.mipmap.mirror_right)
                        view_house.setBackgroundResource(R.mipmap.three_right)
                    }
                }
            }
        }
    }

    private fun setVisblePosition(position: Int) {
//        view_house.tag = position
        if (position == 0) {
            cameraFlag = 0
        } else {
            cameraFlag = 1
        }
        boostLineAdapter!!.position = position
        boostLineAdapter!!.notifyDataSetChanged()
        setBoostLine(position)
    }
}
