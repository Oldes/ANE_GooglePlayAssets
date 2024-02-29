//   ____  __   __        ______        __
//  / __ \/ /__/ /__ ___ /_  __/__ ____/ /
// / /_/ / / _  / -_|_-<_ / / / -_) __/ _ \
// \____/_/\_,_/\__/___(@)_/  \__/\__/_// /
//  ~~~ oldes.huhuman at gmail.com ~~~ /_/
//
// SPDX-License-Identifier: Apache-2.0

package tech.oldes.GooglePlayAssets;

import android.content.Context;
import android.util.Log;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;

public class GooglePlayAssetsExtension implements FREExtension
{
	public static final String TAG = "ANE_GooglePlayAssets";
	public static final String VERSION = "1.0.0 (Asset: 2.2.0)";
	public static final int VERBOSE = 1;

	public static GooglePlayAssetsExtensionContext extensionContext;
	public static Context appContext;

	@Override
	public FREContext createContext(String contextType) {
		if(VERBOSE > 2) Log.i(TAG, "createContext");
		try {
			extensionContext = new GooglePlayAssetsExtensionContext();
		} catch (Exception err) {
			Logger.exception("createContext", err);
		}
		return extensionContext;
	}

	@Override
	public void dispose() {
		if(VERBOSE > 2) Log.i(TAG, "Extension disposed.");
		appContext = null;
		extensionContext = null;
	}

	@Override
	public void initialize() {
		if(VERBOSE > 2) Log.i(TAG, "Extension initialized.");
	}
}
