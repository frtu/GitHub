package org.frtu.simple.tika.scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.frtu.simple.lucene.LuceneHandlerFactory;
import org.frtu.simple.lucene.SearchHandler;
import org.frtu.simple.tika.catalog.CatalogRepository;
import org.frtu.simple.tika.model.AudioItem;

public class MusicListAllDocuments {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File file = new File("target/audio_indexes");
		
		CatalogRepository catalogRepository = new CatalogRepository();
		
		
		LuceneHandlerFactory luceneHandlerFactory = new LuceneHandlerFactory(file);
		SearchHandler searchHandler = luceneHandlerFactory.buildSearchHandler();
		int maxDoc = searchHandler.getMaxDoc();
		System.out.println("There is " + maxDoc + " document(s)");
		for (int i = 0; i < maxDoc; i++) {
			Document document = searchHandler.getDocById(i);

			AudioItem audioItem = new AudioItem(document);
			catalogRepository.addAudioItem(audioItem);

			// String fileName = document.get("fileName");
			// System.out.println(audioItem);
		}
		HashMap<String, HashSet<String>> catalogNames = catalogRepository.getAlbumNames();
		for (Iterator iterator = catalogNames.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			Set<String> keySet = catalogNames.get(key);
			for (String value : keySet) {
				System.out.println(key + " - " + value);
			}
		}

		// IndexReader reader = // create IndexReader
		// for (int i=0; i<reader.maxDoc(); i++) {
		// if (reader.isDeleted(i))
		// continue;
		//
		// Document doc = reader.document(i);
		// String docId = doc.get("docId");
		//
		// // do something with docId here...
		// }
	}
}
