package org.frtu.simple.catalog;

import java.io.File;
import java.util.List;

import org.frtu.simple.tika.model.AudioItem;
import org.frtu.simple.tika.scan.MusicScanner;

public class CatalogBuilder {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CatalogBuilder.class);

	public static CatalogRepository buildCatalogFromDirectory(File mediaFolder) {
		List<AudioItem> scannedItems = MusicScanner.scanFolder(mediaFolder);
		logger.info("Scanned folder={} and found {} item(s).", mediaFolder.getAbsolutePath(), scannedItems.size());

		CatalogRepository catalogRepository = new CatalogRepository();
		for (AudioItem audioItem : scannedItems) {
			catalogRepository.addAudioItem(audioItem);
		}
		return catalogRepository;
	}

	public static CatalogRepository buildCatalogFromIndex(File indexDirectory) {
		CatalogRepository catalogRepository = new CatalogRepository(indexDirectory);
		catalogRepository.loadAllItems();
		return catalogRepository;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// In Memory
		File mediaFolder = new File("C:\\_Media_\\Music");
		CatalogRepository catalogRepository = buildCatalogFromDirectory(mediaFolder);
		catalogRepository.printCatalog(System.out);

		// Persistence
		File indexDirectory = new File("target/audio_indexes");
		CatalogRepository catalogRepository2 = buildCatalogFromIndex(indexDirectory);
		catalogRepository2.printCatalog(System.out);
	}

}
