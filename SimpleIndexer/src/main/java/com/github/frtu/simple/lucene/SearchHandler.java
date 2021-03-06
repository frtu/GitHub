package com.github.frtu.simple.lucene;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;

public class SearchHandler {
	private LuceneHandlerFactory luceneHandlerFactory;
	private IndexSearcher searcher;

	public SearchHandler(LuceneHandlerFactory luceneHandlerFactory, IndexSearcher searcher) {
		super();
		this.luceneHandlerFactory = luceneHandlerFactory;
		this.searcher = searcher;
	}

	public int getMaxDoc() {
		IndexReader indexReader = searcher.getIndexReader();
		int maxDoc = indexReader.maxDoc();
		return maxDoc;
	}

	public Document getDocById(int docId) {
		Document document;
		try {
			document = searcher.doc(docId);
		} catch (IOException e) {
			throw new IllegalStateException("Cannot read doc from searcher with docId=" + docId, e);
		}
		return document;
	}

	public Collector search(String fieldName, String querystr, Collector collector) throws ParseException, IOException {
		Query query = luceneHandlerFactory.buildQuery(fieldName, querystr);
		searcher.search(query, collector);
		return collector;
	}

	public ScoreDoc[] searchDocByTopScore(String fieldName, String querystr, int hitsPerPage) throws ParseException, IOException {
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		search(fieldName, querystr, collector);
		TopDocs topDocs = collector.topDocs();
		ScoreDoc[] hits = topDocs.scoreDocs;
		return hits;
	}

}
