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

	public class PlayAssetsExecutionException extends Event
	{

		public static const PLAY_ASSET_EXECUTION_EXCEPTION:String = "PLAY_ASSET_EXECUTION_EXCEPTION";

		private var _message:String;
		
		public function PlayAssetsExecutionException(message:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			this._message = message;
			super(PLAY_ASSET_EXECUTION_EXCEPTION, bubbles, cancelable);
		}

		public function get message():String { return _message; }

		public override function toString():String {
			return "[PLAY_ASSET_EXECUTION_EXCEPTION message:"+ _message+"]";
		}
	}
}