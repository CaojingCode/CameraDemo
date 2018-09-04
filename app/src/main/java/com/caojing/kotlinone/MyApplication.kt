package com.caojing.kotlinone

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 * Created by Caojing on 2018/8/20.
 *  你不是一个人在战斗
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}