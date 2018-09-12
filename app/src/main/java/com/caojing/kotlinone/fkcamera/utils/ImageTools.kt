package com.caojing.kotlinone.fkcamera.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import jp.co.cyberagent.android.gpuimage.*

/**
 * Created by Caojing on 2018/9/12.
 *  你不是一个人在战斗
 */
object ImageTools {
    @SuppressLint("StaticFieldLeak")
    private var gpuImage: GPUImage? = null
    var imageFilterGroup: GPUImageFilterGroup = GPUImageFilterGroup()
    private var exposureFilter: GPUImageExposureFilter = GPUImageExposureFilter(0.0f)
    private var shadowFilter: GPUImageHighlightShadowFilter = GPUImageHighlightShadowFilter(0.5f, 1f)
    var levelsFilter: GPUImageLevelsFilter = GPUImageLevelsFilter()

    //把图片加载和图片滤镜处理放在同一个方法中完成，供外界进行调用
    fun getGpuImage(context: Context, bitmap: Bitmap, exprogress: Int, shadowprogress: Int, levelsprogress: Int): Bitmap {
        exposureFilter.setExposure((-1 + 2 * (exprogress / 100.0)).toFloat())
        shadowFilter.setShadows((shadowprogress / 100.0).toFloat())
        levelsFilter.setMin((levelsprogress / 200.0).toFloat(), 1.0f, 1.0f, 0.0f, 1.0f)

        imageFilterGroup.addFilter(exposureFilter)
        imageFilterGroup.addFilter(shadowFilter)
        imageFilterGroup.addFilter(levelsFilter)

        gpuImage = GPUImage(context)
        gpuImage!!.setImage(bitmap)
        gpuImage!!.setFilter(imageFilterGroup)
        return gpuImage!!.bitmapWithFilterApplied
    }

    fun clearFilter(){
        imageFilterGroup=GPUImageFilterGroup()
    }
}