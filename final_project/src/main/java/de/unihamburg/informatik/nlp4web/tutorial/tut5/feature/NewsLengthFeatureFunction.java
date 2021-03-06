package de.unihamburg.informatik.nlp4web.tutorial.tut5.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.ml.Feature;
import org.cleartk.ml.feature.extractor.CleartkExtractorException;
import org.cleartk.ml.feature.extractor.FeatureExtractor1;
import org.cleartk.ml.feature.function.FeatureFunction;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

/**
 * Checks weather the word contains a dash ( - )
 * It is assumed that the provided Feature is a String representing a single word.
 */
public class NewsLengthFeatureFunction<T extends Annotation> implements FeatureExtractor1<T> {

	@Override
	public List<Feature> extract(JCas view, T focusAnnotation) throws CleartkExtractorException {
		long count = JCasUtil.selectCovered(Token.class, focusAnnotation)
				.stream()
				.count();
//		System.out.println(count);

		return Collections.singletonList(new Feature("NewsLength", count));
	}
}