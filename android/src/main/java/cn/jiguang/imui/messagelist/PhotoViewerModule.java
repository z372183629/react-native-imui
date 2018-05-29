package cn.jiguang.imui.messagelist;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;

import com.LogUtil;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.popup.tool.PopupUtil;

import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.commons.models.IMediaFile;
import cn.jiguang.imui.utils.PhotoViewPagerViewUtil;

public class PhotoViewerModule extends ReactContextBaseJavaModule {
    private ReactContext mContext;

    public PhotoViewerModule(ReactApplicationContext context) {
        super(context);
        mContext = context;
    }

    @Override
    public String getName() {
        return "PhotoViewPager";
    }

    @ReactMethod
    public void show(ReadableArray list, final int curlIndex) {
        final List<IMediaFile> imgList = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            MediaFile mediaFile = new MediaFile(list.getString(i));
            imgList.add(mediaFile);
        }

        mContext.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PhotoViewPagerViewUtil.show(mContext.getCurrentActivity(), imgList, curlIndex, longClickListener);
            }
        });
    }

    private PhotoViewPagerViewUtil.IPhotoLongClickListener longClickListener = new PhotoViewPagerViewUtil.IPhotoLongClickListener() {
        @Override
        public boolean onClick(final Dialog dialog, View v, final IMediaFile mediaFile) {
            String code = null;
            try {
                code = DecodeUtil.parseQRcodeBitmap(mediaFile.getThumbPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<String> list = new ArrayList<>();
            list.add("保存图片");
            list.add("取消");
            PopupUtil.showDialog(mContext.getCurrentActivity(), null, list, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        PhotoViewPagerViewUtil.saveImageToAlbum(mediaFile, mContext);
                    } else if (position == 1) {
                        dialog.dismiss();
                    }
                }
            });
            return false;
        }
    };

    private class MediaFile implements IMediaFile {
        private String mPath = null;
        private String mUrl = null;

        public MediaFile(String url) {
            final String lUrl = url.toLowerCase();
            if (lUrl.startsWith("http://") || lUrl.startsWith("https://")) {
                mUrl = url;
            }
        }

        public void setId(String id) {

        }

        public String getId() {
            return null;
        }

        public String getHeight() {
            return null;
        }

        public String getWidth() {
            return null;
        }

        public String getDisplayName() {
            return null;
        }

        public long getDuration() {
            return 0;
        }

        public String getThumbPath() {
            return mPath;
        }

        public String getPath() {
            return mPath;
        }


        public String getUrl() {
            return mUrl;
        }
    }

    ;
}
