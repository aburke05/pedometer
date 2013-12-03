/*
 * Copyright 2013 Thomas Hoffmann
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.j4velin.pedometer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.database.Cursor;
import android.os.Environment;

public class Logger {

	public final static boolean LOG = true;

	private static FileWriter fw;
	private static Date date = new Date();
	private final static String APP = "Pedometer";

	public static void log(Throwable ex) {
		log(ex.getMessage());
		for (StackTraceElement ste : ex.getStackTrace()) {
			log(ste.toString());
		}
	}
	
	public static void log(final Cursor c) {
		if (!Logger.LOG)
			return;
		c.moveToFirst();
		String title ="";
		for(int i=0; i<c.getColumnCount();i++)
			title += c.getColumnName(i)+" | ";
		log(title);
		while(!c.isAfterLast()) {
			title = "";
			for(int i=0; i<c.getColumnCount();i++)
				title += c.getString(i)+" | ";
			log(title);
			c.moveToNext();
		}
	}

	@SuppressWarnings("deprecation")
	public
	static void log(String msg) {
		if (!Logger.LOG)
			return;
		android.util.Log.d(APP, msg);
		if (BuildConfig.DEBUG && false)
			android.util.Log.d(APP, msg);
		else {
			try {
				if (fw == null) {
					fw = new FileWriter(new File(Environment.getExternalStorageDirectory().toString() + "/" + APP + ".log"), true);
				}
				date.setTime(System.currentTimeMillis());
				fw.write(date.toLocaleString() + " - " + msg + "\n");
				fw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void finalize() throws Throwable {
		try {
			if (fw != null)
				fw.close();
		} finally {
			super.finalize();
		}
	}

}
