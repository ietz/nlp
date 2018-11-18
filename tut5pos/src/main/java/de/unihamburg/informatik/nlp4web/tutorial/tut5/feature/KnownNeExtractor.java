package de.unihamburg.informatik.nlp4web.tutorial.tut5.feature;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.KnownNEAnnotation;
import org.apache.uima.jcas.JCas;
import org.cleartk.ml.Feature;
import org.cleartk.ml.feature.extractor.CleartkExtractorException;
import org.cleartk.ml.feature.extractor.FeatureExtractor2;
import org.cleartk.util.AnnotationUtil;

import java.util.Collections;
import java.util.List;

public class KnownNeExtractor implements FeatureExtractor2<Token, KnownNEAnnotation> {

	@Override
	public List<Feature> extract(JCas view, Token token, KnownNEAnnotation kne) throws CleartkExtractorException {
		if (AnnotationUtil.contains(kne, token)) {
			return Collections.singletonList(new Feature("KNE-" + kne.getTypeOf()));
		} else {
			return Collections.emptyList();
		}
	}
}
