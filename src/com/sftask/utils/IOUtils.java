package com.sftask.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.charset.Charset;

import android.util.Log;

public class IOUtils {

	public static List<String> readLines(File file) throws Exception {
		FileReader fr =null;
		BufferedReader br=null;
		List<String> result = new ArrayList<String>();
		try {			
			 fr = new FileReader(file);
			 br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), Charset.forName("utf-8")));
			String s;
			while ((s = br.readLine()) != null) {
				result.add(s);
			}
			fr.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(fr!=null){
				fr.close();
			}
			if(br!=null){
				br.close();
			}
			return result;
		}
		
		
	}

	public static boolean writeLines(File file, List<String> contents) {
		boolean result=false;
		BufferedWriter writer =null;
		try {
			createFold(file);
			if(!file.exists()){
				file.createNewFile();
			}
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));
			for (String content : contents) {
				writer.write(content);
				writer.newLine();
			}
			writer.flush();
			result= true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(IOUtils.class.getName(),e.getMessage());
		}finally{
			if(writer!=null){
				try {
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		}		
	}

	public static boolean writeLines(File file, String... contents) {
		try {
			List<String> lines = new ArrayList<String>();
			for (String content : contents) {
				lines.add(content);
			}
			return writeLines(file, lines);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void createFold(File folder) {
		try {
			if (!folder.exists()) {
				new File(folder.getParent()).mkdirs();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		List<String> lines = IOUtils
				.readLines(new File(
						"E:\\workspace-play\\myshop\\WebRoot\\pages\\Index\\index.html"));
		for (String string : lines) {
			System.out.println(string);
		}
		IOUtils.writeLines(new File("c:\\cc\\nofeng.jsp"), lines);

	}

}
