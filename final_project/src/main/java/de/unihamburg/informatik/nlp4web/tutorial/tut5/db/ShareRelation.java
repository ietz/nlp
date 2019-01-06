package de.unihamburg.informatik.nlp4web.tutorial.tut5.db;

public class ShareRelation {
	private final long newsId;
	private final long userId;
	private final long count;

	public ShareRelation(long newsId, long userId, long count) {
		this.newsId = newsId;
		this.userId = userId;
		this.count = count;
	}

	public long getNewsId() {
		return newsId;
	}

	public long getUserId() {
		return userId;
	}

	public long getCount() {
		return count;
	}
}
