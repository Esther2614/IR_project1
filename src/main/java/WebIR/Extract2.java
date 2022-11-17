package WebIR;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Extract2 {
    public static String inputpath = "D:\\1央财\\2022-2023第一学期\\课程\\信息组织与检索\\IR_project1\\src\\index\\xml";  //xml输入文件夹绝对路径
    public static String indexpath = "D:\\1央财\\2022-2023第一学期\\课程\\信息组织与检索\\IR_project1\\src\\index\\index";  //lucene创建出的索引的存储位置
    public static List<org.apache.lucene.document.Document> docList= new ArrayList<>();  //Lucene文档集合

    public static void main(String[] args) throws DocumentException, IOException {
        //删除原有索引
        File old_file = new File(indexpath);
//        File file = new File(inputpath);
//        for(File f: file.listFiles()){
//            String str = " "+f;
//            str = str.replace(".tei.xml",".pdf");
//            System.out.println(str.replace("xml","oriPDFs\\oriPDFs"));
//        }
        for (File f : old_file.listFiles()){
            if (f.exists()){
                f.delete();
            }
        }
        File file = new File(inputpath);
        //提取xml字段，并放入lucene的文档集合
        for(File f: file.listFiles()){
            extract(f);
        }
        //创建索引库
        createIndex();
    }

    public static void extract(File f) throws DocumentException, IOException {
        //dom4j解析xml
        SAXReader reader = new SAXReader();
        Document docXML = reader.read(f);
        //根节点
        Element root = docXML.getRootElement();

        //title(已完成)
        String title = "";
        //要用if语句判断，防止出现空指针
        if (root.element("teiHeader").element("fileDesc").
                element("titleStmt").elementText("title") != null){
            title = String.valueOf
                    (root.element("teiHeader").element("fileDesc").
                            element("titleStmt").elementText("title"));
        }
        title = title.trim();

        //fileDesc(3 contents: authors, affiliation, address)
        StringBuffer[] fileDesc = new StringBuffer[3];
        fileDesc[0] = new StringBuffer();  //authors
        fileDesc[1] = new StringBuffer();  //affiliation
        fileDesc[2] = new StringBuffer();  //address

        fileDesc = getFD(root.element("teiHeader").element("fileDesc").element("sourceDesc").
                element("biblStruct").element("analytic"), fileDesc);
        String authors = new String(fileDesc[0]).trim();
        authors =  authors.length()!=0 ? authors.substring(0,authors.length()-1) : authors;

        String affli = new String(fileDesc[1]).trim();
        affli = affli.length()!=0 ? affli.substring(0,affli.length()-1) : affli;

        String address = new String(fileDesc[2]).trim();
        address = address.length()!=0 ? address.substring(0,address.length()-1) : address;

        //date
        String date = "";
        if (root.element("teiHeader").element("fileDesc").element("sourceDesc").
                element("biblStruct").element("monogr").element("imprint") != null){
            date = String.valueOf
                    (root.element("teiHeader").element("fileDesc").element("sourceDesc").
                            element("biblStruct")
                            .element("monogr").element("imprint").elementTextTrim("date"));
        }
        date = date.trim();


        //profileDesc(2 contents: abstract & keywords)
        ArrayList profileDescList = new ArrayList<>();
        ArrayList<String> prof = getElementContent(root.element("teiHeader").element("profileDesc"),
                profileDescList);
        StringBuffer profileDesc1 = new StringBuffer();
        for(String s: prof) {
            profileDesc1.append(s);
            profileDesc1.append(" ");
        }
        String profileDesc = new String(profileDesc1).trim();

        //body(即正文)
        String body = "";
        if (root.element ("text").element("body") != null){
            ArrayList bodyList = new ArrayList<>();
            ArrayList<String> bd = getElementContent(root.element ("text").element("body"), bodyList);
            StringBuffer body1 = new StringBuffer();
            for(String s: bd) {
                body1.append(s);
                body1.append(" ");
            }
            body = new String(body1).trim();
        }

        //back(2 contents: acknowledge & references)
        ArrayList backList = new ArrayList<>();
        ArrayList<String> bk = getElementContent(root.element ("text").element("back"), backList);
        StringBuffer back1 = new StringBuffer();
        for(String s: bk) {
            back1.append(s);
            back1.append(" ");
        }
        String back = new String(back1).trim();

        //path
        String path = " "+f;
        String[] p =path.split("\\\\");
        path = p[p.length-1];
        path = path.replace(".tei.xml",".pdf");

        //放入lucene
        org.apache.lucene.document.Document docLuc = new org.apache.lucene.document.Document();
        docLuc.add(new TextField("Title", title, Field.Store.YES));
        docLuc.add(new TextField("Author", authors, Field.Store.YES));
        docLuc.add(new TextField("Affiliation", affli, Field.Store.YES));
        docLuc.add(new TextField("Address", address, Field.Store.YES));
        docLuc.add(new TextField("Date", date, Field.Store.YES));
        docLuc.add(new TextField("AbstractAndKeyWords", profileDesc, Field.Store.YES));
        docLuc.add(new TextField("MainText", body, Field.Store.YES));
        docLuc.add(new TextField("AcknowledgementAndReferences", back, Field.Store.YES));
        docLuc.add(new TextField("Path", path, Field.Store.YES));
        docList.add(docLuc);

/*
        //此段为print出txt，方便检查
        StringBuffer pathname=new StringBuffer(outputpath);  //输出文件夹绝对路径
        pathname.append(f.getName());
        pathname.append(".txt");

        File writeName = new File(new String(pathname));
        writeName.createNewFile();
        FileWriter writer = new FileWriter(writeName);
        BufferedWriter out = new BufferedWriter(writer);
        out.write("title is:\n"+title);
        out.write("\n\nauthors are:\n"+authors);
        out.write("\n\naffiliations are:\n"+affli);
        out.write("\n\naddresses are:\n"+address);
        out.write("\n\ndate is:\n"+date);
        out.write("\n\nabstract&keywords are:\n"+profileDesc);
        out.write("\n\nbody is:\n"+body);
        out.write("\n\nack and references are:\n"+back);
        out.close();
        writer.close();
*/
    }

    //创建索引库
    public static void createIndex() throws IOException {
        Analyzer analyzer = new StandardAnalyzer();  //创建分词器
        Directory dir = FSDirectory.open(Paths.get(indexpath));  //目录对象
        IndexWriterConfig config= new IndexWriterConfig(analyzer);  //使用分词器
        IndexWriter inw = new IndexWriter(dir, config);  //输出流对象
        for (org.apache.lucene.document.Document d: docList){
            inw.addDocument(d);
        }
        inw.close();
        System.out.println("===========索引添加成功===========");
    }


    //对某元素下所有节点进行遍历，并提取所有内容，递归
    public static ArrayList getElementContent(Element element, ArrayList result) {
        List elements = element.elements();
        if (elements.size() == 0) {
            //没有子元素
            //String xpath = element.getPath();
            String value = element.getTextTrim();
            result.add(value);
        } else {
            //有子元素
            result.add(element.getTextTrim());
            for (Iterator it = elements.iterator(); it.hasNext();) {
                Element elem = (Element) it.next();
                //递归遍历
                getElementContent(elem, result);
            }
        }
        return result;
    }

    //提取fileDesc中的authors, affli, adress，递归
    public static StringBuffer[] getFD(Element element, StringBuffer[] result) {
        List elements = element.elements();
        if (elements.size() == 0) {
            //没有子元素
            addFD(element, result);
        } else {
            //有子元素
            addFD(element, result);
            for (Iterator it = elements.iterator(); it.hasNext();) {
                Element elem = (Element) it.next();
                //递归遍历
                getFD(elem, result);
            }
        }
        return result;
    }

    //提取fileDesc中的authors, affli, adress的具体方式
    public static void addFD(Element e, StringBuffer[] result){
        String name = e.getName();
        if(name.equals("persName")){
            ArrayList temp1 = new ArrayList<>();
            ArrayList<String> au = getElementContent(e, temp1);
            StringBuffer author = new StringBuffer();
            for (String s: au){
                author.append(s);
                author.append(" ");
            }
            result[0].append(author);
            result[0].append(", ");
        }
        else if(name.equals("orgName")){
            result[1].append(e.getTextTrim());
            result[1].append(", ");
        }
        else if(name.equals("address")){
            ArrayList temp2 = new ArrayList<>();
            ArrayList<String> ad = getElementContent(e, temp2);
            StringBuffer address = new StringBuffer();
            for (String s: ad){
                address.append(s);
                address.append(" ");
            }
            result[2].append(address);
            result[2].append(", ");
        }
    }
}
