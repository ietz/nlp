package de.unihamburg.informatik.nlp4web.tutorial.tut5.util;

import java.util.List;

public class TrainTestSplit<T> {
    public final List<T> train;
    public final List<T> test;

    public TrainTestSplit(List<T> complete, double split) {
        int index = (int) Math.floor(complete.size() * split);
        this.train = complete.subList(0, index);
        this.test = complete.subList(index, complete.size());
    }
}
