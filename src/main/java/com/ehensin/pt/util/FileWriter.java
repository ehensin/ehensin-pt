package com.ehensin.pt.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class FileWriter {
	Writer fw;

	public FileWriter(String path, String fileName, boolean isAppend) {
		File dir = new File(path);
		if (!dir.exists())
			dir.mkdirs();
		File file = new File(dir, fileName);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream ostream = null;
		try {
			file.createNewFile();
			ostream = new FileOutputStream(file, isAppend);
		} catch (Exception ex) {
			LogManager.getLogManager().getLogger("FileWriter").log(Level.SEVERE, "Failed to create file ["
					+ fileName + "].", ex);
		}
		fw = new OutputStreamWriter(ostream);
		fw = new BufferedWriter(fw, 1024 * 1024);

	}

	public void write(String string) {
		if (string != null) {
			try {
				fw.write(string + "\n");
			} catch (Exception e) {
				LogManager.getLogManager().getLogger("FileWriter").log(Level.SEVERE, "Failed to write [" + string
						+ "].", e);
			}
		}
	}

	public void flush() {
		try {
			fw.flush();
		} catch (Exception e) {
			LogManager.getLogManager().getLogger("FileWriter").log(Level.SEVERE, "Failed to flush writer,", e);
		}
	}

	public void close() {
		try {
			fw.flush();
			fw.close();
		} catch (Exception e) {
			LogManager.getLogManager().getLogger("FileWriter").log(Level.SEVERE, "Failed to flush writer,", e);
		}
	}
}
