package cn.jiguang.imui.messagelist;


import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.UIImplementation;
import com.facebook.react.uimanager.UIManagerModule;
import com.popup.tool.PopupUtil;

import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.commons.models.IMediaFile;
import cn.jiguang.imui.messagelist.module.RCTMediaFile;
import cn.jiguang.imui.messagelist.module.RCTMember;
import cn.jiguang.imui.messagelist.module.RCTMessage;
import cn.jiguang.imui.utils.PhotoViewPagerViewUtil;

import static cn.jiguang.imui.messagelist.MessageUtil.configChatInput;
import static cn.jiguang.imui.messagelist.MessageUtil.configMessage;


public class AuroraIMUIModule extends ReactContextBaseJavaModule {


    private final String REACT_MSG_LIST_MODULE = "AuroraIMUIModule";

    public AuroraIMUIModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return REACT_MSG_LIST_MODULE;
    }

    @Override
    public void initialize() {
        super.initialize();
        UIImplementation t;
        UIManagerModule a;
    }

    @ReactMethod
    public void appendMessages(ReadableArray messages) {
        String[] rctMessages = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            RCTMessage rctMessage = configMessage(messages.getMap(i));
            rctMessages[i] = rctMessage.toString();
        }
        Intent intent = new Intent();
        intent.setAction(ReactMsgListManager.RCT_APPEND_MESSAGES_ACTION);
        intent.putExtra("messages", rctMessages);
        getReactApplicationContext().sendBroadcast(intent);
    }

    @ReactMethod
    public void updateMessage(ReadableMap message) {
        RCTMessage rctMessage = configMessage(message);
        Intent intent = new Intent();
        intent.setAction(ReactMsgListManager.RCT_UPDATE_MESSAGE_ACTION);
        intent.putExtra("message", rctMessage.toString());
        getReactApplicationContext().sendBroadcast(intent);
    }

    @ReactMethod
    public void insertMessagesToTop(ReadableArray messages) {
        String[] rctMessages = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            RCTMessage rctMessage = configMessage(messages.getMap(i));
            rctMessages[i] = rctMessage.toString();
        }
        Intent intent = new Intent();
        intent.setAction(ReactMsgListManager.RCT_INSERT_MESSAGES_ACTION);
        intent.putExtra("messages", rctMessages);
        getReactApplicationContext().sendBroadcast(intent);
    }

    @ReactMethod
    public void insertMessage(ReadableMap message) {
    }

    @ReactMethod
    public void deleteMessage(ReadableArray messages) {
        String[] rctMessages = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            ReadableMap item = messages.getMap(i);
            if (item.hasKey(MessageConstant.Message.MSG_ID)) {
                String id = item.getString(MessageConstant.Message.MSG_ID);
                rctMessages[i] = id;
            }
        }
        Intent intent = new Intent();
        intent.setAction(ReactMsgListManager.RCT_DELETE_MESSAGES_ACTION);
        intent.putExtra("messages", rctMessages);
        getReactApplicationContext().sendBroadcast(intent);
    }

    @ReactMethod
    public void clearMessage() {
        Intent intent = new Intent();
        intent.setAction(ReactMsgListManager.RCT_CLEAR_MESSAGES_ACTION);
        getReactApplicationContext().sendBroadcast(intent);
    }

    @ReactMethod
    public void stopPlayVoice() {
        Log.w(getClass().getName(), "stopPlayVoice");
        Intent intent = new Intent();
        intent.setAction(ReactMsgListManager.RCT_STOP_PLAY_VOICE_ACTION);
        getReactApplicationContext().sendBroadcast(intent);
    }

    @ReactMethod
    public void scrollToBottom() {
        Intent intent = new Intent();
        intent.setAction(ReactMsgListManager.RCT_SCROLL_TO_BOTTOM_ACTION);
        getReactApplicationContext().sendBroadcast(intent);
    }

    @ReactMethod
    public void getAitMember(ReadableMap member) {

        RCTMember chatInput = configChatInput(member);
        Intent intent = new Intent();
        intent.setAction(ReactChatInputManager.RCT_AIT_MEMBERS_ACTION);
        intent.putExtra(ReactChatInputManager.RCT_DATA, chatInput.toString());
        getReactApplicationContext().sendBroadcast(intent);
    }

    @ReactMethod
    public void showImageView(final ReadableArray list, final int index) {
        this.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PhotoViewPagerViewUtil.show(getCurrentActivity(),getImageList(list), index, longClickListener);
            }
        });
    }

    private List<IMediaFile> getImageList(ReadableArray list) {
        List<IMediaFile> imageList = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            String uri = list.getMap(i).getString("uri");
            IMediaFile mediaFile = null;
            String lUri = uri.toLowerCase();
            if (lUri.startsWith("http://") || lUri.startsWith("https://")) {
                mediaFile = new RCTMediaFile(null, null, uri);
            }else{
                mediaFile = new RCTMediaFile(uri, uri,null);
            }
            imageList.add(mediaFile);
        }
        return imageList;
    }

    private PhotoViewPagerViewUtil.IPhotoLongClickListener longClickListener = new PhotoViewPagerViewUtil.IPhotoLongClickListener() {
        @Override
        public boolean onClick(final Dialog dialog, View v, final IMediaFile mediaFile) {
            List<String> list = new ArrayList<>();
            list.add("保存图片");
            list.add("取消");
            PopupUtil.showDialog(getCurrentActivity(), null, list, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        PhotoViewPagerViewUtil.saveImageToAlbum(mediaFile, getCurrentActivity());
                    } else if (position == 1) {
                        dialog.dismiss();
                    }
                }
            });
            return false;
        }
    };
}
