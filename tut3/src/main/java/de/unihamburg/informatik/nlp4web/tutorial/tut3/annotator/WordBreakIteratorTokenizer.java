package de.unihamburg.informatik.nlp4web.tutorial.tut3.annotator;

import java.text.BreakIterator;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.unihamburg.informatik.nlp4web.tutorial.tut3.type.BIToken;

public class WordBreakIteratorTokenizer extends BreakIteratorTokenizer {

	@Override
	protected BreakIterator getBreakIterator() {
		return BreakIterator.getWordInstance();
	}

	@Override
	protected void annotate(JCas jcas, int start, int end) {
		if (!Character.isWhitespace(jcas.getDocumentText().charAt(start))) {
	    	BIToken annotation = new BIToken(jcas);
	    	annotation.setBegin(start);
	    	annotation.setEnd(end);
	    	annotation.addToIndexes();
		}
	}
}
