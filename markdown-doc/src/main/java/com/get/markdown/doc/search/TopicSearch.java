package com.get.markdown.doc.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.get.markdown.doc.entity.vo.Page;

@Service
public class TopicSearch extends BaseSearch {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected Analyzer getTopicAnalyzer() {
		//为Topic做中文分词处理
		Map<String,Analyzer> analyzerPerField = new HashMap<>();
		analyzerPerField.put("topicNameSmartChinese", new SmartChineseAnalyzer());
		analyzerPerField.put("topicContentMarkdownSmartChinese", new SmartChineseAnalyzer());
		PerFieldAnalyzerWrapper aWrapper = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerPerField);
		return aWrapper;
	}
	
	protected IndexWriter getLuceneWriter() {
		if (indexWriter == null) {
			Directory directory = SearchUtils.getDirectory(getIndexName());
			
			IndexWriterConfig writerConfig = new IndexWriterConfig(getTopicAnalyzer());
			try {
				indexWriter = new IndexWriter(directory, writerConfig);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return indexWriter;
	}
	
	/**
	 * 添加索引
	 * @param topicId
	 * @param topicContentId
	 * @param topicName
	 * @param topicContentMarkdown
	 * @return
	 */
	public boolean addIndex(Integer topicId, Integer topicContentId, String topicUri, String topicName, String topicContentMarkdown) {
		Document doc = new Document();
		//StringField被索引不被分词，整个值被看作为一个单独的token而被索引;TextField既被索引又被分词，但是没有词向量。
		doc.add(new Field("topicId", topicId+"", StringField.TYPE_STORED));
		doc.add(new Field("topicUri", topicUri, StringField.TYPE_STORED));
		doc.add(new Field("topicName", topicName, TextField.TYPE_STORED));
		doc.add(new Field("topicNameSmartChinese", topicName, TextField.TYPE_NOT_STORED));
		
		if (topicContentId != null) {
			doc.add(new Field("topicContentId", topicContentId+"", StringField.TYPE_STORED));
		}
		if (topicContentMarkdown != null) {
			doc.add(new Field("topicContentMarkdown", topicContentMarkdown, TextField.TYPE_STORED));
			doc.add(new Field("topicContentMarkdownSmartChinese", topicContentMarkdown, TextField.TYPE_NOT_STORED));
		}
		try {
			IndexWriter indexWriter = getLuceneWriter();
			indexWriter.addDocument(doc);
			indexWriter.flush();
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 删除全部索引
	 * @return
	 */
	public boolean deleteAll() {
		try {
			getLuceneWriter().deleteAll();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 分页查询，最多翻5页
	 * @param key
	 * @param pageNum 页码，从1开始
	 * @param pageSize 每页的记录数
	 */
	public Page searchPage(String searchKey, Integer pageNum, Integer pageSize) {
		searchKey = searchKey.toLowerCase();
		Page page = new Page(pageNum, pageSize);
		List<Object> resultList = new ArrayList<Object> ();
		page.setRecordList(resultList);
		IndexSearcher searcher = getIndexSearcher();
		
		Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("topicName", searchKey)), Occur.SHOULD);
		builder.add(new TermQuery(new Term("topicContentMarkdown", searchKey)), Occur.SHOULD);
		
		//为了提高准确性，对中文分词的两个字段采用短语查询（PhraseQuery）
		List<String> phraseList = analyzeBySmartChinese(searchKey);
		int pi = 0;
		PhraseQuery.Builder pq1 = new PhraseQuery.Builder();
		for (String phrase : phraseList) {
			pq1.add(new Term("topicNameSmartChinese", phrase), pi);
			pi++;
		}
		//短语之间相隔10个字符内
		pq1.setSlop(10);
		builder.add(pq1.build(), Occur.SHOULD);
		PhraseQuery.Builder pq2 = new PhraseQuery.Builder();
		for (String phrase : phraseList) {
			pq2.add(new Term("topicContentMarkdownSmartChinese", phrase), pi);
			pi++;
		}
		//短语之间相隔10个字符内
		pq2.setSlop(10);
		builder.add(pq2.build(), Occur.SHOULD);
		
		BooleanQuery query = builder.build();
//		Query query = new TermQuery(new Term("topicNameSmartChinese", searchKey));
		
		// 高亮显示关键字
		Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter("<font color='red'>", "</font>"), 
				new QueryScorer(query));
		//限定返回的高亮文字段字符长度不超过200
		Fragmenter fragmenter = new SimpleFragmenter(500);
        highlighter.setTextFragmenter(fragmenter);
        Analyzer analyzer = getTopicAnalyzer();
		try {
			TopDocs rs = searcher.search(query, pageSize*5);
			int numTotalHits = rs.totalHits;
			page.setTotal(numTotalHits);
			logger.info("搜索：searchKey={}，检索到记录数：{}", searchKey, numTotalHits);
		    int start = (pageNum-1)*pageSize;
		    int end = Math.min(numTotalHits, start+pageSize);
			for (int i = start; i < end; i++) {
				Document oneHit = searcher.doc(rs.scoreDocs[i].doc);
				Map<String, String> oneMap = new HashMap<String, String>();
//				for (IndexableField field : oneHit.getFields()) {
//					oneMap.put(field.name(), field.stringValue());
//				}
				oneMap.put("topicId", oneHit.get("topicId"));
				oneMap.put("topicUri", oneHit.get("topicUri"));
				//topicName高亮
				String topicNameHl = highlighter.getBestFragment(analyzer, "topicNameSmartChinese", oneHit.get("topicName"));
				if (topicNameHl == null) {
					topicNameHl = highlighter.getBestFragment(analyzer, "topicName", oneHit.get("topicName"));
				}
				if (topicNameHl == null) {
					oneMap.put("topicName", oneHit.get("topicName"));
				} else {
					oneMap.put("topicName", topicNameHl);
				}
				//topicContentMarkdown高亮
				String topicContentMarkdownHl = highlighter.getBestFragment(analyzer, "topicContentMarkdownSmartChinese", oneHit.get("topicContentMarkdown"));
				if (topicContentMarkdownHl == null) {
					topicContentMarkdownHl = highlighter.getBestFragment(analyzer, "topicContentMarkdown", oneHit.get("topicContentMarkdown"));
				}
				if (topicContentMarkdownHl == null) {
					oneMap.put("topicContentMarkdown", oneHit.get("topicContentMarkdown"));
				} else {
					oneMap.put("topicContentMarkdown", topicContentMarkdownHl);
				}
				resultList.add(oneMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}
	
	/**
	 * 删除单个索引
	 * @param topicId
	 */
	public boolean deleteByTopicId(Integer topicId) {
		try {
			IndexWriter indexWriter = getLuceneWriter();
			indexWriter.deleteDocuments(new Term("topicId", topicId+""));
			indexWriter.flush();
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 添加索引
	 * @param topicId
	 * @param topicContentId
	 * @param topicName
	 * @param topicContentMarkdown
	 * @return
	 */
	public boolean editIndex(Integer topicId, Integer topicContentId, String topicUri, String topicName, String topicContentMarkdown) {
		Document doc = new Document();
		//StringField被索引不被分词，整个值被看作为一个单独的token而被索引;TextField既被索引又被分词，但是没有词向量。
		doc.add(new Field("topicId", topicId+"", StringField.TYPE_STORED));
		doc.add(new Field("topicUri", topicUri, StringField.TYPE_STORED));
		doc.add(new Field("topicName", topicName, TextField.TYPE_STORED));
		doc.add(new Field("topicNameSmartChinese", topicName, TextField.TYPE_NOT_STORED));
		
		if (topicContentId != null) {
			doc.add(new Field("topicContentId", topicContentId+"", StringField.TYPE_STORED));
		}
		if (topicContentMarkdown != null) {
			doc.add(new Field("topicContentMarkdown", topicContentMarkdown, TextField.TYPE_STORED));
			doc.add(new Field("topicContentMarkdownSmartChinese", topicContentMarkdown, TextField.TYPE_NOT_STORED));
		}
		try {
			IndexWriter indexWriter = getLuceneWriter();
			//先删除
			indexWriter.deleteDocuments(new Term("topicId", topicId+""));
			//再增加
			indexWriter.addDocument(doc);
			indexWriter.flush();
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	protected List<String> analyzeBySmartChinese(String str) {
		List<String> result = new ArrayList<String>();
	    try { 
			Analyzer analyzer = new SmartChineseAnalyzer();
	        TokenStream tokenStream = analyzer.tokenStream("field", str);    
	        CharTermAttribute term=tokenStream.addAttribute(CharTermAttribute.class);    
	        tokenStream.reset();       
	        while( tokenStream.incrementToken() ){        
	            result.add( term.toString() );       
	        }       
	        tokenStream.end();       
	        tokenStream.close();   
	    } catch (IOException e1) {    
	        e1.printStackTrace();
	    }
	    return result;
	}
	
	public static void main(String[] args) {
		
	}
	
}
