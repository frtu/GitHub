package com.github.frtu.simple.scan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.github.frtu.simple.scan.DirectoryScanner;
import com.github.frtu.simple.scan.SelectiveFileScannerObserver;
import com.github.frtu.simple.scan.observers.CumulativeFileScannerObserver;

public class DirectoryScannerTest {
	public static final File DIRECTORY = new File("src/test/resources/test_sequence");
	private static final String FILE1_TXT = "file1.txt";
	private static final String FILE2_TXT = "file2.txt";

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
	public void testScanDirectoryWithFileScanner() {
		CumulativeFileScannerObserver fileScanner = new CumulativeFileScannerObserver();
		DirectoryScanner directoryScanner = new DirectoryScanner(fileScanner);
		directoryScanner.setFileExtensionToFilter("xml", "json");
		directoryScanner.scanDirectory(DIRECTORY);

		ArrayList<File> resultsList = fileScanner.getResults();
		if (resultsList.isEmpty()) {
			fail("ATTENTION : Test case is not valid since the resource may not be here ?!?");
			return;
		}
		final List<String> result = Arrays.asList(new String[]{"file3.xml", "file4.json"});
		for (File file: resultsList) {
			assertTrue("Should only have xml and json", result.contains(file.getName()));
		}
	}

//	Remove order check
//	@Test
//	public void testScanDirectory() {
//		CumulativeFileScannerObserver fileScanner = new CumulativeFileScannerObserver();
//		DirectoryScanner directoryScanner = new DirectoryScanner(fileScanner);
//		directoryScanner.scanDirectory(DIRECTORY);
//
//		ArrayList<File> resultsList = fileScanner.getResults();
//		assertTrue("file1 should happen before file2", resultsList.get(1).getName().equals(FILE1_TXT));
//		assertTrue("file1 should happen before file2", resultsList.get(2).getName().equals(FILE2_TXT));
//	}

	@Test
	public void testSelectiveScanDirectory() {
		SelectiveFileScannerImplementation fileScanner = new SelectiveFileScannerImplementation();
		DirectoryScanner directoryScanner = new DirectoryScanner(fileScanner);
		directoryScanner.scanDirectory(DIRECTORY);
	}
}
