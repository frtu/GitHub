package org.frtu.scan.simple;

import java.io.File;

/**
 * File observer that should be used with {@link DirectoryScanner}
 * 
 * @author Frederic TU
 */
public interface FileScannerObserver {
	public void scanFile(File file);
}