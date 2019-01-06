package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator;

import static org.apache.uima.fit.util.JCasUtil.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.cleartk.ml.CleartkSequenceAnnotator;
import org.cleartk.ml.Instance;
import org.cleartk.ml.feature.extractor.CoveredTextExtractor;
import org.cleartk.ml.feature.extractor.FeatureExtractor1;
import org.cleartk.ml.feature.extractor.TypePathExtractor;
import org.cleartk.ml.feature.function.FeatureFunctionExtractor;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.feature.NewsLengthFeatureFunction;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation;


public class FeatureAnnotator extends CleartkSequenceAnnotator<String> {

    private Collection<FeatureExtractor1<FakeNewsAnnotation>> getExtractors(JCas jCas) {

        FeatureExtractor1<FakeNewsAnnotation> idExtractor = new TypePathExtractor<>(FakeNewsAnnotation.class, "id");
        FeatureExtractor1<FakeNewsAnnotation> sourceExtractor = new TypePathExtractor<>(FakeNewsAnnotation.class, "source");
        FeatureExtractor1<FakeNewsAnnotation> shareCountExtractor = new TypePathExtractor<>(FakeNewsAnnotation.class, "shareCount");

        // covered text wirklich sinnvoll?
        FeatureExtractor1<FakeNewsAnnotation> newsTextFeatureExtractor = new FeatureFunctionExtractor<>(new CoveredTextExtractor<>(),
        			new NewsLengthFeatureFunction()
        		);
        
        // nicht sinnvoll, da mehrere Autoren in einem String getrennt mit "; " z.B. "tim; anna; ..."
        FeatureExtractor1<FakeNewsAnnotation> authorsExtractor = new TypePathExtractor<>(FakeNewsAnnotation.class, "authors");
        
        return Arrays.asList(idExtractor, sourceExtractor, shareCountExtractor, newsTextFeatureExtractor, authorsExtractor);
    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        Collection<FeatureExtractor1<FakeNewsAnnotation>> extractors = getExtractors(jCas);
        
        // wähle FakeNewsAnnotation
        // es gibt nur eine FakeNewsAnnotation pro jCas!
        for(FakeNewsAnnotation fnAnnotation : select(jCas, FakeNewsAnnotation.class)) {
            List<Instance<String>> instances = new ArrayList<>();
        	Instance<String> instance = new Instance<>();
        	
        	// wähle alle Tokens in der FakeNewsAnnotation
        	// erzeuge eine Instance mit allen Features aus der FakeNewsAnnotation
        	for(FeatureExtractor1<FakeNewsAnnotation> extractor : extractors) {
        		instance.addAll(extractor.extract(jCas, fnAnnotation));
        	}
        	
        	// Lösung für erzeugte Instance hinzufügen
            if (this.isTraining()) {
                instance.setOutcome(Boolean.toString(fnAnnotation.getGoldValue()));
            }        
            // Instanz hinzufügen
            instances.add(instance); // add the instance to the list !!!
        	
        	// trainiere oder klassifiziere mit den instances
            if (this.isTraining()) {
                this.dataWriter.write(instances);
            } else {
                List<String> fakeOrRealNews = this.classify(instances);
                fnAnnotation.setPredictedValue(Boolean.parseBoolean(fakeOrRealNews.get(0)));
                fnAnnotation.addToIndexes();
            }
        }
    }
}