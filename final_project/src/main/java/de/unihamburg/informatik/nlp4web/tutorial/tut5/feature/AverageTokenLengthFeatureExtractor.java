package de.unihamburg.informatik.nlp4web.tutorial.tut5.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.ml.Feature;
import org.cleartk.ml.feature.extractor.CleartkExtractorException;
import org.cleartk.ml.feature.extractor.FeatureExtractor1;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class AverageTokenLengthFeatureExtractor <T extends Annotation> implements FeatureExtractor1<T> {

	@Override
	public List<Feature> extract(JCas view, T focusAnnotation) throws CleartkExtractorException {
		List<Token> tokens = new ArrayList<>();
		tokens = JCasUtil.selectCovered(Token.class, focusAnnotation);
		int totalTokenLength = 0;
		for(Token token : tokens) {
			totalTokenLength += token.getCoveredText().length();
		}
		
		int averageTokenLength = totalTokenLength/tokens.size();
//		System.out.println(averageTokenLength);
	

		return Collections.singletonList(new Feature("AverageTokenLength", averageTokenLength));
	}
}
