package de.unihamburg.informatik.nlp4web.tutorial.tut5.db;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
	private final long id;
	private final String name;
	private final List<UserModel> following = new ArrayList<>();
	private final List<UserModel> followers = new ArrayList<>();
	private final List<Share> shares = new ArrayList<>();

	public UserModel(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<UserModel> getFollowing() {
		return following;
	}

	public void addFollowing(UserModel user) {
		this.following.add(user);
	}

	public List<UserModel> getFollowers() {
		return followers;
	}

	public void addFollower(UserModel user) {
		this.followers.add(user);
	}

	public List<Share> getShares() {
		return shares;
	}

	public void addShare(Share share) {
		this.shares.add(share);
	}
}
