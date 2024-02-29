//   ____  __   __        ______        __
//  / __ \/ /__/ /__ ___ /_  __/__ ____/ /
// / /_/ / / _  / -_|_-<_ / / / -_) __/ _ \
// \____/_/\_,_/\__/___(@)_/  \__/\__/_// /
//  ~~~ oldes.huhuman at gmail.com ~~~ /_/
//
// SPDX-License-Identifier: Apache-2.0

package tech.oldes.google
{
	
	public class PlayAssetsStatus
	{
		////////////////////////////////////////////////////////
		//	CONSTANTS
		//
		
		public static const UNKNOWN          :int = 0;
		public static const PENDING          :int = 1;
		public static const DOWNLOADING      :int = 2;
		public static const TRANSFERRING     :int = 3;
		public static const COMPLETED        :int = 4;
		public static const FAILED           :int = 5;
		public static const CANCELED         :int = 6;
		public static const WAITING_FOR_WIFI :int = 7;
		public static const NOT_INSTALLED    :int = 8;

		public static const STRING_UNKNOWN          :String = "UNKNOWN";
		public static const STRING_PENDING          :String = "PENDING";
		public static const STRING_DOWNLOADING      :String = "DOWNLOADING";
		public static const STRING_TRANSFERRING     :String = "TRANSFERRING";
		public static const STRING_COMPLETED        :String = "COMPLETED";
		public static const STRING_FAILED           :String = "FAILED";
		public static const STRING_CANCELED         :String = "CANCELED";
		public static const STRING_WAITING_FOR_WIFI :String = "WAITING_FOR_WIFI";
		public static const STRING_NOT_INSTALLED    :String = "NOT_INSTALLED";
		
		private static const names:Array = new Array(
			STRING_UNKNOWN,
			STRING_PENDING,
			STRING_DOWNLOADING,
			STRING_TRANSFERRING,
			STRING_COMPLETED,
			STRING_FAILED,
			STRING_CANCELED,
			STRING_WAITING_FOR_WIFI,
			STRING_NOT_INSTALLED
		);
		
		//----------------------------------------
		//
		// Public Methods
		//
		//----------------------------------------

		public static function getStatusName(status:int):String {
			return names[status];
		}
		public static function getStatus(name:String):int {
			var status:int = 0;
			switch (name) {
				case STRING_PENDING:          status = 1; break;
				case STRING_DOWNLOADING:      status = 2; break;
				case STRING_TRANSFERRING:     status = 3; break;
				case STRING_COMPLETED:        status = 4; break;
				case STRING_FAILED:           status = 5; break;
				case STRING_CANCELED:         status = 6; break;
				case STRING_WAITING_FOR_WIFI: status = 7; break;
				case STRING_NOT_INSTALLED:    status = 8; break;
			}
			return status;
		}
	}
}
