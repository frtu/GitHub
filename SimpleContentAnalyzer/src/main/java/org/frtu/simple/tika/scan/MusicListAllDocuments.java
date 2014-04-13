package org.frtu.simple.tika.scan;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.frtu.simple.lucene.LuceneHandlerFactory;
import org.frtu.simple.lucene.SearchHandler;
import org.frtu.simple.tika.model.AudioItem;

public class MusicListAllDocuments {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File file = new File("target/audio_indexes");
		
		LuceneHandlerFactory luceneHandlerFactory = new LuceneHandlerFactory(file);
		
		SearchHandler searchHandler = luceneHandlerFactory.buildSearchHandler();
		for (int i = 0; i < searchHandler.getMaxDoc(); i++) {
			Document document = searchHandler.getDocById(i);
			
			AudioItem audioItem = new AudioItem(document);
			
			String fileName = document.get("fileName");
			System.out.println(audioItem);
		}
		
//		IndexReader reader = // create IndexReader
//				for (int i=0; i<reader.maxDoc(); i++) {
//				    if (reader.isDeleted(i))
//				        continue;
//
//				    Document doc = reader.document(i);
//				    String docId = doc.get("docId");
//
//				    // do something with docId here...
//				}
	}
}
