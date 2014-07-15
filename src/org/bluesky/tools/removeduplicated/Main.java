package org.bluesky.tools.removeduplicated;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		String type = "string";
		String root = "/Users/zhulantian/code/phoenix/packages/phoenix";
		String path = "/Users/zhulantian/code/phoenix/packages/phoenix/res/values/colors.xml";

		int count = 0;
		ResourcesAnalyser analyser = new ResourcesAnalyser(path, type);
		Repeat repeat;
		while ((repeat = analyser.analyse()) != null) {
			removeLine(path, repeat.lineNum);
			replace(root, type, repeat.repeatKey, repeat.availableKey);
			++count;
		}
		System.out.println("---------- success:" + count);
	}

	private static void replace(String root, String type, String target,
			String to) {
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
			new FolderReplaceHelper(root).replace(_target, _to);
		}
	}

	private static void removeLine(String path, int lineNum) {
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), "utf-8"));
			int currentLineNum = 0;
			String line;
			while ((line = IOUtils.readLine(reader)) != null) {
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
}
