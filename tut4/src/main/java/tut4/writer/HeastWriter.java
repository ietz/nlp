package tut4.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import tut4.type.HearstAnnotation;

public class HeastWriter extends JCasConsumer_ImplBase {
	public static final String LF = System.getProperty("line.separator");
	public Map<String, Integer> patternCounter = new HashMap<>();
	public Map<String, Integer> typeCounter = new HashMap<>();

	public void process(JCas jcas) throws AnalysisEngineProcessException {
		StringBuilder sb = new StringBuilder();
		sb.append("--- HEAST Annotations ---");
		sb.append(LF);
		String hypoHyper;

		for (HearstAnnotation a : JCasUtil.select(jcas, HearstAnnotation.class)) {
			sb.append("[" + a.getType().getShortName() + "/" + a.getTypeOf() + "] ");
			sb.append("(" + a.getBegin() + ", " + a.getEnd() + ") ");
			sb.append(a.getHyponym() + " -> " + a.getHyperonym());
			hypoHyper = "Hyponym: \"" + a.getHyponym() + "\" Hyperonym: \"" + a.getHyperonym() + "\"";
			sb.append(LF);
			
			// For Task 4.2.a
			if (! patternCounter.containsKey(hypoHyper)) {
				patternCounter.put(hypoHyper, 1);
			}
			else {
				patternCounter.put(hypoHyper, patternCounter.get(hypoHyper) + 1);
			}
			
			// For task 4.2.b
			if (! typeCounter.containsKey(a.getTypeOf())) {
				typeCounter.put(a.getTypeOf(), 1);
			}
			else {
				typeCounter.put(a.getTypeOf(), typeCounter.get(a.getTypeOf()) + 1);
			}
			
		}
		sb.append(LF);
		
		getContext().getLogger().log(Level.INFO, sb.toString());
		
		patternCounter.forEach((k,v) -> System.out.println(k + " Count: " + v));
		
		typeCounter.forEach((k, v) -> System.out.println(k + " Count: " + v));
	}
}
