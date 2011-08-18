package com.sftask.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.*;
import java.nio.charset.Charset;

import android.util.Log;

public class LogUtils {
	
	public static final File file=new File("/sdcard/sf-log.txt");
	public static final String FORMAT="[%s]%s";
		
	public static void error(Throwable ex){
		if(ex.getLocalizedMessage()!=null){
			Log.e("error", ex.getLocalizedMessage());
			error(ex.getLocalizedMessage());
		}
		for (StackTraceElement element : ex.getStackTrace()) {
			error(element.toString());
			Log.e("error", element.toString());
		}
		error("---------------------------------------------");
	}

	public static void error(String msg){
		List<String> contents=new ArrayList<String>();
		try {
			contents=IOUtils.readLines(file);
		} catch (Exception e) {
			IOUtils.writeLines(file, e.getMessage());
		}
		String dateFormat=DateUtils.format(new Date());
		String log=String.format(FORMAT,dateFormat,msg);
		contents.add(log);
		IOUtils.writeLines(file, contents);
	}
}
