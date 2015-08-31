package com.sunny.nerdlauncher;

import android.support.v4.app.Fragment;

public class NerdLauncherActivity extends SingleCrimeActivity {

	@Override
	protected Fragment createFragment() {
		return new NerdLauncherFragment();
	}

}
