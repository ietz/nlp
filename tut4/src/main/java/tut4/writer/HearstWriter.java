package tut4.writer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import tut4.type.HearstAnnotation;

public class HearstWriter extends JCasConsumer_ImplBase {
	private static final String LF = System.getProperty("line.separator");

	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Map<String, Integer> relationCounter = new HashMap<>();
		Map<String, Integer> typeCounter = new HashMap<>();

		StringBuilder sb = new StringBuilder();
		sb.append("--- HEAST Annotations ---");
		sb.append(LF);

		for (HearstAnnotation a : JCasUtil.select(jcas, HearstAnnotation.class)) {
			sb.append("[" + a.getType().getShortName() + "/" + a.getTypeOf() + "] ");
			sb.append("(" + a.getBegin() + ", " + a.getEnd() + ") ");
			sb.append(a.getHyponym() + " -> " + a.getHyperonym());
			sb.append(LF);

			// For Task 4.2.a
			String hypoHyper = "Hyponym: \"" + a.getHyponym() + "\" Hyperonym: \"" + a.getHyperonym() + "\"";
			relationCounter.compute(hypoHyper, (k, v) -> v == null ? 1 : v + 1);
			typeCounter.compute(a.getTypeOf(), (k, v) -> v == null ? 1 : v + 1);
		}
		sb.append(LF);

		sb.append("-- PATTERN COUNTS").append(LF);
		appendCounts(sb, typeCounter, 5);
		sb.append(LF);

		sb.append("-- RELATIONS").append(LF);
		appendCounts(sb, relationCounter, 50);

		getContext().getLogger().log(Level.INFO, sb.toString());
	}

	private void appendCounts(StringBuilder sb, Map<String, Integer> counts, long limit) {
		counts.entrySet().stream()
				.sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
				.limit(limit)
				.forEachOrdered(entry -> sb.append(String.format("%03d %s\n", entry.getValue(), entry.getKey())));
	}
}
