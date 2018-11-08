package de.unihamburg.informatik.nlp4web.tutorial.tut3;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.tudarmstadt.ukp.dkpro.core.jazzy.JazzyChecker;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.unihamburg.informatik.nlp4web.tutorial.tut3.reader.TextReader;
import de.unihamburg.informatik.nlp4web.tutorial.tut3.writer.AnnotationWriter;


public class PipelineTutorial2 {

	public static void main(String[] args) throws UIMAException, IOException {
		
        CollectionReader textReader = createReader(TextReader.class,
        TextReader.PARAM_DIRECTORY_NAME, "src/main/resources/simple-documents");
		
		AnalysisEngine stanfordSegmenter = createEngine(StanfordSegmenter.class);

		AnalysisEngine spellChecker = createEngine(JazzyChecker.class,
				JazzyChecker.PARAM_MODEL_LOCATION, "src/main/resources/dict/words.small");

		AnalysisEngine annotationWriter = createEngine(AnnotationWriter.class);
		
		SimplePipeline.runPipeline(textReader,  stanfordSegmenter, spellChecker, annotationWriter);
	}
}
