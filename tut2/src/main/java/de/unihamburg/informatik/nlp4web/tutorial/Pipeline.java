package de.unihamburg.informatik.nlp4web.tutorial;


import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.unihamburg.informatik.nlp4web.tutorial.reader.WebReader;
import de.unihamburg.informatik.nlp4web.tutorial.writer.WebWriter;

public class Pipeline {
	
	public static void main(String[] args) throws UIMAException, IOException {
		runPipeline("BreakIteratorSegmenter", BreakIteratorSegmenter.class);
		runPipeline("StanfordSegmenter", StanfordSegmenter.class);
	}
	
	private static void runPipeline(String identifier, Class<? extends AnalysisComponent> segmenter) throws UIMAException, IOException {
		SimplePipeline.runPipeline(
			createReader(WebReader.class),
			createEngine(segmenter),
			createEngine(WebWriter.class, WebWriter.PIPELINE_IDENTIFIER, identifier)
		);
	}
}
