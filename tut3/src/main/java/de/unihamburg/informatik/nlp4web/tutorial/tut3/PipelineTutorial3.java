package de.unihamburg.informatik.nlp4web.tutorial.tut3;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.unihamburg.informatik.nlp4web.tutorial.tut3.annotator.BreakIteratorTokenizer;
import de.unihamburg.informatik.nlp4web.tutorial.tut3.annotator.SentenceSplitter;
import de.unihamburg.informatik.nlp4web.tutorial.tut3.annotator.WhitespaceTokenizer;
import de.unihamburg.informatik.nlp4web.tutorial.tut3.annotator.WordBreakIteratorTokenizer;
import de.unihamburg.informatik.nlp4web.tutorial.tut3.reader.TextReader;
import de.unihamburg.informatik.nlp4web.tutorial.tut3.writer.AnnotationWriter;
import de.unihamburg.informatik.nlp4web.tutorial.tut3.writer.TokensPerSentenceWriter;

public class PipelineTutorial3
{

    public static void main(String[] args)
        throws UIMAException, IOException
    {

        CollectionReader textReader = createReader(TextReader.class,
                TextReader.PARAM_DIRECTORY_NAME, "src/main/resources/simple-documents");
                
        AnalysisEngine wsTokenizer = createEngine(WhitespaceTokenizer.class);
        AnalysisEngine biTokenizer = createEngine(WordBreakIteratorTokenizer.class);
        AnalysisEngine sentenceTokenizer = createEngine(SentenceSplitter.class);
        AnalysisEngine annotationWriter = createEngine(AnnotationWriter.class);
        AnalysisEngine tpsWriter = createEngine(TokensPerSentenceWriter.class);

        SimplePipeline.runPipeline(
                textReader,
                wsTokenizer,
                biTokenizer,
                sentenceTokenizer,
                annotationWriter,
                tpsWriter
        );
    }
}
