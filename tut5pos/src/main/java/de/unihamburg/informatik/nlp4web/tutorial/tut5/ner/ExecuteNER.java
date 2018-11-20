package de.unihamburg.informatik.nlp4web.tutorial.tut5.ner;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.File;
import java.io.IOException;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.KnownNEAnnotator;
import org.apache.uima.UIMAException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;
import org.cleartk.ml.crfsuite.CrfSuiteStringOutcomeDataWriter;
import org.cleartk.ml.jar.DefaultDataWriterFactory;
import org.cleartk.ml.jar.DirectoryDataWriterFactory;
import org.cleartk.ml.jar.GenericJarClassifierFactory;
import org.cleartk.util.cr.FilesCollectionReader;

import de.tudarmstadt.ukp.dkpro.core.snowball.SnowballStemmer;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.NERAnnotator;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.reader.NERReader;

public class ExecuteNER {

    public static void writeModel(File posTagFile, String modelDirectory, String language) throws UIMAException, IOException {
        runPipeline(
                FilesCollectionReader.getCollectionReaderWithSuffixes(posTagFile.getAbsolutePath(),
                        NERReader.CONLL_VIEW, posTagFile.getName()),
                createEngine(NERReader.class),
                createEngine(SnowballStemmer.class, SnowballStemmer.PARAM_LANGUAGE, language),
				createEngine(KnownNEAnnotator.class),
                createEngine(NERAnnotator.class,
                        NERAnnotator.PARAM_IS_TRAINING, true,
                        DirectoryDataWriterFactory.PARAM_OUTPUT_DIRECTORY, modelDirectory,
                        DefaultDataWriterFactory.PARAM_DATA_WRITER_CLASS_NAME, CrfSuiteStringOutcomeDataWriter.class
                )
        );
    }

    public static void trainModel(String modelDirectory) throws Exception {
        org.cleartk.ml.jar.Train.main(modelDirectory);

    }

    public static void classifyTestFile(String modelDirectory, File testPosFile, String language) throws UIMAException, IOException {
        runPipeline(
                FilesCollectionReader.getCollectionReaderWithSuffixes(testPosFile.getAbsolutePath(),
                        NERReader.CONLL_VIEW, testPosFile.getName()),
                createEngine(NERReader.class),
                createEngine(SnowballStemmer.class, SnowballStemmer.PARAM_LANGUAGE, language),
                createEngine(KnownNEAnnotator.class),
                createEngine(NERAnnotator.class, GenericJarClassifierFactory.PARAM_CLASSIFIER_JAR_PATH, modelDirectory + "model.jar"),
                createEngine(ScoreNER.class, ScoreNER.PARAM_OUTPUT_FILE, testPosFile.getAbsolutePath() + ".out")
        );
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        String modelDirectory = "src/test/resources/model/";
        String language = "de";
        File nerTrain = new File("src/main/resources/ner/ner_deu.train");
        File nerTest = new File("src/main/resources/ner/ner_deu.dev");
        new File(modelDirectory).mkdirs();
        writeModel(nerTrain, modelDirectory, language);
        trainModel(modelDirectory);
        classifyTestFile(modelDirectory, nerTest, language);
        long now = System.currentTimeMillis();
        UIMAFramework.getLogger().log(Level.INFO, "Time: " + (now - start) + "ms");
    }
}