package com.wiser.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wiser
 * @version 版本
 */
public class WISERQRCode {

	/**
	 * 创建二维码
	 *
	 * @param url
	 * @param mID
	 * @param dfId
	 * @param photo
	 * @return
	 */
	public static void createQRCodeBitmapForUrl(final FragmentActivity activity, String url, final String mID, final int dfId, final ImageView photo, final boolean isAddLogo) {
		Glide.with(activity).load(url).asBitmap().into(new ImageViewTarget<Bitmap>(photo) {

			@Override protected void setResource(Bitmap resource) {
				photo.setImageBitmap(createQRImage(activity, mID, resource, isAddLogo));
			}

			@Override public void onLoadStarted(Drawable placeholder) {
				super.onLoadStarted(placeholder);
				photo.setImageBitmap(createQRImage(activity, mID, null, isAddLogo));
			}

			@Override public void onLoadFailed(Exception e, Drawable errorDrawable) {
				super.onLoadFailed(e, errorDrawable);
				photo.setImageBitmap(createQRImage(activity, mID, BitmapFactory.decodeResource(activity.getResources(), dfId), isAddLogo));
			}

			@Override public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
				super.onResourceReady(resource, glideAnimation);
				photo.setImageBitmap(createQRImage(activity, mID, resource, isAddLogo));
			}
		});
	}

	/**
	 * 生成二维码Bitmap
	 *
	 * @param logoBm
	 *            二维码中心的Logo图标（可以为null）
	 * @return 合成后的bitmap
	 */
	public static Bitmap createQRImage(FragmentActivity activity, String data, Bitmap logoBm, boolean isAddLogo) {

		try {

			if (data == null || "".equals(data)) {
				return null;
			}

			int widthPix = activity.getWindowManager().getDefaultDisplay().getWidth();
			widthPix = widthPix / 5 * 3;
			int heightPix = widthPix;

			// 配置参数
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 容错级别
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 设置空白边距的宽度
			hints.put(EncodeHintType.MARGIN, 3); // default is 4

			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
			int[] pixels = new int[widthPix * heightPix];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < heightPix; y++) {
				for (int x = 0; x < widthPix; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * widthPix + x] = 0xff000000;
					} else {
						pixels[y * widthPix + x] = 0xffffffff;
					}
				}
			}

			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

			if (isAddLogo) {
				if (logoBm != null) {
					bitmap = addLogo(bitmap, logoBm);
				}
			}

			return bitmap;
			// 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
			// return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new
			// FileOutputStream(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 在二维码中间添加Logo图案
	 */
	private static Bitmap addLogo(Bitmap src, Bitmap logo) {
		if (src == null) {
			return null;
		}

		if (logo == null) {
			return src;
		}

		// 获取图片的宽高
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();

		if (srcWidth == 0 || srcHeight == 0) {
			return null;
		}

		if (logoWidth == 0 || logoHeight == 0) {
			return src;
		}

		// logo大小为二维码整体大小的1/5
		float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(src, 0, 0, null);
			canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
			canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

			canvas.save();
			canvas.restore();
		} catch (Exception e) {
			bitmap = null;
			e.getStackTrace();
		}

		return bitmap;
	}

}
