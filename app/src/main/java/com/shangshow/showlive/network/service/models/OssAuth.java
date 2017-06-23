package com.shangshow.showlive.network.service.models;

import java.io.Serializable;

public class OssAuth implements Serializable {


    public String filePath;//文件存储目录
    public String domainUrl;//文件存储目录
    public String endPoint;//阿里OSS地址
    public String bucket;//阿里OSS存储位置
    public String callback;//阿里OSS存储位置

    public OssAuthCredentials credentials;


    public OssAuth() {

    }


    @Override
    public String toString() {
        return super.toString();
    }
}