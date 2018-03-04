package com.panda.live.pandalive.Utils;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Android Studio on 1/31/2018.
 */

public class ViewFindUtils {
    /**
     * ViewHolderCụm từ ngắn gọn, để tránh các định nghĩa trùng lặp ViewHolder adapter, giảm lượng sử dụng mã:
     *
     * <pre>
     * if (convertView == null)
     * {
     * 	convertView = View.inflate(context, R.layout.ad_demo, null);
     * }
     * TextView tv_demo = ViewHolderUtils.get(convertView, R.id.tv_demo);
     * ImageView iv_demo = ViewHolderUtils.get(convertView, R.id.iv_demo);
     * </pre>
     */
    public static <T extends View> T hold(View view, int id)
    {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if (viewHolder == null)
        {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);

        if (childView == null)
        {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        return (T) childView;
    }

    /**
     * Thay thế phương pháp findviewById
     */
    public static <T extends View> T find(View view, int id)
    {
        return (T) view.findViewById(id);
    }
}
