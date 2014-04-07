package org.frtu.simple.scan;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.frtu.simple.scan.filters.ExtensionFilenameFilter;
import org.frtu.simple.scan.observers.CululativeFileScannerObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a directory scanner support.
 * 
 * @author Frederic TU
 */
public class DirectoryScanner {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private FileFilter fileFilter;

	/** Scan all files */
	private List<FileScannerObserver> fileScanners = new ArrayList<FileScannerObserver>();

	/** fileScanner map that is used for selected (dedicated) file */
	private HashMap<String, SelectiveFileScannerObserver> selectiveFileScannerMap = new HashMap<String, SelectiveFileScannerObserver>();

	public DirectoryScanner(FileScannerObserver fileScanner) {
		super();
		this.addFileScanner(fileScanner);
	}

	/**
	 * Easy way to set all the extension file you want to filter.
	 * 
	 * Note : Every time this method is called, the previous file extension is removed ! Call is concurrent with setFileFilter(), last call
	 * wins !
	 * 
	 * @param extensions
	 */
	public void setFileExtensionToFilter(String... extensions) {
		this.fileFilter = new ExtensionFilenameFilter(extensions);
	}

	/**
	 * Set a file filtering rule.
	 * 
	 * Note : Every time this method is called, the previous file extension is removed ! Call is concurrent with setFileFilter(), last call
	 * wins !
	 * 
	 * @param fileFilter
	 */
	public void setFileFilter(FileFilter fileFilter) {
		this.fileFilter = fileFilter;
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
		logger.debug("Currently indexing directory={}", directoryToScan.getAbsoluteFile());
		File[] files;
		if (fileFilter != null) {
			files = directoryToScan.listFiles(fileFilter);
		} else {
			files = directoryToScan.listFiles();
		}
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
					dealWithFolder(file);
				}
			}
		}
	}

	/**
	 * Util method that allow to scan all files in a folder. Works in cooperation with {@link #setFileFilter(FileFilter)} or
	 * {@link #setFileExtensionToFilter(String...)}
	 * 
	 * @param directoryToScan
	 * @return
	 */
	public ArrayList<File> scanAllFiles(File directoryToScan) {
		CululativeFileScannerObserver fileScanner = new CululativeFileScannerObserver();
		
		DirectoryScanner directoryScanner = new DirectoryScanner(fileScanner);
		for (FileScannerObserver fileScannerObserver : fileScanners) {
			directoryScanner.addFileScanner(fileScannerObserver);
		}
		directoryScanner.setFileFilter(this.fileFilter);
		directoryScanner.scanDirectory(directoryToScan);
		return fileScanner.getResults();
	}

	/**
	 * Check whether the folder should be scanned recursively
	 * 
	 * @param file
	 */
	private void dealWithFolder(File file) {
		// Can add folder logic here
		scanDirectory(file);
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
