package org.bluesky.tools.removeduplicated;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ResourcesAnalyser {

	private final String path;
	private final String type;

	public ResourcesAnalyser(String valuePath, String resourceType) {
		this.path = valuePath;
		this.type = resourceType;
	}

	public Repeat analyse() {
		Map<String, String> strings = new HashMap<String, String>();
		Repeat repeat = null;

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), "utf-8"));
			String content;
			int lineNum = 0;
			while ((content = reader.readLine()) != null) {
				lineNum++;
				Pair<String, String> pair = getKeyValue(content);
				if (pair == null) {
					continue;
				}
				if (strings.containsKey(pair.value)) {
					repeat = new Repeat();
					repeat.lineNum = lineNum;
					repeat.repeatKey = pair.key;
					repeat.availableKey = strings.get(pair.value);
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
		return repeat;
	}

	private Pair<String, String> getKeyValue(String input) {
		int start = input.indexOf('>');
		if (start == -1) {
			return null;
		}
		int end = input.indexOf(valueEndFlag(), start);
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

	private String valueEndFlag() {
		if ("string".equals(type)) {
			return "</string>";
		} else if ("dimen".equals(type)) {
			return "</dimen>";
		} else if ("color".equals(type)) {
			return "</color>";
		}
		return "<";
	}
}
