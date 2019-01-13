package de.unihamburg.informatik.nlp4web.tutorial.tut5.fni;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.util.Level;
import org.cleartk.ml.Feature;
import org.cleartk.ml.crfsuite.CrfSuiteStringOutcomeDataWriter;
import org.cleartk.ml.feature.transform.InstanceDataWriter;
import org.cleartk.ml.jar.DefaultDataWriterFactory;
import org.cleartk.ml.jar.DirectoryDataWriterFactory;
import org.cleartk.ml.jar.GenericJarClassifierFactory;

import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.DBAnnotator;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.FeatureAnnotator;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.reader.DBReader;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.writer.AnnotationWriter;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.writer.EvaluationWriter;

public class ExecuteFakeNewsIdentification {



    public static void writeModel(String db, String language, File modelDirectory, File stopwordsFile) throws UIMAException, IOException {
        runPipeline(
        	createReader(DBReader.class,
        				DBReader.PARAM_DB, db,
        				DBReader.PARAM_LANGUAGE, language,
        				FeatureAnnotator.PARAM_IS_TRAINING, true,
        				DBReader.PARAM_VIEW, "DB-VIEW"),
        	createEngine(DBAnnotator.class,
        				DBAnnotator.PARAM_VIEW, "DB-VIEW",
                        DBAnnotator.PARAM_STOP_WORDS, stopwordsFile),
    		createEngine(StanfordSegmenter.class),
//            createEngine(StanfordPosTagger.class),
//            createEngine(StanfordLemmatizer.class),
        	createEngine(AnnotationWriter.class,
    				AnnotationWriter.PARAM_DBVIEW, "DB-VIEW"),
        	createEngine(FeatureAnnotator.class,
                    FeatureAnnotator.PARAM_IS_TRAINING, true,
                    FeatureAnnotator.PARAM_DIRECTORY_NAME, modelDirectory,
                    DefaultDataWriterFactory.PARAM_DATA_WRITER_CLASS_NAME, InstanceDataWriter.class.getName(),
                    DirectoryDataWriterFactory.PARAM_OUTPUT_DIRECTORY, modelDirectory)
        );
    }

    public static void trainModel(File modelDirectory) throws Exception {
        org.cleartk.ml.jar.Train.main(modelDirectory);

    }

    public static void classifyTestFile(String db, String language, File modelDirectory, File stopwordsFile) throws UIMAException, IOException {
        runPipeline(
            	createReader(DBReader.class,
            				DBReader.PARAM_DB, db,
            				DBReader.PARAM_LANGUAGE, language,
                        FeatureAnnotator.PARAM_IS_TRAINING, false,
            				DBReader.PARAM_VIEW, "DB-VIEW"),
            	createEngine(DBAnnotator.class,
        				DBAnnotator.PARAM_VIEW, "DB-VIEW",
                        DBAnnotator.PARAM_STOP_WORDS, stopwordsFile),
            	/*createEngine(AnnotationWriter.class,
    				AnnotationWriter.PARAM_DBVIEW, "DB-VIEW"),*/
            	createEngine(FeatureAnnotator.class,
                        FeatureAnnotator.PARAM_IS_TRAINING, false,
                        FeatureAnnotator.PARAM_DIRECTORY_NAME, modelDirectory,
                        FeatureAnnotator.PARAM_TF_IDF_URI,
                        FeatureAnnotator.createTokenTfIdfDataURI(modelDirectory),
                        FeatureAnnotator.PARAM_TF_IDF_CENTROID_SIMILARITY_URI,
                        FeatureAnnotator.createIdfCentroidSimilarityDataURI(modelDirectory),
                        FeatureAnnotator.PARAM_MINMAX_URI,
                        FeatureAnnotator.createMinMaxDataURI(modelDirectory),
                        GenericJarClassifierFactory.PARAM_CLASSIFIER_JAR_PATH, modelDirectory + "/model.jar"),
                createEngine(EvaluationWriter.class)   
            );
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        File modelDirectory = new File("src/test/resources/model/");
        modelDirectory.mkdirs();

        File stopWordsFile = new File("src/main/resources/stopwords.txt");

        String language = "en";
        String dbpath = new ExecuteFakeNewsIdentification().getResourceFilePath("db/fakenewsnet.db");
        String db = "jdbc:sqlite:"+dbpath;
        writeModel(db, language, modelDirectory, stopWordsFile);
        trainModel(modelDirectory);
        classifyTestFile(db, language, modelDirectory, stopWordsFile);
        long now = System.currentTimeMillis();
        UIMAFramework.getLogger().log(Level.INFO, "Time: " + (now - start) + "ms");
    }
    
    private String getResourceFilePath(String filename) {
    	return new File(this.getClass().getClassLoader().getResource(filename).getFile()).getAbsolutePath();
    }
}