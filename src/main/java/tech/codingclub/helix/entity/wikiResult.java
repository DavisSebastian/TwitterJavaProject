package tech.codingclub.helix.entity;

public class wikiResult {

    public String query;
    public String text_result;
    public String image_url;

    public String getQuery() {
        return query;
    }

    public String getText_result() {
        return text_result;
    }

    public String getImage_url() {
        return image_url;
    }

    public wikiResult(String query, String text_result, String image_url)
    {
        this.image_url=image_url;
        this.text_result=text_result;
        this.query=query;
    }

}
