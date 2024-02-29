package tech.oldes.GooglePlayAssets;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.adobe.fre.FREContext;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.play.core.assetpacks.AssetLocation;
import com.google.android.play.core.assetpacks.AssetPackException;
import com.google.android.play.core.assetpacks.AssetPackLocation;
import com.google.android.play.core.assetpacks.AssetPackManager;
import com.google.android.play.core.assetpacks.AssetPackManagerFactory;
import com.google.android.play.core.assetpacks.AssetPackState;
import com.google.android.play.core.assetpacks.AssetPackStateUpdateListener;
import com.google.android.play.core.assetpacks.AssetPackStates;
import com.google.android.play.core.assetpacks.model.AssetPackStatus;
import com.google.android.play.core.assetpacks.model.AssetPackStorageMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GooglePlayAssetsManager implements AssetPackStateUpdateListener {
	private static GooglePlayAssetsManager playAssertInstance = null;
	AssetPackManager assetPackManager;
	Task<AssetPackStates> assetPackStatesTask = null;
	private Context mContext;
	private FREContext mExtCtx;
	private static final String TAG = "GooglePlayAssetsManager";
	ArrayList<InputStream> mStreams = new ArrayList<>();
	Map<String, AssetPackState> mAssetPackStateMap = null;

	private GooglePlayAssetsManager() {
		this.mAssetPackStateMap = new HashMap<>();
	}

	public static GooglePlayAssetsManager getInstance() {
		//Logger.d(TAG, "getInstance() start\n");
		if (playAssertInstance == null) {
			playAssertInstance = new GooglePlayAssetsManager();
		}
		//Logger.d(TAG, "getInstance() end");
		return playAssertInstance;
	}

	public boolean setContext(Context context, Activity mActivity, FREContext freContext) {
		Logger.d(TAG, "setContext()");
		try {
			this.mContext = context;
			this.mExtCtx = freContext;
			this.assetPackManager = AssetPackManagerFactory.getInstance(mActivity.getApplicationContext());
			this.assetPackManager.registerListener(this);
		} catch (Exception err) {
			Logger.exception("setContext", err);
			return false;
		}
		Logger.d(TAG, "setContext() end");
		return true;
	}

	public int openInstallTimeDeliveryAsset(String assetName) throws Exception {
		Logger.d(TAG, "openInstallTimeDeliveryAsset");
		AssetManager assetManager = this.mContext.getAssets();
		int nStreamID = 0;
		InputStream is = assetManager.open(assetName);

		this.mStreams.add(is);
		nStreamID = this.mStreams.size();
		if (nStreamID > 134217712) {
			Log.w("AdobeAIR", "Play Asset Delivery: too many asset files opened");
		}

		Logger.d(TAG, "openInstallTimeDeliveryAssert end -> stream ID " + nStreamID);
		return nStreamID;
	}

	public void getPackStates(final String assetPackName) {
		Logger.d(TAG, "getPackStates()");
		this.assetPackManager.getPackStates(Collections.singletonList(assetPackName)).addOnCompleteListener(new OnCompleteListener<AssetPackStates>() {
			public void onComplete(@NonNull Task<AssetPackStates> task) {
				try {
					Logger.i(TAG, "getPackStates() complete");
					AssetPackState assetPackState = task.getResult().packStates().get(assetPackName);
					if (assetPackState == null) {
						Logger.w(TAG, "AssetPackState result is not available");
					} else {
						Logger.d(TAG, "status: " + String.valueOf(assetPackState.status()) + ", name: " + assetPackState.name() + ", errorCode: " + assetPackState.errorCode() + ", bytesDownloaded: " + assetPackState.bytesDownloaded() + ", totalBytesToDownload: " + assetPackState.totalBytesToDownload() + ", transferProgressPercentage: " + assetPackState.transferProgressPercentage());
						Logger.d(TAG, "store the state..."+assetPackName +" in "+mAssetPackStateMap);
						GooglePlayAssetsManager.this.mAssetPackStateMap.put(assetPackName, assetPackState);
						Logger.d(TAG, "dispatchStatusEventAsync...");
						GooglePlayAssetsManager.this.mExtCtx.dispatchStatusEventAsync(assetPackState.name(), String.valueOf(assetPackState.status()));
					}
				} catch (Exception err) {
					Logger.exception("getPackStates", err);
				}
			}
		});
	}

	public int fetch(final List<String> packNameList) {
		Logger.d(TAG, "fetch()");
		int status = 0;
		if (!packNameList.isEmpty()) {
			Task<AssetPackStates> assetPackStatesTask = this.assetPackManager.fetch(packNameList);
			assetPackStatesTask.addOnCompleteListener(new OnCompleteListener<AssetPackStates>() {
				public void onComplete(@NonNull Task<AssetPackStates> task) {
					Logger.i(TAG, "fetch() complete");
					try {
						AssetPackState assetPackState = task.getResult().packStates().get(packNameList.get(0));
						if (assetPackState == null) {
							Logger.w(TAG, "AssetPackState result is not available");
						} else {
							Logger.i(TAG, "status: " + assetPackState.status() + ", name: " + assetPackState.name() + ", errorCode: " + assetPackState.errorCode() + ", bytesDownloaded: " + assetPackState.bytesDownloaded() + ", totalBytesToDownload: " + assetPackState.totalBytesToDownload() + ", transferProgressPercentage: " + assetPackState.transferProgressPercentage());
							GooglePlayAssetsManager.this.mAssetPackStateMap.put(assetPackState.name(), assetPackState);
							GooglePlayAssetsManager.this.mExtCtx.dispatchStatusEventAsync(assetPackState.name(), String.valueOf(assetPackState.status()));
						}
					} catch (RuntimeExecutionException err) {
						if (err.getCause() instanceof AssetPackException) {
							AssetPackException apException = (AssetPackException)err.getCause();
							// the only available info is the status code
							// https://developer.android.com/reference/com/google/android/play/core/assetpacks/model/AssetPackErrorCode.html#summary
							int status = apException.getStatusCode();
							if (status < 0) {
								GooglePlayAssetsExtension.extensionContext.dispatchStatusEventAsync("AssetPackException", String.valueOf(status));
							}
						} else Logger.exception("fetch", err);
					}
				}
			});
		}

		Logger.d(TAG, "fetch() end: status " + status);
		return status;
	}

	public String getAbsoluteAssetPath(String assetPack, String relativeAssetPath) {
		if (!relativeAssetPath.startsWith("/")) {
			relativeAssetPath = "/" + relativeAssetPath;
		}

		Logger.d(TAG, "getAbsoluteAssetPath(\"" + assetPack + "\", \"" + relativeAssetPath + "\")");
		String assetsFolderPath = null;
		AssetPackState state = (AssetPackState)this.mAssetPackStateMap.get(assetPack);
		if (state == null) {
			Logger.w(TAG, "Attempting to get path for asset pack with unknown status");
			return null;
		} else if (state.status() != AssetPackStatus.COMPLETED) {
			Logger.w(TAG, "Attempting to get path for asset pack that is not yet completed");
			return null;
		} else {
			AssetLocation loc = this.assetPackManager.getAssetLocation(assetPack, relativeAssetPath);
			if (loc == null) {
				Logger.w(TAG, "Asset location not found");
			} else {
				 assetsFolderPath = loc.path();
				 Logger.d(TAG, "Asset location is " + assetsFolderPath);
			}
			return assetsFolderPath;
		}
	}

	public String getAssetPackLocation(String assetPack) {
		String assetPackPath = null;
		Logger.d(TAG, "getPackLocation");
		AssetPackLocation packLocation = this.assetPackManager.getPackLocation(assetPack);
		if (packLocation == null) {
			Logger.w(TAG, "Asset pack [" + assetPack + "] has not been downloaded");
		} else {
			Logger.i(TAG, "Asset pack [" + assetPack + "] is already available");
			int storageMethod = packLocation.packStorageMethod();
			switch(storageMethod) {
			case AssetPackStorageMethod.STORAGE_FILES:
				Logger.d(TAG, "Storage method = STORAGE_FILES");
				break;
			case AssetPackStorageMethod.APK_ASSETS:
				Logger.d(TAG, "Storage method = APK_ASSETS");
				break;
			default:
				Logger.d(TAG, "Storage method = " + storageMethod);
			}

			assetPackPath = packLocation.assetsPath();
			if (assetPackPath != null) {
				Logger.d(TAG, "Asset pack files at " + assetPackPath);
			} else {
				Logger.w(TAG, "Asset pack is not using file storage");
			}
		}

		Logger.d(TAG, "getPackLocation end " + assetPackPath);
		return assetPackPath;
	}

	public int getStatus(String assetPackName) {
		Logger.d(TAG, "getStatus()");
		AssetPackState state = (AssetPackState)this.mAssetPackStateMap.get(assetPackName);
		if (state != null) {
			Logger.d(TAG, "status: "+state.status());
			return state.status();
		}
		Logger.w(TAG, "Attempting to get status for asset pack with unknown status");
		this.assetPackStatesTask = null;
		this.getPackStates(assetPackName);
		return 0;
	}

	public int cancel(String packNames) {
		Logger.d(TAG, "cancel()");
		List<String> list = new ArrayList<>();
		list.add(packNames);
		AssetPackStates assetPackStates = this.assetPackManager.cancel(list);
		int status = 0;
		if (assetPackStates != null) {
			AssetPackState state = (AssetPackState)assetPackStates.packStates().get(packNames);
			if (state != null) {
				status = state.status();
			}
		}

		Logger.d(TAG, "cancel status = " + status);
		return status;
	}

	public int removePack(String packNames) {
		Logger.d(TAG, "removePack()");
		Task<Void> removeTask = this.assetPackManager.removePack(packNames);
		if (removeTask.isComplete()) {
			Logger.d(TAG, "removePack() complete");
			return 4;
		} else {
			return 5;
		}
	}

	public int totalBytesToDownload(String assetPackName) {
		Logger.d(TAG, "totalBytesToDownload");
		AssetPackState state = (AssetPackState)this.mAssetPackStateMap.get(assetPackName);
		if (state == null) {
			Logger.w(TAG, "Attempting to get details for asset pack with unknown status");
			return 0;
		} else {
			Logger.d(TAG, "totalBytesToDownload=" + state.totalBytesToDownload());
			return (int)state.totalBytesToDownload();
		}
	}

	public int byteDownloaded(String assetPackName) {
		Logger.d(TAG, "byteDownloaded");
		AssetPackState state = (AssetPackState)this.mAssetPackStateMap.get(assetPackName);
		if (state == null) {
			Logger.w(TAG, "Attempting to get details for asset pack with unknown status");
			return 0;
		} else {
			Logger.d(TAG, "byteDownloaded=" + state.bytesDownloaded());
			return (int)state.bytesDownloaded();
		}
	}

	public int transferProgressPercentage(String assetPackName) {
		Logger.d(TAG, "transferProgressPercentage");
		AssetPackState state = (AssetPackState)this.mAssetPackStateMap.get(assetPackName);
		if (state == null) {
			Logger.w(TAG, "Attempting to get details for asset pack with unknown status");
			return 0;
		} else {
			Logger.d(TAG, "transferProgressPercentage=" + state.transferProgressPercentage());
			return state.transferProgressPercentage();
		}
	}

	public boolean showCellularDataConfirmation(String assetPackName) {
		Logger.d(TAG, "requestCellularDataConfirmation for " + assetPackName);
		AssetPackState state = (AssetPackState)this.mAssetPackStateMap.get(assetPackName);
		if (state == null) {
			Logger.w(TAG, "Attempting to get details for asset pack with unknown status");
			return false;
		} else if (state.status() != AssetPackStatus.WAITING_FOR_WIFI) {
			Logger.w(TAG, "Asset pack is not in the 'WAITING_FOR_WIFI' status: ignoring request");
			return false;
		} else {
			this.assetPackManager.showCellularDataConfirmation(this.mExtCtx.getActivity()).addOnSuccessListener(new OnSuccessListener<Integer>() {
				public void onSuccess(Integer resultCode) {
					if (resultCode == -1) {
						Logger.i(TAG, "Confirmation dialog has been accepted.");
					} else if (resultCode == 0) {
						Logger.i(TAG, "Confirmation dialog has been denied by the user.");
					} else {
						Logger.i(TAG, "Confirmation dialog unknown response = " + resultCode);
					}

				}
			});
			return true;
		}
	}
	public void onStateUpdate(AssetPackState assetPackState) {
		Logger.i(TAG, "Asset pack onStateUpdate -> " + assetPackState.name() + " -> " + String.valueOf(assetPackState.status()));
		this.mAssetPackStateMap.put(assetPackState.name(), assetPackState);
		this.mExtCtx.dispatchStatusEventAsync(assetPackState.name(), String.valueOf(assetPackState.status()));
	}
}
