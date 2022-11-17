package WebIR;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Search2 {
    public static String indexpath = "D:\\1央财\\2022-2023第一学期\\课程\\信息组织与检索\\IR_project1\\src\\index\\index";  //lucene创建出的索引的存储位置

    public String getIndexpath() {
        return indexpath;
    }

    public static void main(String[] args) throws ParseException, IOException {
        System.out.print("请输入搜索关键词:");
        Scanner input = new Scanner(System.in);
        String searchText = input.next();
        //普通索引，多项联合
//        normalsearch(searchText);
        //短语索引测试
//        List<String> aList= Arrays.asList(searchText.split(" "));
//        testsearch(aList,"Title");
        //普通索引测试
//        testsearch(searchText,"Title");
        //span测试
//        String[] searchText1 = ;
//        System.out.println(searchText1);
        String[] searchText1 = searchText.split(",");
        testsearch(searchText1,"Title");
    }
    public static void testsearch (String[] searchText, String field) throws IOException, ParseException {
        //创建分词器
        Analyzer analyzer = new StandardAnalyzer();

        String[] fields = { "Title", "MainText", "Author", "Affiliation" };
        //普通索引
//        QueryParser qp = new QueryParser(field, analyzer);
//        Query query = qp.parse(searchText);

//        FuzzyQuery query = (FuzzyQuery) qp.parse(searchText);

        //span搜索
        //初始化
        String queryStringStart = "";  //起始单词
        String queryStringEnd = "";    //终止单词
        String excludeString = "";     //排除词
        Boolean order = false;         //顺序要求
        int slop = 1;

        if (searchText.length >=1){
            queryStringStart = searchText[0];  //起始单词
        }
        if (searchText.length >=2){
            queryStringEnd = searchText[1];    //终止单词
        }
        if (searchText.length >=3){
            if (searchText[2].equalsIgnoreCase("false")){
                order = false;
            }
            else if (searchText[2].equalsIgnoreCase("true")){
                order = true;
            }
        }
        if (searchText.length >=4){
            slop = Integer.parseInt(searchText[3]);//相隔位数
        }
        if (searchText.length >= 5) {
            excludeString = searchText[4];
        }

        SpanQuery queryStart = new SpanTermQuery(new Term(field, queryStringStart));
        SpanQuery queryEnd = new SpanTermQuery(new Term(field, queryStringEnd));
        SpanQuery spanNearQuery = new SpanNearQuery(new SpanQuery[]{queryStart, queryEnd}, slop, order);
        //添加限制
        SpanQuery excludeQuery = new SpanTermQuery(new Term(field, excludeString));
        SpanNotQuery query = new SpanNotQuery(spanNearQuery, excludeQuery);
        //短语索引
//        PhraseQuery.Builder querybuilder = new PhraseQuery.Builder();
//        for (int i = 0; i < queryList.size(); i++) {
//            querybuilder.add(new Term(field, queryList.get(i).toString()));
//        }
//        querybuilder.setSlop(3);
//        PhraseQuery query = querybuilder.build();

        //模糊索引
//        Term term = new Term(field,searchText);
//        FuzzyQuery query = new FuzzyQuery(term);
        //正则索引
//        Term term = new Term(field,searchText);
//        RegexpQuery query = new RegexpQuery(term);
        //根据索引目录创建一个对象
        Directory dir = FSDirectory.open(Paths.get(indexpath));
        //读取索引文件
        IndexReader inr = DirectoryReader.open(dir);
        //创建索引查询器
        IndexSearcher ins = new IndexSearcher(inr);
        //返回得分最高的100条结果
        TopDocs topDocs = ins.search(query, 100);
        //输出结果总个数
        //System.out.println("the number of hits: "+topDocs.totalHits);
        //结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        if (scoreDocs != null){
            int count = 1;
            for (ScoreDoc scoreDoc : scoreDocs){
                int docID = scoreDoc.doc;  //lucene为文档分配的ID
                //根据索引ID找到源文档
                Document doc = ins.doc(docID);
                String Title = doc.get("Title");
                String Authors = doc.get("Author");
                String Affiliation = doc.get("Affiliation");
                String Address = doc.get("Address");
                String Date = doc.get("Date");
                String AbstractAndKeyWords = doc.get("AbstractAndKeyWords");
                String MainText = doc.get("MainText");
                String AcknowledgementAndReferences = doc.get("AcknowledgementAndReferences");
                System.out.println("===================================");
                System.out.println("第"+count+"名");
                System.out.println("title: "+Title);
                System.out.println("main text: "+MainText);

                count = count+1;
            }
        }
    }

    //普通全域搜索
    public static ArrayList<Article> NormalSearch(String key) {
        ArrayList<Article> newsList = new ArrayList<Article>();
        Directory directory = null;
        try {
            File f = new File(indexpath);
            if (!f.exists()) {
                f.mkdirs();
            }

            directory = FSDirectory.open(f.toPath());
            DirectoryReader dReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(dReader);
            String[] fields = { "Title", "Author", "Affiliation", "Address",
                    "Date", "AbstractAndKeyWords", "MainText", "AcknowledgementAndReferences" };

            Analyzer analyzer = new StandardAnalyzer();
            MultiFieldQueryParser parser2 = new MultiFieldQueryParser(fields, analyzer);
            Query query2 = parser2.parse(key);
            TopDocs topDocs = searcher.search(query2, 500);

            if (topDocs != null) {
                for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                    Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
                    String path = doc.get("Path").trim();  //获取路径

                    HLandFrag hf = new HLandFrag();
                    String[] hlResult = hf.getHighlight(query2, doc);

                    Article news = new Article(hlResult[0], hlResult[1], hlResult[2], hlResult[3],
                            hlResult[4], hlResult[5], hlResult[6], hlResult[7], path);
                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }

    //普通单域搜索
    public static ArrayList<Article> SingleFieldSearch(String key,String field) {
        ArrayList<Article> newsList = new ArrayList<Article>();
        Directory directory = null;
        try {
            File f = new File(indexpath);
            if (!f.exists()) {
                f.mkdirs();
            }

            directory = FSDirectory.open(f.toPath());
            DirectoryReader dReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(dReader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser qp = new QueryParser(field, analyzer);
            Query query2 = qp.parse(key);

            TopDocs topDocs = searcher.search(query2, 500);
            if (topDocs != null) {

                for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                    Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
                    String path = doc.get("Path").trim();  //获取路径

                    HLandFrag hf = new HLandFrag();
                    String[] hlResult = hf.getHighlight(query2, doc);

                    Article news = new Article(hlResult[0], hlResult[1], hlResult[2], hlResult[3],
                            hlResult[4], hlResult[5], hlResult[6], hlResult[7], path);
                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }

/*
    //支持短语查询的分域搜索
    public static ArrayList<Article> Phrase_SingleFieldSearch(String key,String field) {
        ArrayList<Article> newsList = new ArrayList<Article>();
        Directory directory = null;
        try {
            File f = new File(indexpath);
            if (!f.exists()) {
                f.mkdirs();
            }

            directory = FSDirectory.open(f.toPath());
            DirectoryReader dReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(dReader);

            Term parser2 = new Term(field, key);
            WildcardQuery query2 = new WildcardQuery(parser2);

            TopDocs topDocs = searcher.search(query2, 500);
            if (topDocs != null) {

                for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                    Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
                    String path = doc.get("Path").trim();  //获取路径

                    HLandFrag hf = new HLandFrag();
                    String[] hlResult = hf.getHighlight(query2, doc);

                    Article news = new Article(hlResult[0], hlResult[1], hlResult[2], hlResult[3],
                            hlResult[4], hlResult[5], hlResult[6], hlResult[7], path);
                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }
*/

    //支持通配符查询的分域搜索
    public static ArrayList<Article> Wildcard_SingleFieldSearch(String key,String field) {
        ArrayList<Article> newsList = new ArrayList<Article>();
        Directory directory = null;
        try {
            File f = new File(indexpath);
            if (!f.exists()) {
                f.mkdirs();
            }

            directory = FSDirectory.open(f.toPath());
            DirectoryReader dReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(dReader);

            Term parser2 = new Term(field, key);
            WildcardQuery query2 = new WildcardQuery(parser2);

            TopDocs topDocs = searcher.search(query2, 500);
            if (topDocs != null) {

                for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                    Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
                    String path = doc.get("Path").trim();  //获取路径

                    HLandFrag hf = new HLandFrag();
                    String[] hlResult = hf.getHighlight(query2, doc);

                    Article news = new Article(hlResult[0], hlResult[1], hlResult[2], hlResult[3],
                            hlResult[4], hlResult[5], hlResult[6], hlResult[7], path);
                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }

    //支持模糊搜索的分域搜索
    public static ArrayList<Article> Fuzzy_SingleFieldSearch(String key,String field) {
        ArrayList<Article> newsList = new ArrayList<Article>();
        Directory directory = null;
        try {
            File f = new File(indexpath);
            if (!f.exists()) {
                f.mkdirs();
            }

            directory = FSDirectory.open(f.toPath());
            DirectoryReader dReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(dReader);

            Term parser2 = new Term(field, key);
            FuzzyQuery query2 = new FuzzyQuery(parser2);

            TopDocs topDocs = searcher.search(query2, 500);
            if (topDocs != null) {
                for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                    Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
                    String path = doc.get("Path");  //获取路径

                    HLandFrag hf = new HLandFrag();
                    String[] hlResult = hf.getHighlight(query2, doc);

                    Article news = new Article(hlResult[0], hlResult[1], hlResult[2], hlResult[3],
                            hlResult[4], hlResult[5], hlResult[6], hlResult[7], path);
                    newsList.add(news);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }

    //正则搜索
    public static ArrayList<Article> Regexp_SingleFieldSearch(String key,String field) {
        ArrayList<Article> newsList = new ArrayList<Article>();
        Directory directory = null;
        try {
            File f = new File(indexpath);
            if (!f.exists()) {
                f.mkdirs();
            }

            directory = FSDirectory.open(f.toPath());
            DirectoryReader dReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(dReader);

            key = ".*"+ key +".*";
            Term parser2 = new Term(field, key);
            RegexpQuery query2 = new RegexpQuery(parser2);

            TopDocs topDocs = searcher.search(query2, 500);
            if (topDocs != null) {
                for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                    Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
                    String path = doc.get("Path");  //获取路径

                    HLandFrag hf = new HLandFrag();
                    String[] hlResult = hf.getHighlight(query2, doc);

                    Article news = new Article(hlResult[0], hlResult[1], hlResult[2], hlResult[3],
                            hlResult[4], hlResult[5], hlResult[6], hlResult[7], path);
                    newsList.add(news);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }

    //func-未加限制的跨度查询
    public static ArrayList<Article> Span_SingleFieldSearch(String[] searchText,String field) {
        //searchText按序输入起始词，结尾词，是否有序（True则起始词一定在结尾词前），两词之间最大间隔，排除词
        //searchText最少两位，起码要有起始词和结尾词

        ArrayList<Article> newsList = new ArrayList<Article>();
        Directory directory = null;
        try {
            File f = new File(indexpath);
            if (!f.exists()) {
                f.mkdirs();
            }

            //初始化
            String queryStringStart = "";  //起始单词
            String queryStringEnd = "";    //终止单词
            String excludeString = "";     //排除词
            Boolean order = false;         //顺序要求
            int slop = 1;
            directory = FSDirectory.open(f.toPath());
            DirectoryReader dReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(dReader);

            if (searchText.length >=1){
                queryStringStart = searchText[0];  //起始单词
            }
            if (searchText.length >=2){
                queryStringEnd = searchText[1];    //终止单词
            }
            if (searchText.length >=3){
                if (searchText[2].equalsIgnoreCase("false")){
                    order = false;
                }
                else if (searchText[2].equalsIgnoreCase("true")){
                    order = true;
                }
            }
            if (searchText.length >=4){
                slop = Integer.parseInt(searchText[3]);//相隔位数
            }
            if (searchText.length >= 5) {
                excludeString = searchText[4];
            }

            SpanQuery queryStart = new SpanTermQuery(new Term(field, queryStringStart));
            SpanQuery queryEnd = new SpanTermQuery(new Term(field, queryStringEnd));
            SpanQuery spanNearQuery = new SpanNearQuery(new SpanQuery[]{queryStart, queryEnd}, slop, order);
            //添加限制
            SpanQuery excludeQuery = new SpanTermQuery(new Term(field, excludeString));
            SpanNotQuery query2 = new SpanNotQuery(spanNearQuery, excludeQuery);

            TopDocs topDocs = searcher.search(query2, 500);
            if (topDocs != null) {
                for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                    Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
                    String path = doc.get("Path");  //获取路径

                    HLandFrag hf = new HLandFrag();
                    String[] hlResult = hf.getHighlight(query2, doc);

                    Article news = new Article(hlResult[0], hlResult[1], hlResult[2], hlResult[3],
                            hlResult[4], hlResult[5], hlResult[6], hlResult[7], path);
                    newsList.add(news);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }
}