package de.unihamburg.informatik.nlp4web.tutorial.tut5.util;

public class OnlineRequestObject {

    private String title;
    private String body;

    public OnlineRequestObject() {
    }

    public OnlineRequestObject(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
