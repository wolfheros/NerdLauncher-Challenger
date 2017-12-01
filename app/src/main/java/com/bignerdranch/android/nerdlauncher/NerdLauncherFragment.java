package com.bignerdranch.android.nerdlauncher;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by james_huker on 11/28/17.
 */

public class NerdLauncherFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public static Fragment newInstance() {
        return new NerdLauncherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater ,ViewGroup parent , Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_nerd_launcher, parent, false);
        mRecyclerView =(RecyclerView) v.findViewById(R.id.fragment_nerd_launcher_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return v;
    }

    private void setupAdapter() {

        // 创建隐式Intent
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 获取PackageManager
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent,0);

        // 对数组中的actiivties进行排序。
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(),b.loadLabel(pm).toString());
            }
        });

        // 设置Adapter
        mRecyclerView.setAdapter(new ActivityAdapter(activities));

        // 输出日志
        Log.i(TAG ,"Found " + activities.size() +" activities");
    }

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ResolveInfo mResolveInfo;
        private TextView mTextView;
        private ImageView mImageView;


        public ActivityHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTextView = (TextView) itemView.findViewById(R.id.launcher_text);
            // 设置监听器
            // mTextView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.launcher_icon);
            // mImageView.setOnClickListener(this);

        }

        public void bindActivity(ResolveInfo resolveInfo) {

            mResolveInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();

            String appName = mResolveInfo.loadLabel(pm).toString();
            Drawable md = mResolveInfo.loadIcon(pm);
            mTextView.setText(appName);
            // 获取应用图标
            //mTextView.setCompoundDrawables(md,null,null,null);
            mImageView.setImageDrawable(md);
        }

        @Override
        public void onClick(View view) {
            ActivityInfo activityInfo = mResolveInfo.activityInfo;

            Intent i = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities) {
            mActivities = activities;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.icon_textview_fragment, parent ,false);

            return new ActivityHolder(view);
        }
        @Override
        public void onBindViewHolder (ActivityHolder activityHolder,int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            activityHolder.bindActivity(resolveInfo);
        }
        @Override
        public int getItemCount() {
            return  mActivities.size();
        }
    }


}
