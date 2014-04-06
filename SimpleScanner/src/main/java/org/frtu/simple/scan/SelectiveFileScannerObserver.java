package org.frtu.simple.scan;

/**
 * File listener that listen to only on file name. Should be used with {@link DirectoryScanner}
 * 
 * @author Frederic TU
 */
public interface SelectiveFileScannerObserver extends FileScannerObserver {
	public String getSpecificFileName();
}
