;;   ____  __   __        ______        __
;;  / __ \/ /__/ /__ ___ /_  __/__ ____/ /
;; / /_/ / / _  / -_|_-<_ / / / -_) __/ _ \
;; \____/_/\_,_/\__/___(@)_/  \__/\__/_// /
;;  ~~~ oldes.huhuman at gmail.com ~~~ /_/
;;
;; SPDX-License-Identifier: Apache-2.0

Rebol [
	title:   "Build GooglePlayAssets.ane"
	purpose: "Build Google Play Asset Pack Delivery AIR native extension"
	needs:    3.16.0 ;; https://github.com/Oldes/Rebol3/releases/tag/3.16.0
	notes: {
	* This extension is using resources from this ANE:
	  https://github.com/Oldes/ANE_GooglePlay
	* Dealing with `install-time` assets is not implemented!
	}
]

import airsdk ;== https://github.com/Oldes/Rebol-AIRSDK

make-dir %build/

air-task
"Compile GooglePlayAssetsExtension SWC" [
	compc [
		-swf-version     33
		-source-path     %platform/actionscript/src
		-include-classes %tech.oldes.google.PlayAssetsExtension
		-output          %build/tech.oldes.GooglePlayAssets.swc
	]
]

air-task
"Compile Android natives" [
	cd   %platform/android
	eval %gradlew [clean build]
	print as-green "Lint results:"
	print read/string %app/build/reports/lint-results-debug.txt
	cd   %../..
	copy-file %platform/android/app/build/outputs/aar/app-release.aar %build/tech.oldes.GooglePlayAssets.aar
]

air-task
"Compile GooglePlayAssetsExtension ANE" [
	delete-file %build/tech.oldes.GooglePlayAssets.ane
	build-ane [
		id:  @tech.oldes.GooglePlayAssets
		initializer: @GooglePlayAssetsExtension
		platforms: [Android-ARM Android-ARM64 Android-x86 Android-x64]
	]
]

if exists? %../HelloAir/Extensions/ [
	air-task
		"Copy ANE to the HelloAir test app folder" [
		copy-file %build/tech.oldes.GooglePlayAssets.ane %../HelloAir/Extensions/
	]
]