package org.frtu.scan.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

public class DirectoryScannerTest {
	public static final File DIRECTORY = new File("src/test/resources/test_sequence");
	private static final String FILE1_TXT = "file1.txt";
	private static final String FILE2_TXT = "file2.txt";

	final class FileScannerImplementation implements FileScannerObserver {
		private ArrayList<String> arrayList = new ArrayList<String>();

		@Override
		public void scanFile(File file) {
			arrayList.add(file.getName());
		}
	}

	final class SelectiveFileScannerImplementation implements SelectiveFileScannerObserver {
		@Override
		public void scanFile(File file) {
			assertEquals(FILE1_TXT, file.getName());
		}

		@Override
		public String getSpecificFileName() {
			return FILE1_TXT;
		}
	}

	@Test
	public void testScanDirectory() {
		FileScannerImplementation fileScanner = new FileScannerImplementation();
		DirectoryScanner directoryScanner = new DirectoryScanner(fileScanner);
		directoryScanner.scanDirectory(DIRECTORY);

		ArrayList<String> arrayList = fileScanner.arrayList;
		assertTrue("file1 should happen before file2", arrayList.get(1).equals(FILE1_TXT));
		assertTrue("file1 should happen before file2", arrayList.get(2).equals(FILE2_TXT));
	}

	@Test
	public void testSelectiveScanDirectory() {
		SelectiveFileScannerImplementation fileScanner = new SelectiveFileScannerImplementation();
		DirectoryScanner directoryScanner = new DirectoryScanner(fileScanner);
		directoryScanner.scanDirectory(DIRECTORY);
	}
}
