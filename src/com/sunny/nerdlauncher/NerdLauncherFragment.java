package com.sunny.nerdlauncher;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NerdLauncherFragment extends ListFragment {
	
	private static final String TAG = "NerdLauncherFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 向PackageManager查询activity数
		Intent startupIntent = new Intent(Intent.ACTION_MAIN);
		startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		PackageManager pm = getActivity().getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
		
		Log.i(TAG, "I've found " + activities.size() + " activities.");
		
		Collections.sort(activities, new Comparator<ResolveInfo>() {

			@Override
			public int compare(ResolveInfo lhs, ResolveInfo rhs) {
				PackageManager pm = getActivity().getPackageManager();
				return String.CASE_INSENSITIVE_ORDER.
						compare(lhs.loadLabel(pm).toString(), rhs.loadLabel(pm).toString());
			}
		});

		ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(
				getActivity(), android.R.layout.simple_list_item_1, activities) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				PackageManager pm = getActivity().getPackageManager();
				View view = super.getView(position, convertView, parent);
				// Documentation says that simple_list_item_1 is a TextView,
				// so cast it so that you can set its text value.
				TextView tv = (TextView) view;
				ResolveInfo ri = getItem(position);
				tv.setText(ri.loadLabel(pm));
				return view;
			}
		};
		
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ResolveInfo resolveInfo = (ResolveInfo) l.getAdapter().getItem(position);
		ActivityInfo activityInfo = resolveInfo.activityInfo;
		
		if (activityInfo == null) 
			return;
		
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
		// FLAG_ACTIVITY_NEW_TASK标志控制着每个activity仅创建一个任务
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 标记为新任务
		
		startActivity(intent);
	}
	
}
