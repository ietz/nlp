package de.unihamburg.informatik.nlp4web.tutorial.tut5.feature;

import org.cleartk.ml.Feature;
import org.cleartk.ml.feature.function.FeatureFunction;

import java.util.Collections;
import java.util.List;

public class StringLengthFeatureFunction implements FeatureFunction {
	@Override
	public List<Feature> apply(Feature input) {
		Object value = input.getValue();
		if (value instanceof String) {
			String s = (String) value;
			return Collections.singletonList(new Feature("STRING_LENGTH", s.length()));
		} else {
			return Collections.emptyList();
		}
	}
}
