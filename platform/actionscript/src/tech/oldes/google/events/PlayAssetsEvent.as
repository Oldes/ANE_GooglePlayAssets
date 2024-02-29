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
	import tech.oldes.google.PlayAssetsStatus;

	public class PlayAssetsEvent extends Event
	{

		public static const PLAY_ASSET_UPDATE:String = "PLAY_ASSET_UPDATE";

		private var _status:int;
		private var _packName:String;
		
		public function PlayAssetsEvent(assetPackName:String, statusId:int, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			this._status = statusId;
			this._packName = assetPackName;
			super(PLAY_ASSET_UPDATE, bubbles, cancelable);
		}
		public function get assetPackName():String { return _packName; }
		public function get status():int { return _status; }
		public function get statusName():String { return PlayAssetsStatus.getStatusName(_status); }

		public override function toString():String {
			return "[PLAY_ASSET_UPDATE: "+" pack="+ _packName+ " status="+_status+" statusName="+statusName+"]";
		}
	}
}