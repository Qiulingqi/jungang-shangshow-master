package com.shangshow.showlive.common.utils;

import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.netease.nim.uikit.model.UserInfo;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.shangshow.showlive.controller.adapter.adapter.QuickAdapter;
import com.shangshow.showlive.network.service.models.PrivateLetter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2016/10/30.
 */
public class MessageUtils {

    public static void sendMessgeP2p(String roomId, String content){
        IMMessage message = MessageBuilder.createTextMessage(roomId + "", SessionTypeEnum.P2P, content + "");
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }

    public static void sendMessgeP2p(String roomId, String content, EditText editText, ListView listView, QuickAdapter adapter, UserInfo userInfo, UserInfo currentUser, List<PrivateLetter> privateLetters){
        IMMessage message = MessageBuilder.createTextMessage(roomId + "", SessionTypeEnum.P2P, content + "");
        NIMClient.getService(MsgService.class).sendMessage(message, false);
        editText.setText("");
        PrivateLetter privateLetter = new PrivateLetter();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        privateLetter.setTime(DateUtils.formatDisplayTime(date, true));
        privateLetter.setContactId(userInfo.userId + "");
        privateLetter.setAccount(currentUser.userId + "");
        privateLetter.setMessage(content);
        privateLetters.add(privateLetter);
        adapter.add(privateLetter);
        listView.setSelection(listView.getBottom());
    }

    public static void pullMessageHistory(IMMessage imMessage, RequestCallbackWrapper requestCallbackWrapper){
        NIMClient.getService(MsgService.class).pullMessageHistory(imMessage, 100, false).setCallback(requestCallbackWrapper);
    }

    public static void incomingMessage(Observer<List<IMMessage>> incomingMessageObserver){
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
    }

}
