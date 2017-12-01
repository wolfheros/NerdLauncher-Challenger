package com.bignerdranch.android.nerdlauncher;

/**
 *  托管activity ， 创建Fragment
 *
 *  */
import android.support.v4.app.Fragment;

public class NerdLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return NerdLauncherFragment.newInstance();
    }
}
