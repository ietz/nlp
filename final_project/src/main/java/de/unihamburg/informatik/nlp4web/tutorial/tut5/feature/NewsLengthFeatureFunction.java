package de.unihamburg.informatik.nlp4web.tutorial.tut5.feature;

import java.util.Collections;
import java.util.List;

import org.cleartk.ml.Feature;
import org.cleartk.ml.feature.function.FeatureFunction;

/**
 * Checks weather the word contains a dash ( - )
 * It is assumed that the provided Feature is a String representing a single word.
 */
public class NewsLengthFeatureFunction implements FeatureFunction {

	@Override
	public List<Feature> apply(Feature input) {	
		String newsText = (String) input.getValue();	
		return Collections.singletonList(new Feature("newsLength_"+newsText.length()));	
	}
}