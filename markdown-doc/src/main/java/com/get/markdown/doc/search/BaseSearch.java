package com.get.markdown.doc.search;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ReferenceManager;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;

public abstract class BaseSearch {

	protected ReferenceManager<IndexSearcher> searcherManager;
	protected IndexWriter indexWriter;

	/**
	 * 当前实体的索引名
	 * 
	 * @return
	 */
	protected String getIndexName() {
		return this.getClass().getSimpleName();
	}

	protected IndexSearcher getIndexSearcher() {
		IndexSearcher is = null;
		if (searcherManager == null) {
			Directory directory = SearchUtils.getDirectory(getIndexName());
			try {
				searcherManager = new SearcherManager(directory, new SearcherFactory());
				is = searcherManager.acquire();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				searcherManager.maybeRefresh();//刷新searcherManager,获取最新的IndexSearcher
				is = searcherManager.acquire();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return is;
		
	}

	protected IndexWriter getLuceneWriter() {
		if (indexWriter == null) {
			Directory directory = SearchUtils.getDirectory(getIndexName());
			IndexWriterConfig writerConfig = new IndexWriterConfig(new StandardAnalyzer());
			try {
				indexWriter = new IndexWriter(directory, writerConfig);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return indexWriter;
	}

}
