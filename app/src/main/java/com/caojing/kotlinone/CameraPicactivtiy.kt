package com.caojing.kotlinone

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.blankj.utilcode.util.PermissionUtils
import kotlinx.android.synthetic.main.activity_camera_pic.*

/**
 * 房勘图库页面
 * Created by Caojing on 2018/8/27.
 *  你不是一个人在战斗
 */
class CameraPicactivtiy : AppCompatActivity() {

    var picAdapter: PicAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_pic)
        initRecyView()
    }

    fun isPermissions() {
        if (!PermissionUtils.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtils.permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .callback(object : PermissionUtils.FullCallback {
                        override fun onGranted(permissionsGranted: List<String>) {
                            //同意权限
                            picAdapter!!.setNewData(BaseUtils.getImagePathFromSD())
                        }

                        override fun onDenied(permissionsDeniedForever: List<String>, permissionsDenied: List<String>) {
                            //拒绝权限
                            PermissionUtils.launchAppDetailsSettings()
                        }
                    }).rationale { shouldRequest -> shouldRequest.again(true) }.request()

        } else {
            picAdapter!!.setNewData(BaseUtils.getImagePathFromSD())
        }
    }

    /**
     * 初始化RecyView
     */
    private fun initRecyView() {
        rl_camerapic.layoutManager = GridLayoutManager(this, 4)
        picAdapter = PicAdapter()
        rl_camerapic.adapter = picAdapter
        picAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            val intent = Intent(this, PicEditeActivity::class.java)
            intent.putExtra("filePath", picAdapter!!.getItem(position))
            startActivity(intent)
        }
        isPermissions()
    }
}