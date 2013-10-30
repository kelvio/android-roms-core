package com.kelviomatias.romscore.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;

public class BitmapUtil {

	private static Map<Character, Bitmap> bitmapMap = new HashMap<Character, Bitmap>();

	/**
	 * 
	 * @param context
	 * @param c
	 * @param color
	 * @param upper
	 * @return
	 */
	public static Bitmap getBitmapForCharacter(Context context, Character c,
			int color, boolean upper) {

		if (!bitmapMap.containsKey(c)) {

			Resources r = context.getResources();
			int w = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
			int h = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 80, r.getDisplayMetrics());

			Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

			Canvas canvas = new Canvas(bitmap);

			String gText = "" + c;

			if (upper) {
				gText = gText.toUpperCase();
			}

			// new antialised Paint
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(color);
			canvas.drawRect(0, 0, w, h, paint);

			float scale = 1.0f;
			paint.setColor(Color.WHITE);

			// text size in pixels
			paint.setTextSize((int) (50 * scale));
			// text shadow
			// paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

			// draw text to the Canvas center
			Rect bounds = new Rect();
			paint.getTextBounds(gText, 0, gText.length(), bounds);
			int x = (bitmap.getWidth() - bounds.width()) / 2;
			int y = (bitmap.getHeight() + bounds.height()) / 2;

			canvas.drawText(gText, x * scale, y * scale, paint);
			bitmapMap.put(c, bitmap);
		}

		return bitmapMap.get(c);

	}

}
