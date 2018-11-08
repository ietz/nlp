package de.unihamburg.informatik.nlp4web.tutorial.tut1;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.unihamburg.informatik.nlp4web.tutorial.tut1.reader.DummyReader;
import de.unihamburg.informatik.nlp4web.tutorial.tut1.writer.DummyWriter;
import de.unihamburg.informatik.nlp4web.tutorial.tut1.writer.SentenceCountWriter;

public class Pipeline {

	public static void main(String[] args) throws UIMAException, IOException {
	    // a simple dummy reader (reads a hardecoded text and set it to the JCAS)
		CollectionReader reader = createReader(DummyReader.class);
		// a DKPro analysis engen based on stanford segmenter. It is used to 1) break the text into different
		// tokens (words), split the text into different sentences. We will see in other tutorials how to
		// create our own analysis engine
		AnalysisEngine seg = createEngine(StanfordSegmenter.class, StanfordSegmenter.PARAM_LANGUAGE, "en");

		// a simple consumer that prints the annotation (which are added by the Stanfordsegmenter AE
		// and prints them to the console
		AnalysisEngine writer = createEngine(DummyWriter.class);
		/*Logger logger = writer.getLogger();
		logger.setLevel(Level.OFF);
		writer.setLogger(logger);*/
		
		AnalysisEngine sentenceCounter = createEngine(SentenceCountWriter.class);

		SimplePipeline.runPipeline(reader, seg, writer, sentenceCounter);
	}
}
