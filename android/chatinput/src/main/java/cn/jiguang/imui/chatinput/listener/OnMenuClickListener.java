package cn.jiguang.imui.chatinput.listener;


import android.view.View;

/**
 * Menu items' callbacks
 */
public interface OnMenuClickListener {

    /**
     * Fires when send button is on click.
     *
     * @param input Input content
     * @return boolean
     */
    boolean onSendTextMessage(CharSequence input);

    void onFeatureView(int inputHeight,int showType);
    void onShowKeyboard(int inputHeight,int showType);
    void onPhotoClick(View view);
}