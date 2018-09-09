package com.wiser.zxing;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.wiser.library.WISERScanActivity;
import com.wiser.library.view.ViewfinderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanActivity extends WISERScanActivity {

	@BindView(R.id.surface_view) SurfaceView		surfaceView;

	@BindView(R.id.viewfinder_view) ViewfinderView	viewfinderView;

	@Override protected int layoutId() {
		return R.layout.activity_scan;
	}

	@Override protected void initData(@Nullable Bundle savedInstanceState) {
		ButterKnife.bind(this);
		initScan(viewfinderView, surfaceView);
	}

	@Override public void scanSuccess(String json) {
		Toast.makeText(this, json, Toast.LENGTH_LONG).show();
	}

	@Override public void scanFail() {
		Toast.makeText(this, "扫描失败", Toast.LENGTH_LONG).show();
	}

	@OnClick(value = { R.id.scan_back, R.id.scan_photo, R.id.scan_flash }) public void onClickView(View view) {
		switch (view.getId()) {
			case R.id.scan_back:
				this.finish();
				break;
			case R.id.scan_photo:
				photo();
				break;
			case R.id.scan_flash:
				flash();
				break;
		}
	}

	@Override public void applyPermissionFail(int requestCode) {
		super.applyPermissionFail(requestCode);
		Toast.makeText(this, "请到设置页面进行授权", Toast.LENGTH_LONG).show();
	}

	@Override public void applyPermissionSuccess(int requestCode) {
		super.applyPermissionSuccess(requestCode);
	}
}
