package de.unihamburg.informatik.nlp4web.tutorial.tut5.news;

import org.apache.uima.UIMAException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.testing.util.HideOutput;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.cleartk.ml.feature.transform.InstanceDataWriter;
import org.cleartk.ml.jar.DefaultDataWriterFactory;
import org.cleartk.ml.jar.DirectoryDataWriterFactory;
import org.cleartk.ml.jar.GenericJarClassifierFactory;
import org.cleartk.ml.jar.JarClassifierBuilder;
import org.cleartk.util.cr.FilesCollectionReader;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.NewsClassificationAnnotator;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.reader.NewsTitleReader;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.writer.NewsClassificationAnalyzer;

import java.io.File;
import java.io.IOException;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

public class ExecuteNewsClassification {

    public static void writeModel(File saTrain, File modelDirectory, File stopwordsFile)
            throws ResourceInitializationException, UIMAException, IOException {
        System.err.println("Step 1: Extracting features and writing raw instances data");

        runPipeline(
                FilesCollectionReader.getCollectionReaderWithSuffixes(saTrain.getAbsolutePath(),
                        NewsTitleReader.CONLL_VIEW, saTrain.getName()),
                createEngine(NewsTitleReader.class, NewsTitleReader.PARAM_STOP_WORDS, stopwordsFile),
                createEngine(AnalysisEngineFactory.createEngineDescription(
                        NewsClassificationAnnotator.class,
                        NewsClassificationAnnotator.PARAM_IS_TRAINING, true,
                        NewsClassificationAnnotator.PARAM_DIRECTORY_NAME, modelDirectory,
                        DefaultDataWriterFactory.PARAM_DATA_WRITER_CLASS_NAME,
                        InstanceDataWriter.class.getName(),
                        DirectoryDataWriterFactory.PARAM_OUTPUT_DIRECTORY,
                        modelDirectory))

        );
    }

    public static void trainModel(File modelDirectory, String[] arguments) throws Exception {
        /** Stage 3: Train and write model
         * Now that the features have been extracted and normalized, we can proceed
         *in running machine learning to train and package a model **/

        System.err.println("Train model and write model.jar file.");
        HideOutput hider = new HideOutput();
        JarClassifierBuilder.trainAndPackage(modelDirectory, arguments);
        hider.restoreOutput();
    }

    public static void classifyTestFile(File modelDirectory, File saTest, File result, File stopwordsFile)
            throws ResourceInitializationException, UIMAException, IOException {

        runPipeline(
                FilesCollectionReader.getCollectionReaderWithSuffixes(saTest.getAbsolutePath(),
                        NewsTitleReader.CONLL_VIEW, saTest.getName()),
                createEngine(NewsTitleReader.class, NewsTitleReader.PARAM_STOP_WORDS, stopwordsFile),
                createEngine(AnalysisEngineFactory.createEngineDescription(
                        NewsClassificationAnnotator.class,
                        NewsClassificationAnnotator.PARAM_IS_TRAINING, false,
                        NewsClassificationAnnotator.PARAM_DIRECTORY_NAME, modelDirectory,
                        NewsClassificationAnnotator.PARAM_TF_IDF_URI,
                        NewsClassificationAnnotator.createTokenTfIdfDataURI(modelDirectory),
                        NewsClassificationAnnotator.PARAM_TF_IDF_CENTROID_SIMILARITY_URI,
                        NewsClassificationAnnotator.createIdfCentroidSimilarityDataURI(modelDirectory),
                        NewsClassificationAnnotator.PARAM_MINMAX_URI,
                        NewsClassificationAnnotator.createMinMaxDataURI(modelDirectory),
                        GenericJarClassifierFactory.PARAM_CLASSIFIER_JAR_PATH, modelDirectory + "/model.jar")
                ),
                createEngine(NewsClassificationAnalyzer.class, NewsClassificationAnalyzer.PARAM_OUTPUT_DIR, result.getAbsolutePath()));

    }

    public static void main(String[] args) throws Exception {

        String[] trainingArguments = new String[]{"-t", "0"};
        long start = System.currentTimeMillis();

        String modelPath = "src/test/resources/model/";
        new File(modelPath).mkdirs();
        File modelDir = new File(modelPath);
        
        String resltPath = "src/test/resources/results/";
        new File(resltPath).mkdirs();
        File resultDir = new File(resltPath+"news.csv");

        File newsTrain = new File("src/main/resources/news/uci-news-aggregator.csv.train.small");
        File newsTest = new File("src/main/resources/news/uci-news-aggregator.csv.test.small");
        
        File stopWordsFile = new File("src/main/resources/stopwords.txt");
       
        writeModel(newsTrain, modelDir, stopWordsFile);
        trainModel(modelDir, trainingArguments);
        classifyTestFile(modelDir, newsTest, resultDir, stopWordsFile);
        long now = System.currentTimeMillis();
        UIMAFramework.getLogger().log(Level.INFO, "Time: " + (now - start) + "ms");
    }

}
