package cn.cbsd.dogtag.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class DogPicListView extends ListView {

    public DogPicListView(Context context) {
        super(context);
    }

    public DogPicListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DogPicListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);//这句话的作用 告诉父view，我的单击事件我自行处理，不要阻碍我。
        return super.dispatchTouchEvent(ev);
    }
}
