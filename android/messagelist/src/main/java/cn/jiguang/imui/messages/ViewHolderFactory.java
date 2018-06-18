package cn.jiguang.imui.messages;

import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.jiguang.imui.R;

public class ViewHolderFactory {
    private static ViewHolderFactory instance = null;

    private ViewHolderFactory() {
        mSparseArray = new SparseArray<>();
    }

    public static ViewHolderFactory getInstance() {
        if (instance == null)
            instance = new ViewHolderFactory();
        return instance;
    }

    private SparseArray<List<View>> mSparseArray;

    public void createCache(final LayoutInflater layoutInflater, final ViewGroup viewGroup) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                createMore(layoutInflater, viewGroup, R.layout.item_receive_txt, 12);
                createMore(layoutInflater, viewGroup, R.layout.item_send_text, 12);

                createMore(layoutInflater, viewGroup, R.layout.item_receive_photo, 5);
                createMore(layoutInflater, viewGroup, R.layout.item_send_photo, 5);

                createMore(layoutInflater, viewGroup, R.layout.item_receive_link, 5);
                createMore(layoutInflater, viewGroup, R.layout.item_send_link, 5);
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    private synchronized void createMore(final LayoutInflater layoutInflater, final ViewGroup viewGroup, @LayoutRes int res, int max) {
        List<View> list = mSparseArray.get(res);
        if (list == null) {
            list = Collections.synchronizedList(new LinkedList<View>());
            mSparseArray.put(res, list);
        }

        int count = max - list.size();
        for (int i = 0; i < count; i++) {
            list.add(layoutInflater.inflate(res, viewGroup, false));
        }

    }

    public synchronized View getView(LayoutInflater layoutInflater, ViewGroup viewGroup, @LayoutRes int res) {
        List<View> list = mSparseArray.get(res);
        if (list == null || list.size() == 0) {
            createMore(layoutInflater, viewGroup, res, 1);
        }

        int i = list.size() - 1;
        View view = list.get(i);
        list.remove(i);

        return view;
    }
}
