package org.frtu.simple.scan;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a directory scanner support.
 * 
 * @author Frederic TU
 */
public class DirectoryScanner {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/** Scan all files */
	private List<FileScannerObserver> fileScanners = new ArrayList<FileScannerObserver>();

	/** fileScanner map that is used for selected (dedicated) file */
	private HashMap<String, SelectiveFileScannerObserver> selectiveFileScannerMap = new HashMap<String, SelectiveFileScannerObserver>();

	public DirectoryScanner(FileScannerObserver fileScanner) {
		super();
		this.addFileScanner(fileScanner);
	}

	public void addFileScanner(FileScannerObserver fileScanner) {
		if (fileScanner instanceof SelectiveFileScannerObserver) {
			SelectiveFileScannerObserver specificFileScanner = (SelectiveFileScannerObserver) fileScanner;
			selectiveFileScannerMap.put(specificFileScanner.getSpecificFileName(), specificFileScanner);
		} else {
			fileScanners.add(fileScanner);
		}
	}

	/**
	 * 
	 * @param directoryToScan
	 */
	public void scanDirectory(File directoryToScan) {
		scanDirectory(directoryToScan, null);
	}

	/**
	 * 
	 * @param directoryToScan
	 * @param folderToSkip
	 */
	public void scanDirectory(File directoryToScan, Set<String> folderToSkip) {
		logger.debug("Currently indexing directory={}", directoryToScan.getAbsoluteFile());
		File[] files = directoryToScan.listFiles();
		if (files != null) {
			ArrayList<File> folderBuffered = null;

			// First scan the file and add some flag
			for (File file : files) {
				if (file.isDirectory()) {
					// Buffered
					// ---------
					if (folderBuffered == null) {
						folderBuffered = new ArrayList<File>();
					}
					folderBuffered.add(file);
				} else if (file.isFile()) {
					dealWithFile(file);
				}
			}

			if (folderBuffered != null) {
				for (File file : folderBuffered) {
					dealWithFolder(folderToSkip, file);
				}
			}
		}
	}

	/**
	 * Check whether the folder should be scanned recursively
	 * 
	 * @param folderToSkip
	 * @param file
	 */
	private void dealWithFolder(Set<String> folderToSkip, File file) {
		String folderName = file.getName();
		if (folderToSkip == null
				|| (!folderToSkip.contains(folderName) && !folderName.contains(".ear") && !folderName.contains(".war") && !folderName
						.contains(".jar"))) {
			// Logger and scan logic is already in the recursivity
			scanDirectory(file, folderToSkip);
		} else {
			logger.debug("This folder was skip:{}", file.getAbsolutePath());
		}
	}

	/**
	 * deal with the file that need to be parsed specific
	 * 
	 * @param file
	 */
	private void dealWithFile(File file) {
		// Browse thru Map
		SelectiveFileScannerObserver specificFileScanner = selectiveFileScannerMap.get(file.getName());
		if (specificFileScanner != null) {
			logger.debug("Launching fileScanner={} for file={}", specificFileScanner.getClass(), file.getAbsoluteFile());
			specificFileScanner.scanFile(file);
		}

		// Browse thru List
		for (FileScannerObserver fileScanner : fileScanners) {
			logger.debug("Launching fileScanner={} for file={}", fileScanner.getClass(), file.getAbsoluteFile());
			fileScanner.scanFile(file);
		}
	}
}
