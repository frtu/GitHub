package com.github.frtu.simple.scan.observers;

import java.io.File;
import java.util.ArrayList;

import com.github.frtu.simple.scan.FileScannerObserver;

/**
 * Simple implementation of {@link FileScannerObserver} that gather all the found file and return through a {@link #getResults}
 * 
 * @author Frederic TU
 */
public class CumulativeFileScannerObserver implements FileScannerObserver {
	private ArrayList<File> results = new ArrayList<File>();

	@Override
	public void scanFile(File file) {
		results.add(file);
	}

	public ArrayList<File> getResults() {
		return results;
	}
}