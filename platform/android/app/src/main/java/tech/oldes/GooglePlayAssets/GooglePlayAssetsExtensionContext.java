//   ____  __   __        ______        __
//  / __ \/ /__/ /__ ___ /_  __/__ ____/ /
// / /_/ / / _  / -_|_-<_ / / / -_) __/ _ \
// \____/_/\_,_/\__/___(@)_/  \__/\__/_// /
//  ~~~ oldes.huhuman at gmail.com ~~~ /_/
//
// SPDX-License-Identifier: Apache-2.0

package tech.oldes.GooglePlayAssets;

//import static tech.oldes.playassets.PlayAssetsFunctions.*;

import java.util.HashMap;
import java.util.Map;
import android.util.Log;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;


public class GooglePlayAssetsExtensionContext extends FREContext
{
	public GooglePlayAssetsExtensionContext()
	{
	}

	@Override
	public void dispose() {
		if(GooglePlayAssetsExtension.VERBOSE > 2) Log.i(GooglePlayAssetsExtension.TAG,"Context disposed.");
	}

	@Override
	public Map<String, FREFunction> getFunctions() {
		Map<String, FREFunction> functions = new HashMap<>();
		functions.put("init", new GooglePlayAssetsFunctions.Init());
		functions.put("nativeVersion", new GooglePlayAssetsFunctions.NativeVersion());

		functions.put("getObbDir", new GooglePlayAssetsFunctions.GetObbDir());
		functions.put("getMainOBBFile", new GooglePlayAssetsFunctions.GetMainOBBFile());

		functions.put("initAssetDelivery", new GooglePlayAssetsFunctions.InitAssetDelivery());
		functions.put("openInstallTimeAsset", new GooglePlayAssetsFunctions.OpenInstallTimeAsset());
		functions.put("fetchAssetsPack", new GooglePlayAssetsFunctions.FetchAssetsPack());
		functions.put("getAssetAbsolutePath", new GooglePlayAssetsFunctions.GetAssetAbsolutePath());
		functions.put("getAssetPackLocation", new GooglePlayAssetsFunctions.GetAssetPackLocation());
		functions.put("getAssetPackStatus", new GooglePlayAssetsFunctions.GetAssetPackStatus());
		functions.put("getBytesDownloaded", new GooglePlayAssetsFunctions.GetBytesDownloaded());
		functions.put("getTotalBytesToDownLoad", new GooglePlayAssetsFunctions.GetTotalBytesToDownLoad());
		functions.put("getTransferProgressPercentage", new GooglePlayAssetsFunctions.GetTransferProgressPercentage());
		functions.put("showConfirmationDialog", new GooglePlayAssetsFunctions.ShowConfirmationDialog());

		functions.put("testException", new GooglePlayAssetsFunctions.TestException());
		return functions;
	}
}
