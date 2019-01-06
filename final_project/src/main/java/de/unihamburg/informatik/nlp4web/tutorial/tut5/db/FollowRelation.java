package de.unihamburg.informatik.nlp4web.tutorial.tut5.db;

public class FollowRelation {

	private final long fromUserId;
	private final long toUserId;

	public FollowRelation(long fromUserId, long toUserId) {
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
	}

	public long getFromUserId() {
		return fromUserId;
	}

	public long getToUserId() {
		return toUserId;
	}

}
