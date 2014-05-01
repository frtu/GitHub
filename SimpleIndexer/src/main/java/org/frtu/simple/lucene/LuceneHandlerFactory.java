package org.frtu.simple.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class LuceneHandlerFactory {
	public final static Version DEFAULT_LUCENE_VERSION = Version.LUCENE_40;

	private Directory indexDirectory;
	private Version luceneVersion = DEFAULT_LUCENE_VERSION;

	/**
	 * Use the RAM as an the index storage. Index is not persisted when JVM shutdown.
	 */
	public LuceneHandlerFactory() {
		this(new RAMDirectory());
	}

	/**
	 * Create an index at the indicated folder
	 * 
	 * @param indexFolderPath
	 * @throws IOException
	 */
	public LuceneHandlerFactory(String indexFolderPath) {
		this(new File(indexFolderPath));
	}

	public LuceneHandlerFactory(File indexFolder) {
		super();
		try {
			this.indexDirectory = FSDirectory.open(indexFolder);
		} catch (IOException e) {
			throw new IllegalArgumentException("Cannot create/read index directory at location:" + indexFolder.getAbsolutePath(), e);
		}
	}

	private LuceneHandlerFactory(Directory indexDirectory) {
		super();
		this.indexDirectory = indexDirectory;
	}

	public Version getLuceneVersion() {
		return luceneVersion;
	}

	public void setLuceneVersion(Version luceneVersion) {
		this.luceneVersion = luceneVersion;
	}

	private IndexWriterConfig buildIndexWriterConfig() {
		OpenMode openMode = OpenMode.CREATE_OR_APPEND;
		IndexWriterConfig config = buildIndexWriterConfig(openMode);
		return config;
	}

	private IndexWriterConfig buildIndexWriterConfig(OpenMode openMode) {
		IndexWriterConfig config = new IndexWriterConfig(luceneVersion, buildAnalyzer());
		config.setOpenMode(openMode);
		return config;
	}

	private StandardAnalyzer buildAnalyzer() {
		StandardAnalyzer analyzer = new StandardAnalyzer(luceneVersion);
		return analyzer;
	}

	public IndexHandler buildIndexHandler(OpenMode openMode) {
		return new IndexHandler(indexDirectory, buildIndexWriterConfig(openMode));
	}

	public IndexHandler buildIndexHandler() {
		return new IndexHandler(indexDirectory, buildIndexWriterConfig());
	}

	IndexSearcher buildIndexSearcher() {
		IndexReader directoryReader;
		try {
			directoryReader = DirectoryReader.open(indexDirectory);
			IndexSearcher searcher = new IndexSearcher(directoryReader);
			return searcher;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	Query buildQuery(String fieldName, String querystr) throws ParseException {
		Query query = new QueryParser(luceneVersion, fieldName, buildAnalyzer()).parse(querystr);
		return query;
	}

	public SearchHandler buildSearchHandler() {
		return new SearchHandler(this, buildIndexSearcher());
	}
}
