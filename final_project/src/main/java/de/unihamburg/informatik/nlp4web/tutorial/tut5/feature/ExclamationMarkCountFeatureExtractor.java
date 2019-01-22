package de.unihamburg.informatik.nlp4web.tutorial.tut5.feature;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.ml.Feature;
import org.cleartk.ml.feature.extractor.CleartkExtractorException;
import org.cleartk.ml.feature.extractor.FeatureExtractor1;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class ExclamationMarkCountFeatureExtractor<T extends Annotation> implements FeatureExtractor1<T> {
	
	@Override
	public List<Feature> extract(JCas view, T focusAnnotation) throws CleartkExtractorException {
		long count = JCasUtil.selectCovered(Token.class, focusAnnotation)
				.stream()
				.filter(this::isExclamationMark)
				.count();

		return Collections.singletonList(new Feature("ExclamationMarkCount", count));
	}

	private boolean isExclamationMark(Annotation a) {
		String text = a.getCoveredText();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (!(c == '!')) {
				return false;
			}
		}
		return true;
	}

}
