package com.shangshow.showlive.controller.entry;

import java.io.Serializable;

/**
 * Created by 1 on 2017/3/7.
 * 获取屏幕宽高
 */

public class PingMuKuanGao implements Serializable {
    public int wight;
    public int height;

    public PingMuKuanGao() {
        this.wight = wight;
        this.height = height;
    }

    public int getWight() {
        return wight;
    }

    public void setWight(int wight) {
        this.wight = wight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
