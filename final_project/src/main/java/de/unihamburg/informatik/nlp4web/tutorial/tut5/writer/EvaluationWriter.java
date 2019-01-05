package de.unihamburg.informatik.nlp4web.tutorial.tut5.writer;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation;

/**
 * The AnnotationWriter can be used to print Annotations in the text.<br>
 * In it's current implementation it prints all NamedEntityAnnotations.
 * @author timsf
 *
 */
public class EvaluationWriter extends JCasConsumer_ImplBase {
	
	public static final String LF = System.getProperty("line.separator");

	public void process(JCas jcas) throws AnalysisEngineProcessException {
		StringBuilder sb = new StringBuilder();
		sb.append(LF);
		sb.append("=== Evaluation ===");
		sb.append(LF);
        
        for (FakeNewsAnnotation fna : JCasUtil.select(jcas, FakeNewsAnnotation.class)) {
            sb.append("ID: " + fna.getId());
            sb.append(LF);
            sb.append("GoldValue " + (fna.getGoldValue() ? "REAL" : "FAKE"));
            sb.append(LF);
            sb.append("PredictedValue " + (fna.getPredictedValue() ? "REAL" : "FAKE"));
            sb.append(LF);
        }

		sb.append(LF);

		getContext().getLogger().log(Level.INFO, sb.toString());
	}

}
