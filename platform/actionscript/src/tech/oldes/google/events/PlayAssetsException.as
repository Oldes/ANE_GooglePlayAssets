//   ____  __   __        ______        __
//  / __ \/ /__/ /__ ___ /_  __/__ ____/ /
// / /_/ / / _  / -_|_-<_ / / / -_) __/ _ \
// \____/_/\_,_/\__/___(@)_/  \__/\__/_// /
//  ~~~ oldes.huhuman at gmail.com ~~~ /_/
//
// SPDX-License-Identifier: Apache-2.0

package tech.oldes.google.events
{
	import flash.events.Event;

	public class PlayAssetsException extends Event
	{

		public static const PLAY_ASSET_EXCEPTION:String = "PLAY_ASSET_EXCEPTION";

		public static const ACCESS_DENIED        :int = -7;
		public static const API_NOT_AVAILABLE    :int = -5;
		public static const APP_NOT_OWNED        :int = -13;
		public static const APP_UNAVAILABLE      :int = -1;
		public static const DOWNLOAD_NOT_FOUND   :int = -4;
		public static const INSUFFICIENT_STORAGE :int = -10;
		public static const INTERNAL_ERROR       :int = -100;
		public static const INVALID_REQUEST      :int = -3;
		public static const NETWORK_ERROR        :int = -6;
		public static const NETWORK_UNRESTRICTED :int = -12;
		public static const PACK_UNAVAILABLE     :int = -2;
		public static const PLAY_STORE_NOT_FOUND :int = -11;

		private var _status:int;
		private var _name:String;
		
		public function PlayAssetsException(name:String, statusId:int, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			this._status = statusId;
			this._name = name;
			super(PLAY_ASSET_EXCEPTION, bubbles, cancelable);
		}

		public function get status():int { return _status; }
		public function get name():String { return _name; }
		public function get statusName():String {
			switch (_status) {
				case ACCESS_DENIED        : return "ACCESS_DENIED";
				case API_NOT_AVAILABLE    : return "API_NOT_AVAILABLE";
				case APP_NOT_OWNED        : return "APP_NOT_OWNED";
				case APP_UNAVAILABLE      : return "APP_UNAVAILABLE";
				case DOWNLOAD_NOT_FOUND   : return "DOWNLOAD_NOT_FOUND";
				case INSUFFICIENT_STORAGE : return "INSUFFICIENT_STORAGE";
				case INTERNAL_ERROR       : return "INTERNAL_ERROR";
				case INVALID_REQUEST      : return "INVALID_REQUEST";
				case NETWORK_ERROR        : return "NETWORK_ERROR";
				case NETWORK_UNRESTRICTED : return "NETWORK_UNRESTRICTED";
				case PACK_UNAVAILABLE     : return "PACK_UNAVAILABLE";
				case PLAY_STORE_NOT_FOUND : return "PLAY_STORE_NOT_FOUND";
			}
			return "NO_ERROR";
		}

		public override function toString():String {
			return "[PLAY_ASSET_EXCEPTION name:"+ _name+ " status="+_status+" "+statusName+"]";
		}
	}
}