package de.unihamburg.informatik.nlp4web.tutorial.tut5.db;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.util.TrainTestSplit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FakeNewsNet {

    private final double traintest = 0.8;

	private List<UserModel> users;
	private List<NewsModel> news;


	public static FakeNewsNet load(String url) {
		return new FakeNewsNet(url);
	}

	private FakeNewsNet(String url) {
		DBUtils dbUtils = new DBUtils(url);
		Map<Long, UserModel> userIndex = index(dbUtils.selectAllUsers(), UserModel::getId);
		Map<Long, NewsModel> newsIndex = index(dbUtils.selectAllNews(), NewsModel::getId);

		for (FollowRelation followRelation : dbUtils.selectAllFollowRelations()) {
			UserModel from = userIndex.get(followRelation.getFromUserId());
			UserModel to = userIndex.get(followRelation.getToUserId());

			from.addFollowing(to);
			to.addFollower(from);
		}

		for (ShareRelation shareRelation : dbUtils.selectAllShareRelations()) {
			UserModel user = userIndex.get(shareRelation.getUserId());
			NewsModel news = newsIndex.get(shareRelation.getNewsId());

//			// TODO: Remove when DB is fixed
//			if (user == null || news == null) {
//				continue;
//			}

			Share share = new Share(user, news, shareRelation.getCount());
			user.addShare(share);
			news.addShare(share);
		}

		this.users = new ArrayList<>(userIndex.values());
		this.news = new ArrayList<>(newsIndex.values());
	}


	public List<UserModel> getUsers() {
		return users;
	}

	public List<NewsModel> getNews() {
		return news;
	}


	private static <S, T> Map<S, T> index(List<T> items, Function<T, S> keyMapper) {
		return items.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
	}

	public List<NewsModel> getTrueNews() {
		return this.getNews().stream().filter(NewsModel::isReal).collect(Collectors.toList());
	}

	public List<NewsModel> getFakeNews() {
		return this.getNews().stream().filter(newsModel -> !newsModel.isReal()).collect(Collectors.toList());
	}

	public List<NewsModel> getTrain() {
        TrainTestSplit<NewsModel> trueSplit = new TrainTestSplit<>(getTrueNews(), traintest);
        TrainTestSplit<NewsModel> falseSplit = new TrainTestSplit<>(getFakeNews(), traintest);

        ArrayList<NewsModel> result = new ArrayList<>(trueSplit.train);
        result.addAll(falseSplit.train);
        return result;
    }

    public List<NewsModel> getTest() {
        TrainTestSplit<NewsModel> trueSplit = new TrainTestSplit<>(getTrueNews(), traintest);
        TrainTestSplit<NewsModel> falseSplit = new TrainTestSplit<>(getFakeNews(), traintest);

        ArrayList<NewsModel> result = new ArrayList<>(trueSplit.test);
        result.addAll(falseSplit.test);
        return result;
    }

}
