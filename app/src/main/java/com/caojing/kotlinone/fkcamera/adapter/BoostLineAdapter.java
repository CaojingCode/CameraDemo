package com.caojing.kotlinone.fkcamera.adapter;

import android.graphics.Color;

import com.caojing.kotlinone.R;
import com.caojing.kotlinone.fkcamera.bean.CameraTypeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * 辅助线
 */
public class BoostLineAdapter extends BaseQuickAdapter<CameraTypeBean, BaseViewHolder> {
    public BoostLineAdapter() {
        super(R.layout.item_boostline);
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    protected void convert(BaseViewHolder helper, CameraTypeBean item) {
        helper.setText(R.id.textName, item.getTypeName());
        if(helper.getLayoutPosition() < 3){
            if(position == helper.getLayoutPosition()){
                helper.setTextColor(R.id.textName, Color.parseColor("#5CA52A"));
            } else {
                helper.setTextColor(R.id.textName, Color.parseColor("#FFFFFF"));
            }
        } else{
            if(item.getSelecte()){
                helper.setTextColor(R.id.textName, Color.parseColor("#5CA52A"));
            } else {
                helper.setTextColor(R.id.textName, Color.parseColor("#FFFFFF"));
            }
        }

    }
}
