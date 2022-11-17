package WebIR;

public class Article {
    public String title;
    public String author;
    public String affiliation;
    public String address;
    public String date;
    public String abKey;
    public String maintext;
    public String ackRef;
    public String url;// pdf url


    public double sort = 0;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAffiliation() {
        return affiliation;
    }
    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getAddress() {
        return address;
    }
    public void setAddressr(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getAbKey() {
        return abKey;
    }
    public void setAbKey(String abKey) {
        this.abKey = abKey;
    }

    public String getMaintext() {return maintext;}
    public void setMaintext(String maintext) {
        this.maintext = maintext;
    }

    public String getAckRef() {return ackRef;}
    public void setAckRef(String ackRef) {
        this.ackRef = ackRef;
    }

    public String getUrl() {return "oriPDFs/"+url;}
    public void setUrl(String url) {
        this.url = url;
    }


    public Article() {
    }

    public Article(String title, String author, String affiliation, String address,
                   String date, String abKey, String maintext, String ackRef, String url) {
        this.title = title;
        this.author = author;
        this.affiliation = affiliation;
        this.address = address;
        this.date = date;
        this.abKey = abKey;
        this.maintext = maintext;
        this.ackRef = ackRef;
        this.url = url;

    }
}
