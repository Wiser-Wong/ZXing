package com.wiser.zxing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.wiser.library.WISERQRCode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QrActivity extends AppCompatActivity {

	@BindView(R.id.iv_qr) ImageView ivQr;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);

		ButterKnife.bind(this);

		WISERQRCode.createQRCodeBitmapForUrl(this, "", "Wiser", R.mipmap.ic_launcher, ivQr, true);
	}
}
