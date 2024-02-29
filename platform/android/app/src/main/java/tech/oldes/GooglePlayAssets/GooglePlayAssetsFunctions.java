//   ____  __   __        ______        __
//  / __ \/ /__/ /__ ___ /_  __/__ ____/ /
// / /_/ / / _  / -_|_-<_ / / / -_) __/ _ \
// \____/_/\_,_/\__/___(@)_/  \__/\__/_// /
//  ~~~ oldes.huhuman at gmail.com ~~~ /_/
//
// SPDX-License-Identifier: Apache-2.0

package tech.oldes.GooglePlayAssets;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class GooglePlayAssetsFunctions
{
	static public class Init implements FREFunction {
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			Logger.i("Init", "start");
			try {
				GooglePlayAssetsExtension.appContext = freContext.getActivity().getApplicationContext();
			} catch (Exception err) {
				Logger.exception("Init", err);
			}
			return null;
		}
	}

	static public class NativeVersion implements FREFunction {
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject result = null;
			try {
				result = FREObject.newObject( GooglePlayAssetsExtension.VERSION );
			} catch (Exception err) {
				Logger.exception("NativeVersion", err);
			}
			return result;
		}
	}

	static public class GetObbDir implements FREFunction {
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject result = null;
			try {
				result = FREObject.newObject( GooglePlayAssetsExtension.appContext.getObbDir().getAbsolutePath() );
			} catch (Exception err) {
				Logger.exception("GetObbDir", err);
			}
			return result;
		}
	}

	static public class GetMainOBBFile implements FREFunction {
		private static final String TAG = "GetMainOBBFile";
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject result = null;
			try {
				FREObject input = args[0];
				int packageVersion = input.getAsInt();
				Logger.d(TAG, "version: " + packageVersion);
				String packageName = GooglePlayAssetsExtension.appContext.getPackageName();
				Logger.d(TAG, "name: " + packageName);
				File file = new File(GooglePlayAssetsExtension.appContext.getObbDir()+"/main." + packageVersion + "." + packageName + ".obb");
				Logger.d(TAG, "file: " + file);
				if (file.exists()) {
					result = FREObject.newObject( file.getAbsolutePath() );
				}
			} catch (Exception err) {
				Logger.exception(TAG, err);
			}
			return result;
		}
	}



	static public class InitAssetDelivery implements FREFunction {
		private static final String TAG = "InitAssetDelivery";
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject returnValue = null;
			try {
				GooglePlayAssetsExtensionContext ctx = (GooglePlayAssetsExtensionContext)freContext;
				Logger.i(TAG, "start");
				Activity activity = ctx.getActivity();
				Logger.d(TAG, "activity: "+activity);
				Context applicationContext = activity.getApplicationContext();
				Logger.d(TAG, "applicationContext: "+applicationContext);
				GooglePlayAssetsManager instance = GooglePlayAssetsManager.getInstance();
				Logger.d(TAG, "instance: "+instance);
				boolean ret = instance.setContext(applicationContext, activity, freContext);
				Logger.d(TAG, "ret: "+ret);
				returnValue = FREObject.newObject(ret);
			} catch (Exception err) {
				Logger.exception("InitAssetDelivery", err);
			}
			Logger.d(TAG, "end");
			return returnValue;
		}
	}

	static public class OpenInstallTimeAsset implements FREFunction {
		public static final String TAG = "OpenInstallTimeAsset";
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject returnValue = null;
			Logger.i(TAG, "start");
			try {
				FREObject input = args[0];
				String assetFileName = input.getAsString();
				Logger.d(TAG, "Input value is " + assetFileName);
				Activity activity = freContext.getActivity();
				if (activity != null) {
					Context applicationContext = activity.getApplicationContext();
					if (applicationContext == null) {
						Logger.w(TAG, "applicationContext is null");
					} else {
						GooglePlayAssetsManager instance = GooglePlayAssetsManager.getInstance();
						int assetRef = instance.openInstallTimeDeliveryAsset(assetFileName);
						Logger.d(TAG, "return value " + assetRef);
						returnValue = FREObject.newObject(assetRef);
					}
				} else {
					Logger.w(TAG, "Activity is null");
				}
			} catch (Exception err) {
				Logger.exception("OpenInstallTimeAsset", err);
			}

			Logger.d(TAG, "end");
			return returnValue;
		}
	}

	static public class FetchAssetsPack implements FREFunction {
		private static final String TAG = "FetchAssetsPack";
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject returnValue = null;
			Logger.i(TAG, "start");
			try {
				FREObject input = args[0];
				List<String> assertName = Collections.singletonList(input.getAsString());
				Logger.d(TAG, "Input value is " + assertName);
				int fetchStatus = GooglePlayAssetsManager.getInstance().fetch(assertName);
				Logger.d(TAG, "Output value is " + fetchStatus);
				returnValue = FREObject.newObject(fetchStatus);
			} catch (Exception err) {
				Logger.exception("FetchAssetsPack", err);
			}
			Logger.d(TAG, "end");
			return returnValue;
		}
	}

	static public class GetAssetAbsolutePath implements FREFunction {
		private static final String TAG = "GetAssetAbsolutePath";
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject returnValue = null;
			Logger.i(TAG, "start");
			try {
				String assetPack = args[0].getAsString();
				String relativePath = args[1].getAsString();
				Logger.d(TAG, "assetPack: " + assetPack);
				Logger.d(TAG, "relativePath: " + relativePath);
				String absoluteAssetFolderPath = GooglePlayAssetsManager.getInstance().getAbsoluteAssetPath(assetPack, relativePath);
				Logger.d(TAG, "absoluteAssetPath: " + absoluteAssetFolderPath);
				returnValue = FREObject.newObject(absoluteAssetFolderPath);
			} catch (Exception err) {
				Logger.exception("GetAssetAbsolutePath", err);
			}
			Logger.d(TAG, "end");
			return returnValue;
		}
	}

	static public class GetAssetPackLocation implements FREFunction {
		private static final String TAG = "GetAssetPackLocation";
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject returnValue = null;
			Logger.i(TAG, "start");
			try {
				String assetPack = args[0].getAsString();
				Logger.d(TAG, "assetPack: " + assetPack);
				String location = GooglePlayAssetsManager.getInstance().getAssetPackLocation(assetPack);
				returnValue = FREObject.newObject(location);
			} catch (Exception err) {
				Logger.exception("GetAssetPackLocation", err);
			}
			Logger.d(TAG, "end");
			return returnValue;
		}
	}

	static public class GetAssetPackStatus implements FREFunction {
		private static final String TAG = "GetAssetPackStatus";
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject returnValue = null;
			Logger.i(TAG, "start");
			try {
				FREObject input = args[0];
				String assetPack = input.getAsString();
				Logger.d(TAG, "assetPack: " + assetPack);
				int status = GooglePlayAssetsManager.getInstance().getStatus(assetPack);
				returnValue = FREObject.newObject(status);
			} catch (Exception err) {
				Logger.exception("GetAssetPackStatus", err);
			}
			Logger.d(TAG, "end");
			return returnValue;
		}
	}

	static public class GetBytesDownloaded implements FREFunction {
		private static final String TAG = "GetBytesDownloaded";
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject returnValue = null;
			Logger.i(TAG, "start");
			try {
				FREObject input = args[0];
				String assetPack = input.getAsString();
				Logger.d(TAG, "assetPack: " + assetPack);
				long bytesBytes = GooglePlayAssetsManager.getInstance().byteDownloaded(assetPack);
				returnValue = FREObject.newObject((double)bytesBytes);
			} catch (Exception err) {
				Logger.exception("GetBytesDownloaded", err);
			}
			Logger.d(TAG, "end");
			return returnValue;
		}
	}
	static public class GetTotalBytesToDownLoad implements FREFunction {
		private static final String TAG = "GetTotalBytesToDownLoad";
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject returnValue = null;
			Logger.i(TAG, "start");
			try {
				FREObject input = args[0];
				String assetPack = input.getAsString();
				Logger.d(TAG, "assetPack: " + assetPack);
				long totalBytes = GooglePlayAssetsManager.getInstance().totalBytesToDownload(assetPack);
				returnValue = FREObject.newObject((double)totalBytes);
			} catch (Exception err) {
				Logger.exception("GetTotalBytesToDownLoad", err);
			}
			Logger.d(TAG, "end");
			return returnValue;
		}
	}

	static public class GetTransferProgressPercentage implements FREFunction {
		private static final String TAG = "GetTransferProgressPercentage";
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject returnValue = null;
			Logger.i(TAG, "start");
			try {
				FREObject input = args[0];
				String assetPack = input.getAsString();
				Logger.d(TAG, "assetPack: " + assetPack);
				int percentage = GooglePlayAssetsManager.getInstance().transferProgressPercentage(assetPack);
				returnValue = FREObject.newObject(percentage);
			} catch (Exception err) {
				Logger.exception("GetTransferProgressPercentage", err);
			}
			Logger.d(TAG, "end");
			return returnValue;
		}
	}

	static public class ShowCellularDataConfirmation implements FREFunction {
		private static final String TAG = "ShowCellularDataConfirmation";
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject returnValue = null;
			Logger.i(TAG, "start");
			try {
				FREObject input = args[0];
				String assetPack = input.getAsString();
				Logger.d(TAG, "assetPack: " + assetPack);
				boolean requestStatus = GooglePlayAssetsManager.getInstance().showCellularDataConfirmation(assetPack);
				returnValue = FREObject.newObject(requestStatus);
			} catch (Exception err) {
				Logger.exception("ShowCellularDataConfirmation", err);
			}
			Logger.d(TAG, "end");
			return returnValue;
		}
	}

	static public class TestException implements FREFunction {
		// just to test the exception event quickly...
		private static final String TAG = "TestException";
		@Override
		public FREObject call(FREContext freContext, FREObject[] args) {
			FREObject result = null;
			try {
				FREObject input = args[0];
				int status = input.getAsInt();
				if (status < 0) {
					result = FREObject.newObject(true);
					GooglePlayAssetsExtension.extensionContext.dispatchStatusEventAsync("AssetPackException", String.valueOf(status));
				}
			} catch (Exception err) {
				Logger.exception(TAG, err);
			}
			return result;
		}
	}
}
