package com.netease.nim.uikit.session.extension;

import java.io.Serializable;

/**
 * Created by lorntao on 16-8-14.
 */
public class GiftInfo implements Serializable{
    long gaveProductId;
    long productId;
    String type;
    long amount;
    int dCount;
    String productName;
    String shortName;
    String logoUrl;
    String animationType; //

    public long getGaveProductId() {
        return gaveProductId;
    }

    public void setGaveProductId(long gaveProductId) {
        this.gaveProductId = gaveProductId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getdCount() {
        return dCount;
    }

    public void setdCount(int dCount) {
        this.dCount = dCount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getAnimationType() {
        return animationType;
    }

    public void setAnimationType(String animationType) {
        this.animationType = animationType;
    }

}
