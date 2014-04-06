package org.frtu.simple.lucene;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

public class IndexHandler {
	private Directory indexDirectory;
	private IndexWriterConfig config;

	public static interface IndexWriterCallback {
		public Object doIt(IndexWriter indexWriter);
	}

	public IndexHandler(Directory indexDirectory, IndexWriterConfig config) {
		super();
		this.indexDirectory = indexDirectory;
		this.config = config;
	}

	public void execute(IndexWriterCallback indexWriterCallback) {
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(indexDirectory, config);
			indexWriterCallback.doIt(indexWriter);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			IOUtils.closeQuietly(indexWriter);
		}
	}
}
