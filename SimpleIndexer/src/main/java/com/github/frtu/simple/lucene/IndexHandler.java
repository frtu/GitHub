package com.github.frtu.simple.lucene;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

public class IndexHandler {
	private Directory indexDirectory;
	private IndexWriterConfig config;

	// If not null, then this index will be reused till this class is finalized
	private IndexWriter longLivingIndexWriter;

	public static interface IndexWriterCallback {
		public Object doIt(IndexWriter indexWriter) throws IOException;
	}

	public IndexHandler(Directory indexDirectory, IndexWriterConfig config) {
		this(indexDirectory, config, false);
	}

	public IndexHandler(Directory indexDirectory, IndexWriterConfig config, boolean isKeepIndexOpen) {
		super();
		this.indexDirectory = indexDirectory;
		this.config = config;

		if (isKeepIndexOpen) {
			longLivingIndexWriter = buildIndexWriter();
		}
	}

	public void execute(IndexWriterCallback indexWriterCallback) {
		IndexWriter tempIndexWriter = null;
		try {
			if (longLivingIndexWriter != null) {
				// Use longLivingIndexWriter instead of reopen it every time
				indexWriterCallback.doIt(longLivingIndexWriter);
			} else {
				// Use a new one
				tempIndexWriter = buildIndexWriter();
				indexWriterCallback.doIt(tempIndexWriter);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Cannot execute callback action on this indexWriter", e);
		} finally {
			// Close only tempIndexWriter not the long living one
			IOUtils.closeQuietly(tempIndexWriter);
		}
	}

	private IndexWriter buildIndexWriter() {
		IndexWriter newIndexWriter = null;
		try {
			newIndexWriter = new IndexWriter(indexDirectory, config);
			return newIndexWriter;
		} catch (IOException e) {
			IOUtils.closeQuietly(newIndexWriter);
			throw new IllegalStateException(e);
		}
	}

	public void addDocument(final Document luceneDocument) {
		if (longLivingIndexWriter != null) {
			try {
				longLivingIndexWriter.addDocument(luceneDocument);
			} catch (IOException e) {
				throw new IllegalStateException("Cannot addDocument on longLivingIndexWriter with document:" + luceneDocument, e);
			}
		} else {
			IndexWriterCallback indexWriterCallback = new IndexWriterCallback() {
				public Object doIt(IndexWriter indexWriter) throws IOException {
					indexWriter.addDocument(luceneDocument);
					return null;
				}
			};
			execute(indexWriterCallback);
		}
	}

	@PreDestroy
	@Override
	public void finalize() throws Throwable {
		super.finalize();
		IOUtils.closeQuietly(longLivingIndexWriter);
	}
}
