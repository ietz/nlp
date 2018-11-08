package de.unihamburg.informatik.nlp4web.tutorial.tut3.annotator;

import java.text.BreakIterator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.unihamburg.informatik.nlp4web.tutorial.tut3.type.BISentence;
import de.unihamburg.informatik.nlp4web.tutorial.tut3.type.BIToken;

public class SentenceSplitter extends BreakIteratorTokenizer {

    @Override
	protected BreakIterator getBreakIterator() {
		return BreakIterator.getSentenceInstance();
	}

	@Override
	protected void annotate(JCas jcas, int start, int end) {
    	BISentence annotation = new BISentence(jcas);
    	annotation.setBegin(start);
    	annotation.setEnd(end);
    	annotation.addToIndexes();
	}
}
