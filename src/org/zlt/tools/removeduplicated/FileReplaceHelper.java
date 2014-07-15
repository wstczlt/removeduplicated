package org.zlt.tools.removeduplicated;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public class FileReplaceHelper {

	private static final Set<Character> END_CHARS;
	static {
		END_CHARS = new HashSet<Character>();
		END_CHARS.add(',');
		END_CHARS.add(')');
		END_CHARS.add(' ');
		END_CHARS.add(';');
		END_CHARS.add('"');
		END_CHARS.add('\'');
		END_CHARS.add('<');
	}

	private final String path;

	public FileReplaceHelper(String path) {
		this.path = path;
	}

	public void replace(String target, String to) {
		// System.out.println("replacing file:" + path);
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), "utf-8"));
			int lineNum = 0;
			String line;
			boolean find = false;
			while ((line = IOUtils.readLine(reader)) != null) {
				lineNum++;
				String result = replaceLine(line, target, to, 0);
				if (!result.equals(line)) {
					if (!find) {
						System.out.println(new File(path).getName());
						find = true;
					}
					System.out.println(" -> line:" + lineNum);
				}
				content.append(result);
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

	private static String replaceLine(String line, String target, String to,
			int from) {
		int index = line.indexOf(target, from);
		if (index == -1) {
			return line;
		}
		int end = index + target.length();
		if (end == line.length()) {
			return line.replace(target, to);
		} else {
			char next = line.charAt(end);
			if (END_CHARS.contains(next)) {
				return replaceLine(line.replace(target, to), target, to, end);
			} else {
				return replaceLine(line, target, to, end);
			}
		}
	}
}
