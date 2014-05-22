package com.ehensin.pt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class FileReader {
	private BufferedReader reader;
    private String fileName;
	public FileReader(String path, String fileName)throws IOException {
		File dir = new File(path);
		if (!dir.exists())
			throw new IOException("cannot find path " + path);
		File file = new File(dir, fileName);
		if (!file.exists()) {
			throw new IOException("cannot find file " + fileName);
		}
		FileInputStream ostream = null;
		try {
			ostream = new FileInputStream(file);
		} catch (Exception ex) {
			LogManager.getLogManager().getLogger("FileReader").log(Level.SEVERE, "Failed to create file ["
					+ fileName + "].", ex);
		}
		reader = new BufferedReader(new InputStreamReader(ostream), 1024 * 1024);

	}

	public String readLine() {
			try {
				return reader.readLine();
			} catch (Exception e) {
				LogManager.getLogManager().getLogger("FileReader").log(Level.SEVERE, "Failed to reader :" + fileName
						+ "].", e);
			}
			return null;
	
	}

	public void close() {
		try {
			reader.close();
		} catch (Exception e) {
			LogManager.getLogManager().getLogger("FileReader").log(Level.SEVERE, "Failed to close writer,", e);
		}
	}
}
