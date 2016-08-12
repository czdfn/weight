package com.chengsi.weightcalc.constants;

import android.os.Environment;

import java.io.File;

public final class FileConstants {

	public static final String ROOT_DIR = "haoyuntong";
	public static final String ROOT_PATH = Environment.getExternalStorageDirectory() + File.separator + ROOT_DIR + File.separator;
	public static final String ROOT_LOG_PATH = ROOT_PATH + "logs" + File.separator;
	public static final String CRASH_LOG_PATH = ROOT_LOG_PATH + "crash/";
	public static final String ROOT_CACHE_PATH = ROOT_PATH + "c" + File.separator;
	public static final String ROOT_IMAGE_PATH = ROOT_PATH + "i" + File.separator;
	public static final String ROOT_AUDIO_PATH = ROOT_PATH + "a" + File.separator;
	public static final String ROOT_VIDEO_PATH = ROOT_PATH + "v" + File.separator;
	public static final String ROOT_SETTINGS_PATH = ROOT_PATH + "s" + File.separator;
}
