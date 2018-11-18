package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator;

import static org.apache.uima.fit.util.JCasUtil.indexCovering;
import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.util.*;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.feature.KnownNeExtractor;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.feature.RelationIndexExtractor;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.KnownNEAnnotation;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.cleartk.ml.CleartkSequenceAnnotator;
import org.cleartk.ml.Instance;
import org.cleartk.ml.feature.extractor.*;
import org.cleartk.ml.feature.extractor.CleartkExtractor.Focus;
import org.cleartk.ml.feature.extractor.CleartkExtractor.Following;
import org.cleartk.ml.feature.extractor.CleartkExtractor.Preceding;
import org.cleartk.ml.feature.function.*;
import org.cleartk.ml.feature.function.CharacterNgramFeatureFunction.Orientation;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.NEIOBAnnotation;


public class NERAnnotator extends CleartkSequenceAnnotator<String> {

    private CleartkExtractor<Token, Token> getExtractor(JCas jCas) {
        TypePathExtractor<Token> stemExtractor = new TypePathExtractor<>(Token.class, "stem/value");

        FeatureExtractor1<Token> tokenTextFeatureExtractor = new FeatureFunctionExtractor<>(new CoveredTextExtractor<>(),
                new LowerCaseFeatureFunction(), new CapitalTypeFeatureFunction(), new NumericTypeFeatureFunction(),
                new CharacterNgramFeatureFunction(Orientation.RIGHT_TO_LEFT, 0, 2));

        FeatureExtractor1<Token> kneExtractor = new RelationIndexExtractor<>(
                JCasUtil.indexCovering(jCas, Token.class, KnownNEAnnotation.class),
                new KnownNeExtractor()
        );

        CombinedExtractor1<Token> tokenFeatureExtractor = new CombinedExtractor1<>(
                stemExtractor,
                tokenTextFeatureExtractor,
                kneExtractor
        );

        return new CleartkExtractor<>(Token.class,
                tokenFeatureExtractor, new Preceding(2), new Focus(), new Following(2));
    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        CleartkExtractor<Token, Token> extractor = getExtractor(jCas);

        for (Sentence sentence : select(jCas, Sentence.class)) {
            List<Instance<String>> instances = new ArrayList<>();
            List<Token> tokens = selectCovered(jCas, Token.class, sentence);

            for (Token token : tokens) {
                Instance<String> instance = new Instance<>();
                instance.addAll(extractor.extractWithin(jCas, token, sentence));

                if (this.isTraining()) {
                    NEIOBAnnotation goldNE = JCasUtil.selectCovered(jCas, NEIOBAnnotation.class, token).get(0);
                    instance.setOutcome(goldNE.getGoldValue());
                }

                instances.add(instance);
            }

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