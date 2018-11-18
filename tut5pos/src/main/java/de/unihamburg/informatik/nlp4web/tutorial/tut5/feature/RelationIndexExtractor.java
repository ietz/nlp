package de.unihamburg.informatik.nlp4web.tutorial.tut5.feature;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.ml.Feature;
import org.cleartk.ml.feature.extractor.CleartkExtractorException;
import org.cleartk.ml.feature.extractor.FeatureExtractor1;
import org.cleartk.ml.feature.extractor.FeatureExtractor2;

import java.util.*;

public class RelationIndexExtractor<T extends Annotation, U extends Annotation> implements FeatureExtractor1<T> {
	private final Map<T, Collection<U>> index;
	private final FeatureExtractor2<T, U> ex;

	public RelationIndexExtractor(Map<T, Collection<U>> index, FeatureExtractor2<T, U> ex) {
		this.index = index;
		this.ex = ex;
	}

	@Override
	public List<Feature> extract(JCas view, T focusAnnotation) throws CleartkExtractorException {
		List<Feature> result = new ArrayList<>();
		for (U relatedAnnotation : index.getOrDefault(focusAnnotation, Collections.emptyList())) {
			result.addAll(ex.extract(view, focusAnnotation, relatedAnnotation));
		}
		return result;
	}
}
