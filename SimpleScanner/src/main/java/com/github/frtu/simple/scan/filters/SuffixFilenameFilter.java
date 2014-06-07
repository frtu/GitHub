package com.github.frtu.simple.scan.filters;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

public class SuffixFilenameFilter implements FileFilter {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SuffixFilenameFilter.class);

	private final ArrayList<String> acceptedFileSuffixes = new ArrayList<String>();

	public SuffixFilenameFilter() {
		super();
	}

	public SuffixFilenameFilter(String... suffixes) {
		super();
		addAcceptedFileSuffixes(suffixes);
	}

	public void addAcceptedFileSuffixes(String... suffixes) {
		for (String suffix : suffixes) {
			acceptedFileSuffixes.add(suffix);
		}
	}

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			// Skip directories!
			return true;
		}

		String filename = file.getName();
		String basename = FilenameUtils.getBaseName(filename).toLowerCase();
		logger.debug("Found basename={} for filename={}", basename, filename);

		for (String suffix : acceptedFileSuffixes) {
			if (basename.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}
}
