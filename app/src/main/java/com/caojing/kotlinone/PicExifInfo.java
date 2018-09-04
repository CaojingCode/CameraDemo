package com.caojing.kotlinone;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Caojing on 2018/8/27.
 * 你不是一个人在战斗
 */
public class PicExifInfo extends SectionEntity<String> {

    private boolean isSelecte;
    private String type;

    public PicExifInfo(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public PicExifInfo(String s, boolean isSelecte) {
        super(s);
        this.isSelecte = isSelecte;
    }

    public boolean isSelecte() {
        return isSelecte;
    }

    public void setSelecte(boolean selecte) {
        isSelecte = selecte;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
