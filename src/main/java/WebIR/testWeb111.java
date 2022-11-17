package WebIR;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class search
 */
@WebServlet("/searchWeb")
public class testWeb111 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int perpagecount = 10;
    private static ArrayList<Article> articleList;
    private String searchType;
    private String searchField;

    public testWeb111(){
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String queryString = request.getParameter("queryString");  //获取输入
        //System.out.println("test+"+request.getParameter("MultiField"));

        long starTime = System.currentTimeMillis();// start time

        if (queryString != null && !" ".equals(queryString)) {
            String pageNum = request.getParameter("p");
            if(pageNum == null){
                Search2 search2 = new Search2();
                searchType = request.getParameter("SearchRange")==null ? searchType : request.getParameter("SearchRange");
                searchField = request.getParameter("MultiField")==null ? searchField : request.getParameter("MultiField");
                //System.out.println(searchType+"  "+searchField);

                if(searchType.equals("0")){
                    System.out.println("1");
                    articleList = search2.NormalSearch(queryString); //普通全域搜索
                }
                else if(searchType.equals("Normal")){
                    System.out.println(searchField);
                    articleList = search2.SingleFieldSearch(queryString, searchField); //普通单域搜索
                }
                else if(searchType.equals("Wildcard")){
                    articleList = search2.Wildcard_SingleFieldSearch(queryString, searchField); //通配符搜索
                }
                else if (searchType.equals("Fuzzy")) {
                    articleList = search2.Fuzzy_SingleFieldSearch(queryString, searchField); //模糊搜索
                }
                else if(searchType.equals("Span")){
                    String[] queryStringArray = queryString.split(",");
                    int index = 0;
                    for(String s: queryStringArray){
                        queryStringArray[index] = s.trim();
                        index = index +1;
                    }
                    articleList = search2.Span_SingleFieldSearch(queryStringArray, searchField); //模糊搜索
                }
                else if(searchType.equals("Regexp")){
                    articleList = search2.Regexp_SingleFieldSearch(queryString, searchField); //正则搜索
                }
            }
            int hitsNum = articleList.size();

            int p = pageNum == null ? 1 : Integer.parseInt(pageNum);
            //System.out.println("p:" + p);

            Page page = new Page(p, hitsNum / perpagecount + 1, perpagecount, hitsNum,
                    perpagecount * (p - 1), perpagecount * p, true, p == 1 ? false : true);
            //System.out.println("pagetostring: "+page.toString());

            int endpagecount = perpagecount * p;// ҳ����ʱ������λ��
            if (hitsNum < endpagecount) {
                endpagecount = hitsNum;
            }
            List<Article> pagelist = articleList.subList(perpagecount * (p - 1), endpagecount);

            session.setAttribute("totaln", hitsNum);
            session.setAttribute("articlelist", pagelist);
            session.setAttribute("queryback", queryString);
            long endTime = System.currentTimeMillis();// end time
            long Time = endTime - starTime;
            session.setAttribute("time", ((double) Time / 1000));
            session.setAttribute("page", page);

            request.getRequestDispatcher("/result.jsp").forward(request, response);  //跳转页面
            //response.sendRedirect("/result.jsp");
        }
        else if(queryString == null){
            ////无查询结果页面
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}