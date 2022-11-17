package WebIR;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc;


public class Search {
    public static String indexpath = "D:\\1央财\\2022-2023第一学期\\课程\\信息组织与检索\\HW1\\index";  //lucene创建出的索引的存储位置
    public static String searchText;  //搜索词
    public static ScoreDoc[] resultDocs;  //结果集

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getIndexpath() {
        return indexpath;
    }

    public ScoreDoc[] WebSearch() throws ParseException, IOException {
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser qp = new QueryParser("MainText", analyzer);  //搜索域
        Query query = qp.parse(searchText);
        Directory dir = FSDirectory.open(Paths.get(indexpath));
        IndexReader inr = DirectoryReader.open(dir);
        IndexSearcher ins = new IndexSearcher(inr);
        //search
        TopDocs topDocs = ins.search(query, 100);  //展示100条结果
        //System.out.println("the number of hits: "+topDocs.totalHits);  //结果集总数
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;  //结果集
        if (scoreDocs != null){
            /*
            int count = 1;
            for (ScoreDoc scoreDoc : scoreDocs){
                int docID = scoreDoc.doc;  //lucene为文档分配的ID
                org.apache.lucene.document.Document doc = ins.doc(docID);
                System.out.println("===================================");
                System.out.println("第"+count+"名");
                System.out.println("title: "+doc.get("Title"));
                System.out.println("main text: "+doc.get("MainText"));
                count = count+1;
             */
            resultDocs = scoreDocs;
            }
        else {
            resultDocs = null;
        }
        return resultDocs;
    }
}
