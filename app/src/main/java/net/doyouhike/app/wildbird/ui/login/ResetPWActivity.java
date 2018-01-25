package net.doyouhike.app.wildbird.ui.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.base.BaseAppFragmentActivity;

@SuppressWarnings("deprecation")
public class ResetPWActivity extends BaseAppFragmentActivity {

	private ViewPager mViewPager;

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.resetpw;
	}

	@Override
	protected void initViewsAndEvents() {

		initView();
	}

	private void initView() {
		mViewPager=(ViewPager)findViewById(R.id.vp_respw_act_content);
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		PagerSlidingTabStrip tabs=(PagerSlidingTabStrip)findViewById(R.id.ts_respw_act_tabs);
		tabs.setViewPager(mViewPager);
	}


	class MyPagerAdapter extends FragmentPagerAdapter{
		private final String[] TITLES={"通过手机找回","通过邮箱找回"};
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position){
				case 1:
					return new ResetPwEmailFragment();
				default:
					return new ResetPwMobileFragment();
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return 2;
		}
	}
}
