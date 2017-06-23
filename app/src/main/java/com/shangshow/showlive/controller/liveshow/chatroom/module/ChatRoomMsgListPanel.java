package com.shangshow.showlive.controller.liveshow.chatroom.module;

import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.UserPreferences;
import com.netease.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nim.uikit.common.adapter.TViewHolder;
import com.netease.nim.uikit.common.ui.listview.ListViewUtil;
import com.netease.nim.uikit.session.audio.MessageAudioControl;
import com.netease.nim.uikit.session.module.Container;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.shangshow.showlive.controller.adapter.RoomMsgAdapter;
import com.shangshow.showlive.controller.liveshow.chatroom.viewholder.ChatRoomMsgViewHolderFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 聊天室消息收发模块
 * Created by huangjun on 2016/1/27.
 */
public class ChatRoomMsgListPanel implements TAdapterDelegate {
    private static final int MESSAGE_CAPACITY = 500;

    // container
    private Container container;
    private View rootView;
    private Handler uiHandler;

    // message list view
    private ListView messageListView;
    private List<ChatRoomMessage> items = new LinkedList<ChatRoomMessage>();
    /**
     * 消息状态变化观察者
     */
    Observer<ChatRoomMessage> messageStatusObserver = new Observer<ChatRoomMessage>() {
        @Override
        public void onEvent(ChatRoomMessage message) {
            if (isMyMessage(message)) {
                onMessageStatusChange(message);
            }
        }
    };
//    private MsgAdapter adapter;
    private RoomMsgAdapter roomMsgAdapter;
    /**
     * 消息附件上传/下载进度观察者
     */
    Observer<AttachmentProgress> attachmentProgressObserver = new Observer<AttachmentProgress>() {
        @Override
        public void onEvent(AttachmentProgress progress) {
            onAttachmentProgressChange(progress);
        }
    };

    public ChatRoomMsgListPanel(Container container, View rootView) {
        this.container = container;
        this.rootView = rootView;
        init();
    }

    public void onResume() {
        setEarPhoneMode(UserPreferences.isEarPhoneModeEnable());
    }

    public void onPause() {
        MessageAudioControl.getInstance(container.activity).stopAudio();
    }

    public void onDestroy() {
        registerObservers(false);
    }

    public boolean onBackPressed() {
        uiHandler.removeCallbacks(null);
        MessageAudioControl.getInstance(container.activity).stopAudio(); // 界面返回，停止语音播放
        return false;
    }

    private void init() {
        initListView();
        this.uiHandler = new Handler();
        registerObservers(true);
    }

    private void initListView() {
        roomMsgAdapter = new RoomMsgAdapter(rootView.getContext(), 0, items);
//        adapter = new MsgAdapter(container.activity, items, this);
//        adapter.setEventListener(new MsgItemEventListener());

        messageListView = (ListView) rootView.findViewById(R.id.messageListView);
        messageListView.requestDisallowInterceptTouchEvent(true);

//        messageListView.setMode(AutoRefreshListView.Mode.START);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        // adapter
        messageListView.setAdapter(roomMsgAdapter);

//        messageListView.setListViewEventListener(new MessageListView.OnListViewEventListener() {
//            @Override
//            public void onListViewStartScroll() {
//                container.proxy.shouldCollapseInputPanel();
//            }
//        });
//        messageListView.setOnRefreshListener(new MessageLoader());
    }

