package com.caojing.kotlinone;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Caojing on 2018/8/21.
 * 你不是一个人在战斗
 */
public class CameraTypeAdapter extends BaseQuickAdapter<CameraTypeBean, BaseViewHolder> {
    public CameraTypeAdapter() {
        super(R.layout.item_cameratype_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, CameraTypeBean item) {
        helper.setText(R.id.tv_cameratype_name,item.getTypeName());
        if (item.getSelecte()){
            helper.setTextColor(R.id.tv_cameratype_name, Color.parseColor("#5CA52A"));
        }else{
            helper.setTextColor(R.id.tv_cameratype_name, Color.parseColor("#FFFFFF"));
        }
    }
}
