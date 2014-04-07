package org.frtu.simple.scan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.frtu.simple.scan.observers.CululativeFileScannerObserver;
import org.junit.Test;

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
		CululativeFileScannerObserver fileScanner = new CululativeFileScannerObserver();
		DirectoryScanner directoryScanner = new DirectoryScanner(fileScanner);
		directoryScanner.setFileExtensionToFilter("xml", "json");
		directoryScanner.scanDirectory(DIRECTORY);

		ArrayList<File> arrayList = fileScanner.getResults();
		if (arrayList.size() > 1) {
			assertTrue("Should only have xml and json", arrayList.get(0).getName().equals("file3.xml"));
			assertTrue("file1 should happen before file2", arrayList.get(1).getName().equals("file4.json"));
		} else {
			fail("ATTENTION : Test case is not valid since the resource may not be here ?!?");
		}
	}

	@Test
	public void testScanDirectory() {
		CululativeFileScannerObserver fileScanner = new CululativeFileScannerObserver();
		DirectoryScanner directoryScanner = new DirectoryScanner(fileScanner);
		directoryScanner.scanDirectory(DIRECTORY);

		ArrayList<File> arrayList = fileScanner.getResults();
		assertTrue("file1 should happen before file2", arrayList.get(1).getName().equals(FILE1_TXT));
		assertTrue("file1 should happen before file2", arrayList.get(2).getName().equals(FILE2_TXT));
	}

	@Test
	public void testSelectiveScanDirectory() {
		SelectiveFileScannerImplementation fileScanner = new SelectiveFileScannerImplementation();
		DirectoryScanner directoryScanner = new DirectoryScanner(fileScanner);
		directoryScanner.scanDirectory(DIRECTORY);
	}
}
