package de.unihamburg.informatik.nlp4web.tutorial.tut5.postagger;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.fit.testing.util.HideOutput;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.cleartk.ml.jar.DefaultDataWriterFactory;
import org.cleartk.ml.jar.DirectoryDataWriterFactory;
import org.cleartk.ml.jar.GenericJarClassifierFactory;
import org.cleartk.ml.mallet.MalletCrfStringOutcomeDataWriter;
import org.cleartk.util.cr.FilesCollectionReader;

import de.tudarmstadt.ukp.dkpro.core.snowball.SnowballStemmer;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.PosTaggerAnnotator;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.reader.ConllAnnotator;

public class ExecutePosTagger {

	public static void writeModel(File posTagFile, String modelDirectory, String language)
			throws ResourceInitializationException, UIMAException, IOException {

		runPipeline(
				FilesCollectionReader.getCollectionReaderWithSuffixes(posTagFile.getAbsolutePath(),
						ConllAnnotator.CONLL_VIEW, posTagFile.getName()),
				createEngine(ConllAnnotator.class),
				createEngine(SnowballStemmer.class, SnowballStemmer.PARAM_LANGUAGE, language),
				createEngine(PosTaggerAnnotator.class, PosTaggerAnnotator.PARAM_FEATURE_EXTRACTION_FILE,
						"src/main/resources/feature/features.xml", PosTaggerAnnotator.PARAM_IS_TRAINING, true,
						DirectoryDataWriterFactory.PARAM_OUTPUT_DIRECTORY, modelDirectory,
						DefaultDataWriterFactory.PARAM_DATA_WRITER_CLASS_NAME,
						MalletCrfStringOutcomeDataWriter.class));
	}

	public static void trainModel(String modelDirectory) throws Exception {

        System.err.println("Train model and write model.jar file.");
        //uncomment the hider to hide the comments, but sometimes they are usefull
       // HideOutput hider = new HideOutput();
		org.cleartk.ml.jar.Train.main(modelDirectory);
	//	hider.restoreOutput();
	}

	public static void classifyTestFile(String modelDirectory, File testPosFile, String language)
			throws ResourceInitializationException, UIMAException, IOException {
		runPipeline(
				FilesCollectionReader.getCollectionReaderWithSuffixes(testPosFile.getAbsolutePath(),
						ConllAnnotator.CONLL_VIEW, testPosFile.getName()),
				createEngine(ConllAnnotator.class),
				createEngine(SnowballStemmer.class, SnowballStemmer.PARAM_LANGUAGE, language),
				createEngine(PosTaggerAnnotator.class, PosTaggerAnnotator.PARAM_FEATURE_EXTRACTION_FILE,
						"src/main/resources/feature/features.xml",
						GenericJarClassifierFactory.PARAM_CLASSIFIER_JAR_PATH, modelDirectory + "model.jar"),
				createEngine(AnalyzeFeatures.class, AnalyzeFeatures.PARAM_INPUT_FILE, testPosFile.getAbsolutePath(),
						AnalyzeFeatures.PARAM_TOKEN_VALUE_PATH, "pos/PosValue"));
	}

	public static void main(String[] args) throws Exception {

		long start = System.currentTimeMillis();
		String modelDirectory = "src/test/resources/model/";
		String language = "en";
		File posTagFile = new File("src/main/resources/pos/wsj_pos.train_100");
		File testPosFile = new File("src/main/resources/pos/wsj_pos.dev");
		new File(modelDirectory).mkdirs();
		writeModel(posTagFile, modelDirectory, language);
		trainModel(modelDirectory);
		classifyTestFile(modelDirectory, testPosFile, language);
		long now = System.currentTimeMillis();
		UIMAFramework.getLogger().log(Level.INFO, "Time: " + (now - start) + "ms");
	}
}
