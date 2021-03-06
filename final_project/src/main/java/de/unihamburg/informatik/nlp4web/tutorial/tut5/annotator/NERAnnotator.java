package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator;

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.cleartk.ml.CleartkSequenceAnnotator;
import org.cleartk.ml.Feature;
import org.cleartk.ml.Instance;
import org.cleartk.ml.feature.extractor.CleartkExtractor;
import org.cleartk.ml.feature.extractor.CleartkExtractor.Focus;
import org.cleartk.ml.feature.extractor.CleartkExtractor.Following;
import org.cleartk.ml.feature.extractor.CleartkExtractor.Preceding;
import org.cleartk.ml.feature.extractor.CombinedExtractor1;
import org.cleartk.ml.feature.extractor.CoveredTextExtractor;
import org.cleartk.ml.feature.extractor.FeatureExtractor1;
import org.cleartk.ml.feature.extractor.TypePathExtractor;
import org.cleartk.ml.feature.function.CapitalTypeFeatureFunction;
import org.cleartk.ml.feature.function.CharacterCategoryPatternFunction;
import org.cleartk.ml.feature.function.CharacterNgramFeatureFunction;
import org.cleartk.ml.feature.function.CharacterNgramFeatureFunction.Orientation;
import org.cleartk.ml.feature.function.FeatureFunctionExtractor;
import org.cleartk.ml.feature.function.LowerCaseFeatureFunction;
import org.cleartk.ml.feature.function.NumericTypeFeatureFunction;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.NEIOBAnnotation;


public class NERAnnotator extends CleartkSequenceAnnotator<String> {

    private Collection<CleartkExtractor<Token, Token>> getExtractors(JCas jCas) {
        TypePathExtractor<Token> stemExtractor = new TypePathExtractor<>(Token.class, "stem/value");

        FeatureExtractor1<Token> tokenTextFeatureExtractor = new FeatureFunctionExtractor<>(new CoveredTextExtractor<>(),
                new CapitalTypeFeatureFunction(), new NumericTypeFeatureFunction(), new LowerCaseFeatureFunction(),
                new CharacterNgramFeatureFunction(Orientation.LEFT_TO_RIGHT, 0, 3),
                new CharacterNgramFeatureFunction(Orientation.RIGHT_TO_LEFT, 0, 3),
                new CharacterCategoryPatternFunction<>(CharacterCategoryPatternFunction.PatternType.REPEATS_MERGED));

        FeatureExtractor1<Token> stemTextFeatureExtractor = new FeatureFunctionExtractor<>(stemExtractor,
                new CharacterNgramFeatureFunction(Orientation.LEFT_TO_RIGHT, 0, 3),
                new CharacterNgramFeatureFunction(Orientation.RIGHT_TO_LEFT, 0, 3));

        FeatureExtractor1<Token> stemDiffExtractor = (view, focusAnnotation) -> Collections.singletonList(
                new Feature("STEM_DIFF_LENGTH", focusAnnotation.getCoveredText().length() - focusAnnotation.getStemValue().length())
        );

//        FeatureExtractor1<Token> kneExtractor = new RelationIndexExtractor<>(
//                JCasUtil.indexCovering(jCas, Token.class, KnownNEAnnotation.class),
//                new KnownNeExtractor()
//        );

        FeatureExtractor1<Token> posExtractor = new TypePathExtractor<>(Token.class, "pos/PosValue");

        FeatureExtractor1<Token> baseFeatureExtractor = new CombinedExtractor1<>(
                tokenTextFeatureExtractor,
                stemTextFeatureExtractor
                /*,kneExtractor*/
        );

        FeatureExtractor1<Token> focusFeatureExtractor = new CombinedExtractor1<>(
                baseFeatureExtractor,
                stemExtractor,
                stemDiffExtractor
        );

        FeatureExtractor1<Token> contextFeatureExtractor = new CombinedExtractor1<>(
                baseFeatureExtractor,
                posExtractor
        );

        CleartkExtractor<Token, Token> context = new CleartkExtractor<>(Token.class, contextFeatureExtractor, new Preceding(2), new Following(2));
        CleartkExtractor<Token, Token> current = new CleartkExtractor<>(Token.class, focusFeatureExtractor, new Focus());

        return Arrays.asList(current, context);
    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        Collection<CleartkExtractor<Token, Token>> extractors = getExtractors(jCas);

        for (Sentence sentence : select(jCas, Sentence.class)) {
            List<Instance<String>> instances = new ArrayList<>();
            List<Token> tokens = selectCovered(jCas, Token.class, sentence);

            for (Token token : tokens) {
                Instance<String> instance = new Instance<>();
                for (CleartkExtractor<Token, Token> extractor : extractors) {
                    instance.addAll(extractor.extractWithin(jCas, token, sentence));
                }

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
                    NEIOBAnnotation namedEntity = JCasUtil.selectCovered(NEIOBAnnotation.class, token).get(0);
                    namedEntity.setPredictValue(namedEntities.get(i++));
                    namedEntity.addToIndexes();
                }
            }
        }
    }
}