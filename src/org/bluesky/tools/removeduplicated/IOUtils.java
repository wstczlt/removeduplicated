package org.bluesky.tools.removeduplicated;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

public class IOUtils {

	public static void removeLine(String path, int lineNum) {
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), "utf-8"));
			int currentLineNum = 0;
			String line;
			while ((line = readLine(reader)) != null) {
				currentLineNum++;
				if (currentLineNum != lineNum) {
					content.append(line);
				}
			}
		} catch (IOException e) {
			//
		} finally {
			try {
				reader.close();
			} catch (IOException ex) {
				//
			}
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path), "utf-8"));
			writer.write(content.toString());
		} catch (IOException e) {
			//
		} finally {
			try {
				writer.close();
			} catch (IOException ex) {
				//
			}
		}
	}

	public static String readLine(Reader reader) throws IOException {
		StringBuilder builder = new StringBuilder();
		int val;
		while ((val = reader.read()) != -1) {
			char ch = (char) val;
			if (ch != '\n') {
				builder.append(ch);
			} else {
				return builder.append('\n').toString();
			}
		}
		if (builder.length() == 0) {
			return null;
		} else {
			return builder.toString();
		}
	}
}
