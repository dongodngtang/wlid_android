package net.doyouhike.app.wildbird.ui.view;

import net.doyouhike.app.wildbird.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleView extends RelativeLayout implements OnClickListener {


    private RelativeLayout rightParent;
    private RelativeLayout rootView;
    private ImageView left_image, right_image;
    private TextView title, left_text, right_text;
    private ClickListener listener = null;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public interface ClickListener {
        public void clickLeft();

        public void clickRight();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setPadding() {
        left_image.setPadding(10, 3, 10, 3);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.title, this);

        left_image = (ImageView) this.findViewById(R.id.back);
        right_image = (ImageView) this.findViewById(R.id.right_btn);
        title = (TextView) this.findViewById(R.id.title);
        left_text = (TextView) this.findViewById(R.id.left);
        right_text = (TextView) this.findViewById(R.id.right);
        rightParent = (RelativeLayout) this.findViewById(R.id.title_right_parent);
        rootView = (RelativeLayout) this.findViewById(R.id.title_parent);

        left_image.setOnClickListener(this);
        right_image.setOnClickListener(this);
        left_text.setOnClickListener(this);
        right_text.setOnClickListener(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        int bg=typedArray.getResourceId(R.styleable.TitleView_bg_root, 0);

        if (bg!=0){
            rootView.setBackgroundResource(bg);
        }


        int left_src = typedArray.getResourceId(R.styleable.TitleView_left_src, 0);
        if (left_src != 0) {
            left_image.setImageResource(left_src);
        } else {
            left_image.setVisibility(View.GONE);
        }
        String left = typedArray.getString(R.styleable.TitleView_left_text);
        if (left != null) {
            left_image.setVisibility(View.GONE);
            left_text.setText(left);
        }

        int left_txt_color=typedArray.getColor(R.styleable.TitleView_left_txt_color,0);
        if (left_txt_color!=0){
            left_text.setTextColor(left_txt_color);
        }


        String text = typedArray.getString(R.styleable.TitleView_m_title);
        if (text != null) title.setText(text);
        int title_txt_color=typedArray.getColor(R.styleable.TitleView_title_txt_color,0);
        if (title_txt_color!=0){
            title.setTextColor(title_txt_color);
        }

        int right_src = typedArray.getResourceId(R.styleable.TitleView_right_src, 0);
        if (right_src != 0) {
            right_image.setVisibility(View.VISIBLE);
            right_image.setImageResource(right_src);
        }
        String right = typedArray.getString(R.styleable.TitleView_right_text);
        if (right != null) {
            right_image.setVisibility(View.GONE);
            right_text.setText(right);
        }
        int right_txt_color=typedArray.getColor(R.styleable.TitleView_right_txt_color,0);
        if (right_txt_color!=0){
            right_text.setTextColor(right_txt_color);
        }
        typedArray.recycle();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back:
                if (null != listener) {
                    // 加这个监听为了按返回键时可以处理想要处理的动作
                    listener.clickLeft();
                }
                Context context = v.getContext();
                if (listener == null && (context instanceof Activity)) {
                    ((Activity) context).onBackPressed();
                }
                break;
            case R.id.right_btn:
                if (null != listener) {
                    listener.clickRight();
                }
                break;
            case R.id.title_right_parent:
                if (null != listener) {
                    listener.clickRight();
                }
                break;
            case R.id.left:
                if (null != listener) {
                    listener.clickLeft();
                }
                break;
            case R.id.right:
                if (null != listener) {
                    listener.clickRight();
                }
                break;
        }
    }

    public ImageView getRight_image() {
        return right_image;
    }

    public void showRightImg(boolean show) {
        right_image.setVisibility(show ? VISIBLE : GONE);
    }

    public void showLeftView(boolean show) {
        left_image.setVisibility(show ? VISIBLE : GONE);
    }

    public void setLefImageSrc(int resID) {
        left_image.setImageResource(resID);

    }

    public View setRightView(int resID) {
        rightParent.setOnClickListener(this);
      return View.inflate(rightParent.getContext(), R.layout.layout_me_draft, rightParent);

    }

    public RelativeLayout getRightParent() {
        return rightParent;
    }

    public void setRightView(View view) {
        rightParent.removeAllViews();
        rightParent.addView(view);
    }
}
