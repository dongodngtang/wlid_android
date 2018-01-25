package net.doyouhike.app.wildbird.ui.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.model.event.CheckVersionEvent;
import net.doyouhike.app.wildbird.biz.model.event.CheckoutPageEvent;
import net.doyouhike.app.wildbird.biz.model.event.GetBannerEvent;
import net.doyouhike.app.wildbird.biz.model.event.RequestBirdUpdateEvent;
import net.doyouhike.app.wildbird.ui.base.BaseAppFragmentActivity;
import net.doyouhike.app.wildbird.ui.main.add.AddRecordFragment;
import net.doyouhike.app.wildbird.ui.main.discovery.DiscoveryFragment;
import net.doyouhike.app.wildbird.ui.main.fragment.SearchFragment;
import net.doyouhike.app.wildbird.ui.main.fragment.SearchFragment.VisibleListener;
import net.doyouhike.app.wildbird.ui.main.user.mine.MeFragment;
import net.doyouhike.app.wildbird.util.CheckVersionUtil;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;
import net.doyouhike.app.wildbird.util.ShareUtil;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseAppFragmentActivity implements OnClickListener, VisibleListener {

    public static final String EXIT_MAIN = "exit_main";
    public static final String EXIT_MINE = "exit_mine";
    private LinearLayout main_bottom;
    private TextView[] tabs;
    private int pos = 0;
    private ViewPager viewpager;
    private FragmentPagerAdapter adapter;
    private SearchFragment currentFragment;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void initViewsAndEvents() {

        //  版本检查更新
        EventBus.getDefault().post(new CheckVersionEvent());
        //获取广告
        EventBus.getDefault().post(new GetBannerEvent());

        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
            @Override
            public void onClick(int status) {
                switch (status) {
                    case UpdateStatus.Update:
                        LocalSharePreferences.commit(getApplicationContext(), "database", "");
                        break;
                }
            }
        });
        main_bottom = (LinearLayout) super.findViewById(R.id.main_bottom);
        tabs = new TextView[4];
        tabs[0] = (TextView) super.findViewById(R.id.search_tab);
        tabs[1] = (TextView) super.findViewById(R.id.add_tab);
        tabs[2] = (TextView) super.findViewById(R.id.discovery_tab);
        tabs[3] = (TextView) super.findViewById(R.id.mine_tab);
        viewpager = (ViewPager) super.findViewById(R.id.viewpager);

        for (int i = 0; i < tabs.length; i++)
            tabs[i].setOnClickListener(this);
        tabs[pos].setSelected(true);
        adapter = new PagerAdapter(getSupportFragmentManager());

        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(tabs.length);

        viewpager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                changeTab(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });

        viewpager.setCurrentItem(pos);
        adapter.notifyDataSetChanged();

        //请求更新数据库,放在这里,是为了使数据库完全加载后再去更新操作,放在start页面存在数据库还没复制完的问题
        EventBus.getDefault().post(new RequestBirdUpdateEvent());

        new CheckVersionUtil(this).showUpdateDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareUtil.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int index = 0;
        switch (v.getId()) {
            case R.id.search_tab:
                break;
            case R.id.add_tab:
                index = 1;
                break;
            case R.id.discovery_tab:
                index = 2;
                break;
            case R.id.mine_tab:
                index = 3;
                break;
        }
        changeTab(index);
    }

    private void changeTab(int index) {
        // TODO Auto-generated method stub
        tabs[pos].setSelected(false);
        tabs[index].setSelected(true);
        pos = index;
        viewpager.setCurrentItem(pos, false);
    }

    private long firstTime;

    @Override
    public void onBackPressed() {

        if (pos == 0) {
            if (currentFragment != null && !currentFragment.isHide()) {
                return;
            }
        }

        if (firstTime + 2000 > System.currentTimeMillis()) {
            onFinishApp();
        } else {
            toast("再按一次退出程序");
        }
        firstTime = System.currentTimeMillis();

    }

    /**
     * 两次按下返回键调用
     */
    private void onFinishApp() {
        MyApplication.getInstance().finishApplication();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    public void onEventMainThread(CheckoutPageEvent event) {
        tabs[3].callOnClick();
    }


    @Override
    public void setVisivle(int visibility) {
        // TODO Auto-generated method stub
        main_bottom.setVisibility(visibility);
    }

    class PagerAdapter extends FragmentPagerAdapter {


        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 4;
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 0:
                    return new SearchFragment();
                case 1:
                    return AddRecordFragment.newInstance(false,true,null);
                case 2:
                    return new DiscoveryFragment();
                case 3:
                    return new MeFragment();
            }
            return null;
        }


        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {

            switch (position) {
                case 0:
                    currentFragment = (SearchFragment) object;
                    break;
                default:
                    currentFragment = null;
            }
            super.setPrimaryItem(container, position, object);
        }


    }


}
