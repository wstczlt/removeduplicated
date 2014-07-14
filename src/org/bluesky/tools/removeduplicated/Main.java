package org.bluesky.tools.removeduplicated;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

	private static String valuePath = "/Users/zhulantian/code/phoenix/packages/phoenix/res/values/colors.xml";
	private static String rootPath = "/Users/zhulantian/code/phoenix/packages/phoenix";

	public static void main(String[] args) {
		int count = 0;
		while (findAndReplace("color", valuePath)) {
			count++;
		}
		System.out.println("---------- success:" + count);
	}

	private static boolean findAndReplace(String type, String path) {
		Map<String, String> strings = new HashMap<String, String>();
		int resultLineNum = -1;
		Pair<String, String> repeat = null;

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(valuePath), "utf-8"));
			String content;
			int lineNum = 0;
			while ((content = reader.readLine()) != null) {
				lineNum++;
				Pair<String, String> pair = getKeyValue(content);
				if (pair == null) {
					continue;
				}
				if (strings.containsKey(pair.value)) {
					resultLineNum = lineNum;
					repeat = new Pair<String, String>(pair.key,
							strings.get(pair.value));
					break;
				} else {
					strings.put(pair.value, pair.key);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					//
				}

			}
		}

		if (resultLineNum >= 0 && repeat != null) {
			removeLine(resultLineNum);
			replace(repeat.key, repeat.value, type);
			return true;
		}
		return false;
	}

	private static void replace(String target, String to, String type) {
		System.out.println("replacing res:" + target + " -> " + to);
		Set<String> prefixs = new HashSet<String>();
		if ("string".equals(type)) {
			prefixs.add("@string/");
			prefixs.add("R.string.");
		} else if ("dimen".equals(type)) {
			prefixs.add("R.dimen.");
			prefixs.add("@dimen/");
		} else if ("color".equals(type)) {
			prefixs.add("R.color.");
			prefixs.add("@color/");
		}
		for (String replacement : prefixs) {
			String _target = replacement + target;
			String _to = replacement + to;
			new FolderReplaceHelper(rootPath).replace(_target, _to);
		}
	}

	private static void removeLine(final int lineNum) {
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(valuePath), "utf-8"));
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
					new FileOutputStream(valuePath), "utf-8"));
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

	private static Pair<String, String> getKeyValue(String input) {
		int start = input.indexOf('>');
		if (start == -1) {
			return null;
		}
		int end = input.indexOf('<', start);
		if (end == -1) {
			return null;
		}
		final String value = input.substring(start + 1, end);
		start = input.indexOf("name=");
		if (start == -1) {
			return null;
		}
		start = input.indexOf('\"', start);
		if (start == -1) {
			return null;
		}
		end = input.indexOf('\"', start + 1);
		if (end == -1) {
			return null;
		}
		final String name = input.substring(start + 1, end);
		return new Pair<String, String>(name, value);
	}

	private static String readLine(Reader reader) throws IOException {
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

	private static class Pair<K, V> {
		K key;
		V value;

		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}

	}
}
