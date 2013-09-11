package so.zudui.activity.details;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

// 重写此类解决GridView嵌套进ScrollView中显示不正常的问题
public class ParticipantsGridView extends GridView {

	public ParticipantsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ParticipantsGridView(Context context) {
        super(context);
    }
    public ParticipantsGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
