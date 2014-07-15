package org.zlt.tools.removeduplicated;

import java.io.File;
import java.io.FileFilter;

public class FolderReplaceHelper {

	private final String path;

	public FolderReplaceHelper(String path) {
		this.path = path;
	}

	public void replace(String target, String to) {
		File[] files = new File(path).listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isDirectory() || file.getName().endsWith(".java")
						|| file.getName().endsWith(".xml");
			}
		});
		if (files == null || files.length == 0) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				new FolderReplaceHelper(file.getAbsolutePath()).replace(target,
						to);
			} else if (file.isFile()) {
				new FileReplaceHelper(file.getAbsolutePath()).replace(target,
						to);
			}
		}
	}

}
