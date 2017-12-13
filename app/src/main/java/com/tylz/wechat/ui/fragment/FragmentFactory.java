package com.tylz.wechat.ui.fragment;

/**
 * @author cxw
 * @date 2017/12/12
 * @des 主界面4个Fragment工厂
 */

public class FragmentFactory {
    static FragmentFactory sInstance;
    private FragmentFactory(){

    }
    public static FragmentFactory getInstance(){
        if(sInstance == null){
            synchronized (FragmentFactory.class){
                if(sInstance == null){
                    sInstance = new FragmentFactory();
                }
            }
        }
        return sInstance;
    }
    private RecentMessageFragment mRecentMessageFragment;
    private DiscoveryFragment mDiscoveryFragment;
    private MeFragment mMeFragment;
    private ContactsFragment mContactsFragment;
    public RecentMessageFragment getRecentMessageFragment() {
        if (mRecentMessageFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mRecentMessageFragment == null) {
                    mRecentMessageFragment = new RecentMessageFragment();
                }
            }
        }
        return mRecentMessageFragment;
    }

    public ContactsFragment getContactsFragment() {
        if (mContactsFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mContactsFragment == null) {
                    mContactsFragment = new ContactsFragment();
                }
            }
        }
        return mContactsFragment;
    }

    public DiscoveryFragment getDiscoveryFragment() {
        if (mDiscoveryFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mDiscoveryFragment == null) {
                    mDiscoveryFragment = new DiscoveryFragment();
                }
            }
        }
        return mDiscoveryFragment;
    }

    public MeFragment getMeFragment() {
        if (mMeFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                }
            }
        }
        return mMeFragment;
    }
}
