package de.unihamburg.informatik.nlp4web.tutorial.tut5.writer;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

/**
 * The AnnotationWriter can be used to print Annotations in the text.<br>
 * In it's current implementation it prints all NamedEntityAnnotations.
 * @author timsf
 *
 */
public class AnnotationWriter extends JCasConsumer_ImplBase {

    public static final String PARAM_DBVIEW = "VIEW";
    @ConfigurationParameter(name = PARAM_DBVIEW, description = "The db view name to read from", mandatory = true)
    private String view;
	
	public static final String LF = System.getProperty("line.separator");

	public void process(JCas jcas) throws AnalysisEngineProcessException {
		StringBuilder sb = new StringBuilder();
		sb.append(LF);
		sb.append("=== CAS ===");
		sb.append(LF);
		sb.append("-- Document Text --");
		sb.append(LF);
		sb.append(jcas.getDocumentText());
		sb.append(LF);
		sb.append(LF);
		sb.append("-- DB Text --");
		sb.append(LF);
		try {
			sb.append(jcas.getView(view).getDocumentText());
		} catch (CASException e) {
			// TODO Auto-generated catch block
			sb.append("NO DB VIEW :(((");
		}
		sb.append(LF);
		sb.append("-- Annotations --");
		sb.append(LF);
		
        for (Annotation a : JCasUtil.select(jcas, Annotation.class)) {
            sb.append("[" + a.getType().getShortName() + "] ");
            sb.append("(" + a.getBegin() + ", " + a.getEnd() + ") ");
            sb.append(a.getCoveredText());
            sb.append(LF);
        }

		sb.append(LF);

		getContext().getLogger().log(Level.INFO, sb.toString());
	}

}
