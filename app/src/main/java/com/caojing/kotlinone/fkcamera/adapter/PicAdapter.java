package com.caojing.kotlinone.fkcamera.adapter;

import android.widget.ImageView;

import com.caojing.kotlinone.R;
import com.caojing.kotlinone.fkcamera.utils.BaseUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


/**
 * Created by Caojing on 2018/3/25.
 * 你不是一个人在战斗
 */

public class PicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public PicAdapter() {
        super(R.layout.item_camerapic);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        BaseUtils.showNormalImageNoCache(item, (ImageView) helper.getView(R.id.iv_item_image), R.mipmap.house_list_default_pic);
        helper.addOnClickListener(R.id.iv_item_image);
        helper.addOnClickListener(R.id.layoutClick);
    }


}
