package de.unihamburg.informatik.nlp4web.tutorial.tut5.db;

import java.util.ArrayList;
import java.util.List;

public class NewsModel {
	private Long id;
	
	private String authors;
	private String keywords;
	private long publishDate;
	private boolean real;
	private String source;
	private String text;
	private String title;
	private String url;
	private List<Share> shares = new ArrayList<>();
	
	public NewsModel(Long id, String authors, String keywords, long publishDate, boolean real, String source,
			String text, String title, String url) {
		this.id = id;
		this.authors = authors;
		this.keywords = keywords;
		this.publishDate = publishDate;
		this.real = real;
		this.source = source;
		this.text = text.replaceAll("(\\r\\n|\\n)", " ");
		this.title = title;
		this.url = url;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public long getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(long publishDate) {
		this.publishDate = publishDate;
	}
	public boolean isReal() {
		return real;
	}
	public void setReal(boolean real) {
		this.real = real;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public List<Share> getShares() {
		return shares;
	}

	public void addShare(Share share) {
		shares.add(share);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("--NEWS--\n");
		builder.append("--NEWSID--\t"+id+"\n");
		builder.append("--AUTHORS--\t"+authors+"\n");
		builder.append("--KEYWORDS--\t"+keywords+"\n");
		builder.append("--REAL--\t"+real+"\n");
		builder.append("--SOURCE--\t"+source+"\n");
		builder.append("--TITLE--\t"+title+"\n");
		builder.append("--TEXT--\t"+text+"\n");
		builder.append("--WEBURL--\t"+url+"\n");
		return builder.toString();
	}
}
