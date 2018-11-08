package de.unihamburg.informatik.nlp4web.tutorial.tut3.annotator;

import java.text.BreakIterator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.unihamburg.informatik.nlp4web.tutorial.tut3.type.BIToken;

public abstract class BreakIteratorTokenizer
    extends JCasAnnotator_ImplBase
{
	
	// The BreakIteratorTokenizer functionality is partially implemented in the
	// WordBreakIteratorTokenizer class
	
    @Override
    public void process(JCas jcas)
        throws AnalysisEngineProcessException
    {
        String document = jcas.getDocumentText();
        BreakIterator iterator = getBreakIterator();
        iterator.setText(document);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
        	annotate(jcas, start, end);
        }
    }
    
    
    protected abstract BreakIterator getBreakIterator();
    protected abstract void annotate(JCas jcas, int start, int end);

}