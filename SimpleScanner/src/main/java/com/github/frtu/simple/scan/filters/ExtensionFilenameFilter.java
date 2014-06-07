package com.github.frtu.simple.scan.filters;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

public class ExtensionFilenameFilter implements FileFilter {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ExtensionFilenameFilter.class);

	private final ArrayList<String> acceptedFileExtension = new ArrayList<String>();

	public ExtensionFilenameFilter() {
		super();
	}

	public ExtensionFilenameFilter(String... extensions) {
		super();
		addAcceptedFileExtension(extensions);
	}

	public void addAcceptedFileExtension(String... extensions) {
		for (String extension : extensions) {
			String filteredExtension = extension;
			if (extension.contains(".")) {
				filteredExtension = FilenameUtils.getExtension(extension);
			}
			acceptedFileExtension.add(filteredExtension.toLowerCase());
		}
	}

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			// Skip directories!
			return true;
		}

		String filename = file.getName();
		String extension = FilenameUtils.getExtension(filename).toLowerCase();
		logger.debug("Found extension={} for filename={}", extension, filename);

		for (String fileExtension : acceptedFileExtension) {
			if (fileExtension.equals(extension)) {
				return true;
			}
		}
		return false;
	}
}
