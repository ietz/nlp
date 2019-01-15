package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator;

import static org.apache.uima.fit.util.JCasUtil.select;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.feature.CountAnnotationExtractor;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.xpath.operations.Bool;
import org.cleartk.ml.*;
import org.cleartk.ml.feature.extractor.*;
import org.cleartk.ml.feature.function.FeatureFunctionExtractor;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.feature.NewsLengthFeatureFunction;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation;
import org.cleartk.ml.feature.transform.InstanceStream;
import org.cleartk.ml.feature.transform.extractor.CentroidTfidfSimilarityExtractor;
import org.cleartk.ml.feature.transform.extractor.MinMaxNormalizationExtractor;
import org.cleartk.ml.feature.transform.extractor.TfidfExtractor;
import org.cleartk.ml.libsvm.LibSvmStringOutcomeDataWriter;


public class FeatureAnnotator extends CleartkAnnotator<String> {

    public static final String PARAM_TF_IDF_URI = "tfIdfUri";
    public static final String PARAM_TF_IDF_CENTROID_SIMILARITY_URI = "tfIdfCentroidSimilarityUri";
    public static final String PARAM_ZMUS_URI = "zmusUri";
    public static final String PARAM_MINMAX_URI = "minmaxUri";
    public static final String PARAM_DIRECTORY_NAME = "modelOutputDir";
    public static final String TFIDF_EXTRACTOR_KEY = "Token";
    public static final String CENTROID_TFIDF_SIM_EXTRACTOR_KEY = "CentroidTfIdfSimilarity";
    public static final String MINMAX_EXTRACTOR_KEY = "MINMAXFeatures";
    @ConfigurationParameter(
            name = PARAM_TF_IDF_URI,
            mandatory = false,
            description = "provides a URI where the tf*idf map will be written")
    protected URI tfIdfUri;
    @ConfigurationParameter(
            name = PARAM_TF_IDF_CENTROID_SIMILARITY_URI,
            mandatory = false,
            description = "provides a URI where the tf*idf centroid data will be written")
    protected URI tfIdfCentroidSimilarityUri;

    @ConfigurationParameter(
            name = PARAM_MINMAX_URI,
            mandatory = false,
            description = "provides a URI where the min-max feature normalizaation data will be written")
    protected URI minmaxUri;

    @ConfigurationParameter(name = PARAM_DIRECTORY_NAME,
            mandatory = false)
    private File modelOutputDir;

    public static URI createTokenTfIdfDataURI(File outputDirectoryName) {
        File f = new File(outputDirectoryName, TFIDF_EXTRACTOR_KEY + "_tfidf_extractor.dat");
        return f.toURI();
    }

    public static URI createIdfCentroidSimilarityDataURI(File outputDirectoryName) {
        File f = new File(outputDirectoryName, CENTROID_TFIDF_SIM_EXTRACTOR_KEY);
        return f.toURI();
    }

    public static URI createMinMaxDataURI(File outputDirectoryName) {
        File f = new File(outputDirectoryName, MINMAX_EXTRACTOR_KEY + "_minmax_extractor.dat");
        return f.toURI();
    }

