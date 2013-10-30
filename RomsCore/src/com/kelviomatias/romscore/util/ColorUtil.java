package com.kelviomatias.romscore.util;

import android.graphics.Color;

public class ColorUtil {

	public static int getLetterColor(char c) {
		switch (c) {
		case 'a':
			return Color.rgb(180, 0, 0);
		case 'b':
			return Color.rgb(153, 204, 50);
		case 'c':
			return Color.rgb(50, 153, 204);
		case 'd':
			return Color.rgb(159, 95, 159);
		case 'e':
			return Color.rgb(92, 51, 23);
		case 'f':
			return Color.rgb(159, 95, 159);
		case 'g':
			return Color.rgb(219, 147, 112);
		case 'h':
			return Color.rgb(140, 120, 83);
		case 'i':
			return Color.rgb(217, 135, 25);
		case 'j':
			return Color.rgb(140, 23, 23);
		case 'k':
			return Color.rgb(92, 64, 51);
		case 'l':
			return Color.rgb(111, 66, 66);
		case 'm':
			return Color.rgb(35, 107, 142);
		case 'n':
			return Color.rgb(74, 118, 110);
		case 'o':
			return Color.rgb(112, 147, 219);
		case 'p':
			return Color.rgb(33, 94, 33);
		case 'q':
			return Color.rgb(79, 47, 79);
		case 'r':
			return Color.rgb(135, 31, 120);
		case 's':
			return Color.rgb(227, 155, 53);
		case 't':
			return Color.rgb(143, 143, 189);
		case 'u':
			return Color.rgb(207, 181, 59);
		case 'v':
			return Color.rgb(77, 77, 255);
		case 'w':
			return Color.rgb(53, 141, 78);
		case 'x':
			return Color.rgb(255, 28, 174);
		case 'y':
			return Color.rgb(205, 127, 50);
		case 'z':
			return Color.rgb(82, 127, 118);
		}
		return Color.rgb(77, 77, 77);
	}

	public static int getBrigtherLetterColor(int color) {
		float hsv[] = new float[3];

		Color.colorToHSV(color, hsv);

		return Color.HSVToColor(20, hsv);
	}
	
	public static int getBrigtherLetterColor(int color, int alpha) {
		float hsv[] = new float[3];

		Color.colorToHSV(color, hsv);

		return Color.HSVToColor(alpha, hsv);
	}

}
