//   ____  __   __        ______        __
//  / __ \/ /__/ /__ ___ /_  __/__ ____/ /
// / /_/ / / _  / -_|_-<_ / / / -_) __/ _ \
// \____/_/\_,_/\__/___(@)_/  \__/\__/_// /
//  ~~~ oldes.huhuman at gmail.com ~~~ /_/
//
// SPDX-License-Identifier: Apache-2.0

package tech.oldes.google
{
	import flash.events.EventDispatcher;
	import flash.events.Event;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	import tech.oldes.google.events.*
	
	public class PlayAssetsExtension extends EventDispatcher
	{
		////////////////////////////////////////////////////////
		//	CONSTANTS
		//
		
		//
		//	ID and Version numbers
		public static const EXT_CONTEXT_ID:String = PlayAssetsConst.EXTENSIONID;
		private static const EXT_ID_NUMBER:int = -1;
		
		public static const VERSION:String = PlayAssetsConst.VERSION;
		private static const VERSION_DEFAULT:String = "0";
		private static const IMPLEMENTATION_DEFAULT:String = "unknown";
		
		//
		//	Error Messages
		private static const ERROR_CREATION:String = "The PlayAssetsExtension context could not be created";

		//
		//	Public constants
		public static const ON_UPDATE              :String = "PLAY_ASSET_UPDATE";
		public static const ON_EXCEPTION           :String = "PLAY_ASSET_EXCEPTION";
		public static const ON_EXECUTION_EXCEPTION :String = "PLAY_ASSET_EXECUTION_EXCEPTION";

		////////////////////////////////////////////////////////
		//	VARIABLES
		//
		
		//
		// Singleton variables	
		private static var _instance:PlayAssetsExtension;
		private static var _extContext:ExtensionContext;
		
		////////////////////////////////////////////////////////
		//	SINGLETON INSTANCE
		//
		public static function get instance():PlayAssetsExtension {
			if ( !_instance ) {
				_instance = new PlayAssetsExtension( new SingletonEnforcer() );
				_instance.init();
			}
			return _instance;
		}

		public function PlayAssetsExtension( enforcer:SingletonEnforcer ) {
			_extContext = ExtensionContext.createExtensionContext( EXT_CONTEXT_ID, null );
			if ( !_extContext ) throw new Error( ERROR_CREATION );
			_extContext.addEventListener( StatusEvent.STATUS, onStatusHandler );
		}

		private function onStatusHandler( event:StatusEvent ):void {
			if (event.code == "") {
				this.dispatchEvent(new PlayAssetsExecutionException(event.level));
				return;
			}
			var status:int = parseInt(event.level);
			if (status < 0) {
				this.dispatchEvent(new PlayAssetsException(event.code, status));
			} else {
				this.dispatchEvent(new PlayAssetsEvent(event.code, status));
			}
		}

		private function init():void {
			_extContext.call( "init" );
		}

		public function dispose():void {
			if (_extContext) {
				_extContext.removeEventListener( StatusEvent.STATUS, onStatusHandler );
				_extContext.dispose();
				_extContext = null;
			}
			_instance = null;
		}
		

		
		
		//----------------------------------------
		//
		// Public Methods
		//
		//----------------------------------------

		public function get version():String {
			return VERSION;
		}

		public function get nativeVersion():String {
			return _extContext.call("nativeVersion") as String;
		}
		
		public function initAssetDelivery():Boolean {
			return _extContext.call("initAssetDelivery") as Boolean;
		}

		public function openInstallTimeAsset(fileName:String):int {
			return _extContext.call("openInstallTimeAsset") as int;
		}

		public function fetchAssetsPack(pack:String):int {
			if (pack == null) return null;
			return _extContext.call("fetchAssetsPack", pack) as int;
		}

		public function getAssetAbsolutePath(pack:String, path:String):String {
			if (pack == null || path == null) return null;
			return _extContext.call("getAssetAbsolutePath", pack, path) as String;
		}

		public function getAssetPackLocation(pack:String):String {
			if (pack == null) return null;
			return _extContext.call("getAssetPackLocation", pack) as String;
		}

		public function getAssetPackStatus(pack:String):int {
			if (pack == null) return null;
			return _extContext.call("getAssetPackStatus", pack) as int;
		}

		public function getBytesDownloaded(pack:String):int {
			if (pack == null) return null;
			return _extContext.call("getBytesDownloaded", pack) as int;
		}

		public function getTotalBytesToDownLoad(pack:String):int {
			if (pack == null) return null;
			return _extContext.call("getTotalBytesToDownLoad", pack) as int;
		}

		public function getTransferProgressPercentage(pack:String):int {
			if (pack == null) return null;
			return _extContext.call("getTransferProgressPercentage", pack) as int;
		}

		public function showConfirmationDialog():Boolean {
			return _extContext.call("showConfirmationDialog") as Boolean;
		}

		// These OBB related functions are here in case when migrating from OBB to AAB.
		// In such a case one must delete existing OBB file to save a space on device! 
		public function getObbDir():String {
			return _extContext.call("getObbDir") as String;
		}
		public function getMainOBBFile(version:int):String {
			return _extContext.call("getMainOBBFile", version) as String;
		}

		// Just for testing...
		public function testException(status:int):Boolean {
			return _extContext.call("testException", status) as Boolean;
		}
	}
}

class SingletonEnforcer {}