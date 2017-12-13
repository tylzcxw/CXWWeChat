package com.tylz.wechat.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tylz.wechat.ui.base.BaseFragment;

import java.util.List;

/**
 * @author cxw
 * @date 2017/12/12
 * @des 通用的ViewPager适配器(FragmentPagerAdapter)
 */

public class CommonFragmentPagerAdapter  extends FragmentPagerAdapter{
    public static int MAIN_VIEW_PAGER = 1;//主界面的ViewPager
    private int mViewPagerType =  0;
    public String[] mMainViewPagerTitle = null;
    private List<BaseFragment> mFragments;

    public CommonFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        mFragments = fragments;
    }
    public CommonFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments,int viewPagerType) {
        super(fm);
        mViewPagerType = viewPagerType;
        mFragments = fragments;
    }
    @Override
    public Fragment getItem(int position) {
        if(mFragments != null){
            return mFragments.get(position);
        }
        return null;

    }

    @Override
    public int getCount() {
        if(mFragments != null){
            return mFragments.size();
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
