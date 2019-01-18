package de.unihamburg.informatik.nlp4web.tutorial.tut5.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TrainTestSplit<T> {

    private static final double SPLIT = 0.8;

    public final List<T> train;
    public final List<T> test;




    public static <T> TrainTestSplit<T> split(List<T> complete) {
        return new TrainTestSplit<>(complete);
    }

    private TrainTestSplit(List<T> complete) {
    	List<T> dataset = new ArrayList<>(complete);
		Collections.shuffle(dataset, new Random(7323));

        int index = (int) Math.floor(dataset.size() * SPLIT);
        this.train = dataset.subList(0, index);
        this.test = dataset.subList(index, dataset.size());
    }
}
