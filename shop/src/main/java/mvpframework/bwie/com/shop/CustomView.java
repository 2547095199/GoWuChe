package mvpframework.bwie.com.shop;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CZ on 2017/11/17.
 */
public class CustomView extends LinearLayout {
    @Bind(R.id.revserse)
    Button revserse;
    @Bind(R.id.content)
    EditText editText;
    @Bind(R.id.add)
    Button add;
    private int mCount = 1;
    public ClickListener listener;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.customview, null, false);
        ButterKnife.bind(this, view);


        revserse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String content = editText.getText().toString().trim();
                    int count = Integer.valueOf(content);
                    if (count > 1) {
                        mCount = count - 1;
                        editText.setText(mCount + "");
                        if (listener != null) {
                            listener.click(mCount);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String content = editText.getText().toString().trim();
                    int count = Integer.valueOf(content) + 1;

                    mCount = count;
                    editText.setText(count + "");

                    if (listener != null) {
                        listener.click(count);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addView(view);
    }

    public void setEditText(int count){
        editText.setText(count+"");
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public int getCurrentCount() {
        return mCount;
    }

    public interface ClickListener {
        public void click(int count);
    }
}
