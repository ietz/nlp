package de.unihamburg.informatik.nlp4web.tutorial.tut3.annotator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import de.unihamburg.informatik.nlp4web.tutorial.tut3.type.WSToken;

public class WhitespaceTokenizer
    extends JCasAnnotator_ImplBase
{
	private static final Pattern tokenPattern = Pattern.compile("\\S+");

    // problem 3.1 Adapt the whitespace tokenizer into a UIMA annotator
    @Override
    public void process(JCas jcas)
        throws AnalysisEngineProcessException
    {
        String document = jcas.getDocumentText();
        Matcher matcher = tokenPattern.matcher(document);
        matcher.results().forEach(result -> {
            WSToken tokenAnnotation = new WSToken(jcas);      
            tokenAnnotation.setBegin(result.start());
            tokenAnnotation.setEnd(result.end());
            tokenAnnotation.addToIndexes();
        });
    }
}
