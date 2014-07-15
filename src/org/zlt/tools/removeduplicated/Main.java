package org.zlt.tools.removeduplicated;

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
			IOUtils.removeLine(path, repeat.lineNum);
			replace(root, type, repeat.repeatKey, repeat.availableKey);
			++count;
		}
		System.out.println("---------- success:" + count);
	}

	private static void replace(String root, String type, String target,
			String to) {
		System.out.println("replacing res:" + target + " -> " + to);
		Set<String> prefixs = prefixSet(type);
		for (String replacement : prefixs) {
			String _target = replacement + target;
			String _to = replacement + to;
			new FolderReplaceHelper(root).replace(_target, _to);
		}
	}

	private static Set<String> prefixSet(String type) {
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
		return prefixs;
	}
}
