package tech.oldes.GooglePlayAssets;

import android.util.Log;

public class Logger {
	public static final String TAG = GooglePlayAssetsExtension.TAG;
	public static final int VERBOSE = 4;

	// Always display error logs...
	public static void e(String tag, String msg) {
		Log.e(TAG, "["+tag+"] "+msg);
	}
	// Warnings when VERBOSE > 0
	public static void w(String tag, String msg) {
		if (VERBOSE > 0) Log.w(TAG, "["+tag+"] "+msg);
	}
	// Infos when VERBOSE > 1
	public static void i(String tag, String msg) {
		if (VERBOSE > 1) Log.i(TAG, "["+tag+"] "+msg);
	}
	// Debugs when VERBOSE > 2
	public static void d(String tag, String msg) {
		if (VERBOSE > 2) Log.d(TAG, "["+tag+"] "+msg);
	}
	// All other (maximum) when VERBOSE > 3
	public static void v(String tag, String msg) {
		if (VERBOSE > 3) Log.v(TAG, "["+tag+"] "+msg);
	}

	public static void exception(String tag, Exception err) {
		e(tag, err.getMessage());
		if (VERBOSE > 1) err.printStackTrace();
		// send reported message for all other exceptions
		GooglePlayAssetsExtension.extensionContext.dispatchStatusEventAsync("", err.getLocalizedMessage());
	}
}