    // 刷新消息列表
    public void refreshMessageList() {
        container.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                roomMsgAdapter.notifyDataSetChanged();
            }
        });
    }

    public void scrollToBottom() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListViewUtil.scrollToBottom(messageListView);
            }
        }, 200);
    }

    public void onIncomingMessage(List<ChatRoomMessage> messages) {
        boolean needScrollToBottom = ListViewUtil.isLastMessageVisible(messageListView);
//        boolean needRefresh = false;
//        List<IMMessage> addedListItems = new ArrayList<>(messages.size());
        for (IMMessage message : messages) {
            // 保证显示到界面上的消息，来自同一个聊天室
            if (isMyMessage(message)) {
                saveMessage(message, false);
//                addedListItems.add(message);
//                needRefresh = true;
            }
        }
//        if (needRefresh) {
//            roomMsgAdapter.notifyDataSetChanged();
//        }
        items.addAll(messages);
        roomMsgAdapter.addAll(messages);

        // incoming messages tip
        IMMessage lastMsg = messages.get(messages.size() - 1);
        if (isMyMessage(lastMsg) && needScrollToBottom) {
            ListViewUtil.scrollToBottom(messageListView);
        }
    }

    // 发送消息后，更新本地消息列表
    public void onMsgSend(IMMessage message) {
        // add to listView and refresh
        saveMessage(message, false);
        List<IMMessage> addedListItems = new ArrayList<>(1);
        addedListItems.add(message);

        roomMsgAdapter.notifyDataSetChanged();
        ListViewUtil.scrollToBottom(messageListView);
    }

    private void saveMessage(final List<IMMessage> messageList, boolean addFirst) {
        if (messageList == null || messageList.isEmpty()) {
            return;
        }

        for (IMMessage msg : messageList) {
            saveMessage(msg, addFirst);
        }
    }

    public void saveMessage(final IMMessage message, boolean addFirst) {
//        if (message == null) {
//            return;
//        }
//
//        if (items.size() >= MESSAGE_CAPACITY) {
//            items.poll();
//        }
//
//        if (addFirst) {
//            items.add(0, message);
//        } else {
//            items.add(message);
//        }
    }

    public void addMessage(IMMessage message){
        items.add((ChatRoomMessage) message);
        roomMsgAdapter.add((ChatRoomMessage) message);
        ListViewUtil.scrollToBottom(messageListView);

    }

    /**
     * *************** implements TAdapterDelegate ***************
     */
    @Override
    public int getViewTypeCount() {
        return ChatRoomMsgViewHolderFactory.getViewTypeCount();
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return ChatRoomMsgViewHolderFactory.getViewHolderByType(items.get(position));
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }

    /**
     * ************************* 观察者 ********************************
     */

    private void registerObservers(boolean register) {
        ChatRoomServiceObserver service = NIMClient.getService(ChatRoomServiceObserver.class);
        service.observeMsgStatus(messageStatusObserver, register);
        service.observeAttachmentProgress(attachmentProgressObserver, register);
    }

    private void onMessageStatusChange(IMMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            item.setStatus(message.getStatus());
            item.setAttachStatus(message.getAttachStatus());
            if (item.getAttachment() instanceof AVChatAttachment
                    || item.getAttachment() instanceof AudioAttachment) {
                item.setAttachment(message.getAttachment());
            }
            refreshViewHolderByIndex(index);
        }
    }

    private void onAttachmentProgressChange(AttachmentProgress progress) {
        int index = getItemIndex(progress.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            float value = (float) progress.getTransferred() / (float) progress.getTotal();
//            roomMsgAdapter.putProgress(item, value);
            refreshViewHolderByIndex(index);
        }
    }

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == container.sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(container.account);
    }

    /**
     * 刷新单条消息
     *
     * @param index
     */
    private void refreshViewHolderByIndex(final int index) {
        container.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (index < 0) {
                    return;
                }

                Object tag = ListViewUtil.getViewHolderByIndex(messageListView, index);
                if (tag instanceof MsgViewHolderBase) {
                    MsgViewHolderBase viewHolder = (MsgViewHolderBase) tag;
                    viewHolder.refreshCurrentItem();
                }
            }
        });
    }

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            IMMessage message = items.get(i);
            if (TextUtils.equals(message.getUuid(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    private void setEarPhoneMode(boolean earPhoneMode) {
        UserPreferences.setEarPhoneModeEnable(earPhoneMode);
        MessageAudioControl.getInstance(container.activity).setEarPhoneModeEnable(earPhoneMode);
    }

//    /**
//     * *************** MessageLoader ***************
//     */
//    private class MessageLoader implements AutoRefreshListView.OnRefreshListener {
//
//        private static final int LOAD_MESSAGE_COUNT = 5;
//
//        private IMMessage anchor;
//
//        private boolean firstLoad = true;
//        private RequestCallback<List<ChatRoomMessage>> callback = new RequestCallbackWrapper<List<ChatRoomMessage>>() {
//            @Override
//            public void onResult(int code, List<ChatRoomMessage> messages, Throwable exception) {
//                if (messages != null) {
//                    onMessageLoaded(messages);
//                } else {
//                    messageListView.onRefreshComplete(LOAD_MESSAGE_COUNT, LOAD_MESSAGE_COUNT, false);
//                }
//            }
//        };
//
//        public MessageLoader() {
//            anchor = null;
////            loadFromLocal();
//        }
//
//        private void loadFromLocal() {
//            messageListView.onRefreshStart(AutoRefreshListView.Mode.START);
//            NIMClient.getService(ChatRoomService.class).pullMessageHistory(container.account, anchor().getTime(), LOAD_MESSAGE_COUNT)
//                    .setCallback(callback);
//        }
//
//        private IMMessage anchor() {
//            if (items.size() == 0) {
//                return (anchor == null ? ChatRoomMessageBuilder.createEmptyChatRoomMessage(container.account, 0) : anchor);
//            } else {
//                return items.get(0);
//            }
//        }
//
//        /**
//         * 历史消息加载处理
//         */
//        private void onMessageLoaded(List<ChatRoomMessage> messages) {
//            int count = messages.size();
//
//            if (items.size() > 0) {
//                // 在第一次加载的过程中又收到了新消息，做一下去重
//                for (IMMessage message : messages) {
//                    for (IMMessage item : items) {
//                        if (item.isTheSame(message)) {
//                            items.remove(item);
//                            break;
//                        }
//                    }
//                }
//            }
//
//            List<IMMessage> result = new ArrayList<>();
//            for (IMMessage message : messages) {
//                result.add(message);
//            }
//            saveMessage(result, true);
//
//            // 如果是第一次加载，updateShowTimeItem返回的就是lastShowTimeItem
//            if (firstLoad) {
//                ListViewUtil.scrollToBottom(messageListView);
//            }
//
//            refreshMessageList();
//            messageListView.onRefreshComplete(count, LOAD_MESSAGE_COUNT, true);
//
//            firstLoad = false;
//        }
//
//        /**
//         * *************** OnRefreshListener ***************
//         */
//        @Override
//        public void onRefreshFromStart() {
////            loadFromLocal();
//        }
//
//        @Override
//        public void onRefreshFromEnd() {
//        }
//    }

//    private class MsgItemEventListener implements MsgAdapter.ViewHolderEventListener {
//
//        @Override
//        public void onFailedBtnClick(IMMessage message) {
//            if (message.getDirect() == MsgDirectionEnum.Out) {
//                // 发出的消息，如果是发送失败，直接重发，否则有可能是漫游到的多媒体消息，但文件下载
//                if (message.getStatus() == MsgStatusEnum.fail) {
//                    resendMessage(message); // 重发
//                } else {
//                    if (message.getAttachment() instanceof FileAttachment) {
//                        FileAttachment attachment = (FileAttachment) message.getAttachment();
//                        if (TextUtils.isEmpty(attachment.getPath())
//                                && TextUtils.isEmpty(attachment.getThumbPath())) {
//                            showReDownloadConfirmDlg(message);
//                        }
//                    } else {
//                        resendMessage(message);
//                    }
//                }
//            } else {
//                showReDownloadConfirmDlg(message);
//            }
//        }
//
//        @Override
//        public boolean onViewHolderLongClick(View clickView, View viewHolderView, IMMessage item) {
//            return true;
//        }
//
//        // 重新下载(对话框提示)
//        private void showReDownloadConfirmDlg(final IMMessage message) {
//            EasyAlertDialogHelper.OnDialogActionListener listener = new EasyAlertDialogHelper.OnDialogActionListener() {
//
//                @Override
//                public void doCancelAction() {
//                }
//
//                @Override
//                public void doOkAction() {
//                    // 正常情况收到消息后附件会自动下载。如果下载失败，可调用该接口重新下载
//                    if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment)
//                        NIMClient.getService(ChatRoomService.class).downloadAttachment((ChatRoomMessage) message, true);
//                }
//            };
//
//            final EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(container.activity, null,
//                    container.activity.getString(R.string.repeat_download_message), true, listener);
//            dialog.show();
//        }
//
//        // 重发消息到服务器
//        private void resendMessage(IMMessage message) {
//            // 重置状态为unsent
//            int index = getItemIndex(message.getUuid());
//            if (index >= 0 && index < items.size()) {
//                IMMessage item = items.get(index);
//                item.setStatus(MsgStatusEnum.sending);
//                refreshViewHolderByIndex(index);
//            }
//
//            NIMClient.getService(ChatRoomService.class).sendMessage((ChatRoomMessage) message, true);
//        }
//
//    }

    public void addMsg(){
//        items.add()
    }

}
