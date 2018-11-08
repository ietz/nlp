package de.unihamburg.informatik.nlp4web.tutorial.tut1.writer;

import java.util.Iterator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

/**
 * 
 * A dummy consumer that reads all the annotations (which are added by the
 * StanfordSegmenter AE) from the JCAS and print them to console
 *
 */

public class DummyWriter extends JCasConsumer_ImplBase {

    public static final String LF = System.getProperty("line.separator");

    public void process(JCas jcas) throws AnalysisEngineProcessException {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CAS ===");
        sb.append(LF);
        sb.append("-- Document Text --");
        sb.append(LF);

        // Add the text of the JCAS to the stringbuilder object
        sb.append(jcas.getDocumentText());
        sb.append(LF);
        sb.append("-- Annotations --");
        sb.append(LF);

        // For each annotation (which are added by the Stanfordsegementer AE), append
        // the short name of the type,
        // and the offsets (begin and end) of the annotation to the Stringbuilder object
        
        for (Iterator<Annotation> i = jcas.getAnnotationIndex().iterator(); i.hasNext();) {
            Annotation a = i.next();
            sb.append("[" + a.getType().getShortName() + "] ");
            sb.append("(" + a.getBegin() + ", " + a.getEnd() + ") ");
            sb.append(a.getCoveredText());
            sb.append(LF);
        }
        // Add a new line at the end
        sb.append(LF);
        // Print the contents to the console. In real application, you send the results
        // to a database,
        // to a file system, or other applications.
        getContext().getLogger().log(Level.INFO, sb.toString());
    }
}
