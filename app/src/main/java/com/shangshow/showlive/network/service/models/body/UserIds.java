package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taolong on 16/8/24.
 */
public class UserIds implements Serializable {
    private List<Long> userIdList;

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }
}
