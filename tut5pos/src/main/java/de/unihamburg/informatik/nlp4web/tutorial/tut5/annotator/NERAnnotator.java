package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator;

import static org.apache.uima.fit.util.JCasUtil.indexCovering;
import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.util.*;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.feature.KnownNeExtractor;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.KnownNEAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.ml.CleartkSequenceAnnotator;
import org.cleartk.ml.Instance;
import org.cleartk.ml.feature.extractor.*;
import org.cleartk.ml.feature.extractor.CleartkExtractor.Following;
import org.cleartk.ml.feature.extractor.CleartkExtractor.Preceding;
import org.cleartk.ml.feature.function.*;
import org.cleartk.ml.feature.function.CharacterNgramFeatureFunction.Orientation;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.NEIOBAnnotation;


public class NERAnnotator extends CleartkSequenceAnnotator<String> {

    private List<FeatureExtractor1<Token>> features = new ArrayList<>();
    private FeatureExtractor2<Token, KnownNEAnnotation> kneExtractor;

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);

        TypePathExtractor<Token> stemExtractor = new TypePathExtractor<>(Token.class, "stem/value");

        FeatureExtractor1<Token> tokenFeatureExtractor = new FeatureFunctionExtractor<>(new CoveredTextExtractor<>(),
                new LowerCaseFeatureFunction(), new CapitalTypeFeatureFunction(), new NumericTypeFeatureFunction(),
                new CharacterNgramFeatureFunction(Orientation.RIGHT_TO_LEFT, 0, 2));

        CleartkExtractor<Token, Token> contextFeatureExtractor = new CleartkExtractor<>(Token.class,
                tokenFeatureExtractor, new Preceding(2), new Following(2));

        features.add(stemExtractor);
        features.add(tokenFeatureExtractor);
        features.add(contextFeatureExtractor);

        kneExtractor = new KnownNeExtractor();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        for (Sentence sentence : select(jCas, Sentence.class)) {
            List<Instance<String>> instances = new ArrayList<>();
            List<Token> tokens = selectCovered(jCas, Token.class, sentence);
            Map<Token, Collection<KnownNEAnnotation>> kneIndex = indexCovering(jCas, Token.class, KnownNEAnnotation.class);

            for (Token token : tokens) {

                Instance<String> instance = new Instance<>();

                for (FeatureExtractor1<Token> extractor : this.features) {
                    if (extractor instanceof CleartkExtractor) {
                        instance.addAll(
                                (((CleartkExtractor<Token, Token>) extractor).extractWithin(jCas, token, sentence)));
                    } else {
                        instance.addAll(extractor.extract(jCas, token));
                    }
                }

                for (KnownNEAnnotation kne : kneIndex.getOrDefault(token, Collections.emptyList())) {
                    instance.addAll(kneExtractor.extract(jCas, token, kne));
                }

                if (this.isTraining()) {
                    NEIOBAnnotation goldNE = JCasUtil.selectCovered(jCas, NEIOBAnnotation.class, token).get(0);
                    instance.setOutcome(goldNE.getGoldValue());
                }

                // add the instance to the list !!!
                instances.add(instance);
            }
            // differentiate between training and classifying
            if (this.isTraining()) {
                this.dataWriter.write(instances);
            } else {
                List<String> namedEntities = this.classify(instances);
                int i = 0;
                for (Token token : tokens) {
                    NEIOBAnnotation namedEntity = new NEIOBAnnotation(jCas, token.getBegin(), token.getEnd());
                    namedEntity.setPredictValue(namedEntities.get(i++));
                    namedEntity.addToIndexes();
                }
            }
        }

    }

}