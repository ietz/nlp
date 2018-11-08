package tut4.writer;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import tut4.type.HearstAnnotation;

public class HeastWriter extends JCasConsumer_ImplBase {
	public static final String LF = System.getProperty("line.separator");

	public void process(JCas jcas) throws AnalysisEngineProcessException {
		StringBuilder sb = new StringBuilder();
		sb.append("--- HEAST Annotations ---");
		sb.append(LF);

		for (HearstAnnotation a : JCasUtil.select(jcas, HearstAnnotation.class)) {
			sb.append("[" + a.getType().getShortName() + "/" + a.getTypeOf() + "] ");
			sb.append("(" + a.getBegin() + ", " + a.getEnd() + ") ");
			sb.append(a.getHyponym() + " -> " + a.getHyperonym());
			sb.append(LF);
		}

		sb.append(LF);

		getContext().getLogger().log(Level.INFO, sb.toString());
	}
}