    private CombinedExtractor1<FakeNewsAnnotation> extractor;

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        initExtractor();
    }

    private void initExtractor() {

        try {
            TfidfExtractor<String, FakeNewsAnnotation> tfIdfExtractor = initTfIdfExtractor();
            CentroidTfidfSimilarityExtractor<String, FakeNewsAnnotation> simExtractor = initCentroidTfIdfSimilarityExtractor();
            MinMaxNormalizationExtractor<String, FakeNewsAnnotation> minmaxExtractor = initMinMaxExtractor();

            // covered text wirklich sinnvoll?
            FeatureExtractor1<FakeNewsAnnotation> newsTextFeatureExtractor = new FeatureFunctionExtractor<>(new CoveredTextExtractor<>(),
                    new NewsLengthFeatureFunction()
            );


            /** Collecting all features in a CombinedExtractor1<T> **/
            CombinedExtractor1 combinedExtractor1 = new CombinedExtractor1<FakeNewsAnnotation>(
                    tfIdfExtractor,
                    simExtractor,
                    minmaxExtractor
                    //new TypePathExtractor<>(FakeNewsAnnotation.class, "id"),
                    //new TypePathExtractor<>(FakeNewsAnnotation.class, "source"),
                    //new TypePathExtractor<>(FakeNewsAnnotation.class, "shareCount"),
                    //new TypePathExtractor<>(FakeNewsAnnotation.class, "shareUserCount"),
                    //new TypePathExtractor<>(FakeNewsAnnotation.class, "maxUserShareCount"),
//                    newsTextFeatureExtractor
            );

            this.extractor = combinedExtractor1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TfidfExtractor<String, FakeNewsAnnotation> initTfIdfExtractor() throws IOException {
        CleartkExtractor<FakeNewsAnnotation, Token> countsExtractor = new CleartkExtractor<FakeNewsAnnotation, Token>(
                Token.class,
                new CoveredTextExtractor<Token>(),
                new CleartkExtractor.Count(new CleartkExtractor.Covered()));

        TfidfExtractor<String, FakeNewsAnnotation> tfIdfExtractor = new TfidfExtractor<String, FakeNewsAnnotation>(
                FeatureAnnotator.TFIDF_EXTRACTOR_KEY,
                countsExtractor);

        if (this.tfIdfUri != null) {
            tfIdfExtractor.load(this.tfIdfUri);
        }
        return tfIdfExtractor;
    }


    private CentroidTfidfSimilarityExtractor<String, FakeNewsAnnotation> initCentroidTfIdfSimilarityExtractor()
            throws IOException {
        CleartkExtractor<FakeNewsAnnotation, Token> countsExtractor = new CleartkExtractor<FakeNewsAnnotation, Token>(
                Token.class,
                new CoveredTextExtractor<Token>(),
                new CleartkExtractor.Count(new CleartkExtractor.Covered()));

        CentroidTfidfSimilarityExtractor<String, FakeNewsAnnotation> simExtractor = new CentroidTfidfSimilarityExtractor<String, FakeNewsAnnotation>(
                FeatureAnnotator.CENTROID_TFIDF_SIM_EXTRACTOR_KEY,
                countsExtractor);

        if (this.tfIdfCentroidSimilarityUri != null) {
            simExtractor.load(this.tfIdfCentroidSimilarityUri);
        }
        return simExtractor;
    }


    private MinMaxNormalizationExtractor<String, FakeNewsAnnotation> initMinMaxExtractor()
            throws IOException {
        CombinedExtractor1<FakeNewsAnnotation> featuresToNormalizeExtractor = new CombinedExtractor1<FakeNewsAnnotation>(
                new CountAnnotationExtractor<FakeNewsAnnotation>(Sentence.class),
                new CountAnnotationExtractor<FakeNewsAnnotation>(Token.class));

        MinMaxNormalizationExtractor<String, FakeNewsAnnotation> minmaxExtractor = new MinMaxNormalizationExtractor<String, FakeNewsAnnotation>(
                MINMAX_EXTRACTOR_KEY,
                featuresToNormalizeExtractor);

        if (this.minmaxUri != null) {
            minmaxExtractor.load(this.minmaxUri);
        }

        return minmaxExtractor;
    }

    /**
     * Recursively going through all annotated news.
     * During training we write each news instance in the modelOutputDir
     * alone with its gold value. The written instances are then
     * read by the collectFeatures method to transform and train the data.
     **/

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {

        System.err.println("extracting");
        for (FakeNewsAnnotation news : select(jCas, FakeNewsAnnotation.class)) {
            Instance<String> instance = new Instance<String>();

            instance.addAll(this.extractor.extract(jCas, news));

            if (this.isTraining()) {
                instance.setOutcome(Boolean.toString(news.getGoldValue()));
                this.dataWriter.write(instance);

            } else {
                String result = this.classifier.classify(instance.getFeatures());
                news.setPredictedValue(Boolean.parseBoolean(result));
                news.addToIndexes();

            }
        }
        
        if (this.isTraining()) {

            try {
                this.collectFeatures(this.modelOutputDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void collectionProcessComplete() throws AnalysisEngineProcessException {
        super.collectionProcessComplete();

        
    }

    /**
     * Transform features and write training data
     * In this phase, the normalization statistics are computed and the raw
     * features are transformed into normalized features.
     * Then the adjusted values are written with a DataWriter (libsvm in this case)
     * for training
     **/
    public void collectFeatures(File outputDirectory) throws IOException, CleartkProcessingException {

        Iterable<Instance<String>> instances = InstanceStream.loadFromDirectory(outputDirectory);

        System.err.println("Collection feature normalization statistics");
        System.err.println("tfIDF ...");
        /** Collect TF*IDF stats for computing tf*idf values on extracted tokens **/
        URI tfIdfDataURI = FeatureAnnotator.createTokenTfIdfDataURI(outputDirectory);
        TfidfExtractor<String, FakeNewsAnnotation> extractor = new TfidfExtractor<String, FakeNewsAnnotation>(
                FeatureAnnotator.TFIDF_EXTRACTOR_KEY);
        extractor.train(instances);
        extractor.save(tfIdfDataURI);

        System.err.println("tfIDF Done");
        System.err.println("similarity to corpus centroid ...");
        /** Collect TF*IDF Centroid stats for computing similarity to corpus centroid **/
        URI tfIdfCentroidSimDataURI = FeatureAnnotator.createIdfCentroidSimilarityDataURI(outputDirectory);
        CentroidTfidfSimilarityExtractor<String, FakeNewsAnnotation> simExtractor = new CentroidTfidfSimilarityExtractor<String, FakeNewsAnnotation>(
                FeatureAnnotator.CENTROID_TFIDF_SIM_EXTRACTOR_KEY);
        simExtractor.train(instances);
        simExtractor.save(tfIdfCentroidSimDataURI);

        System.err.println("similarity to corpus centroid Done");

        System.err.println("MinMax stats for feature normalization ...");
        /** Collect MinMax stats for feature normalization **/
        URI minmaxDataURI = FeatureAnnotator.createMinMaxDataURI(outputDirectory);
        MinMaxNormalizationExtractor<String, FakeNewsAnnotation> minmaxExtractor = new MinMaxNormalizationExtractor<String, FakeNewsAnnotation>(
                FeatureAnnotator.MINMAX_EXTRACTOR_KEY);
        minmaxExtractor.train(instances);
        minmaxExtractor.save(minmaxDataURI);
        System.err.println("MinMax stats for feature normalization Done");
        /** Rerun training data writer pipeline, to transform the extracted instances -- an alternative,
         * more costly approach would be to reinitialize the DocumentClassificationAnnotator above with
         * the URIs for the feature extractor.
         * In this example, we now write in the libsvm format **/

        System.err.println("Write out model training data");
        LibSvmStringOutcomeDataWriter dataWriter = new LibSvmStringOutcomeDataWriter(outputDirectory);
        for (Instance<String> instance : instances) {
            instance = extractor.transform(instance);
            instance = simExtractor.transform(instance);
            instance = minmaxExtractor.transform(instance);
            dataWriter.write(instance);
        }
        dataWriter.finish();
    }

//
//    @Override
//    public void process(JCas jCas) throws AnalysisEngineProcessException {
//        Collection<FeatureExtractor1<FakeNewsAnnotation>> extractors = getExtractors(jCas);
//
//        // wähle FakeNewsAnnotation
//        // es gibt nur eine FakeNewsAnnotation pro jCas!
//        for(FakeNewsAnnotation fnAnnotation : select(jCas, FakeNewsAnnotation.class)) {
//            List<Instance<String>> instances = new ArrayList<>();
//        	Instance<String> instance = new Instance<>();
//
//        	// wähle alle Tokens in der FakeNewsAnnotation
//        	// erzeuge eine Instance mit allen Features aus der FakeNewsAnnotation
//        	for(FeatureExtractor1<FakeNewsAnnotation> extractor : extractors) {
//        		instance.addAll(extractor.extract(jCas, fnAnnotation));
//        	}
//
//        	// Lösung für erzeugte Instance hinzufügen
//            if (this.isTraining()) {
//                instance.setOutcome(Boolean.toString(fnAnnotation.getGoldValue()));
//            }
//            // Instanz hinzufügen
//            instances.add(instance); // add the instance to the list !!!
//
//        	// trainiere oder klassifiziere mit den instances
//            if (this.isTraining()) {
//                this.dataWriter.write(instances);
//            } else {
//                List<String> fakeOrRealNews = this.classify(instances);
//                fnAnnotation.setPredictedValue(Boolean.parseBoolean(fakeOrRealNews.get(0)));
//                fnAnnotation.addToIndexes();
//            }
//        }
//    }
}