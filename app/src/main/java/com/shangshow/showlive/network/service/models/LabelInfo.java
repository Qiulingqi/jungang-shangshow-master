package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

/**
 * Created by lorntao on 16-8-7.
 */
public class LabelInfo implements Serializable {
    private String labelCode;
    private String labelName;

    public String getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(String labelCode) {
        this.labelCode = labelCode;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}
