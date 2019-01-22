package de.unihamburg.informatik.nlp4web.tutorial.tut5.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.ml.Feature;
import org.cleartk.ml.feature.extractor.CleartkExtractorException;
import org.cleartk.ml.feature.extractor.FeatureExtractor1;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class MaxTokenLengthFeatureExtractor<T extends Annotation> implements FeatureExtractor1<T> {

	@Override
	public List<Feature> extract(JCas view, T focusAnnotation) throws CleartkExtractorException {
		List<Token> tokens = new ArrayList<>();
		tokens = JCasUtil.selectCovered(Token.class, focusAnnotation);

		OptionalInt maxlength = tokens.stream()
        .map(Token::getCoveredText)
        .mapToInt(String::length)
        .max();
		
		int max = maxlength.orElse(0);
//		

		return Collections.singletonList(new Feature("MaxLength", max));
	}

}
