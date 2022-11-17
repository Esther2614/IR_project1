package WebIR;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.*;

import java.io.IOException;
import java.io.StringReader;

public class HLandFrag {
//暂时采用标准分词器，注意！！！！

    public String[] getHighlight(Query query, Document doc) throws InvalidTokenOffsetsException, IOException {
        String Title= doc.get("Title");
        String Author= doc.get("Author");
        String Affiliation= doc.get("Affiliation");
        String Address= doc.get("Address");
        String Date= doc.get("Date");
        String AbstractAndKeyWords= doc.get("AbstractAndKeyWords");
        String MainText= doc.get("MainText");
        String AcknowledgementAndReferences= doc.get("AcknowledgementAndReferences");
        String[] raw = new String[]{Title, Author, Affiliation, Address, Date,
                AbstractAndKeyWords, MainText, AcknowledgementAndReferences};

        QueryScorer scorer = new QueryScorer(query);
        Fragmenter fragment = new SimpleSpanFragmenter(scorer);
        SimpleHTMLFormatter fors = new SimpleHTMLFormatter("<span style=\"color:red;\">", "</span>");
        Highlighter highlighter = new Highlighter(fors, scorer);
        highlighter.setTextFragmenter(fragment);

        String[] result = new String[raw.length];
        int index = 0;

        for(String s: raw){
            TokenStream tokenStream = new StandardAnalyzer().tokenStream(s, new StringReader(s));
            String hl = highlighter.getBestFragment(tokenStream, s);
            result[index] = hl != null ? hl: s;
            index = index + 1;
        }
        return result;
    }
}
