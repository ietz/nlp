package de.unihamburg.informatik.nlp4web.tutorial.tut5.db;

public class Share {

	private final UserModel user;
	private final NewsModel news;
	private final long count;

	public Share(UserModel user, NewsModel news, long count) {
		this.user = user;
		this.news = news;
		this.count = count;
	}

	public UserModel getUser() {
		return user;
	}

	public NewsModel getNews() {
		return news;
	}

	public long getCount() {
		return count;
	}
}
