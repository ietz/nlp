package de.unihamburg.informatik.nlp4web.tutorial.tut5.evaluation;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Scorekeeper<T> {

	private Map<T, Classification> classifications = new HashMap<>();
	private int correct = 0;
	private int tokenCount = 0;

	public void track(T goldValue, T predictedValue) {
		tokenCount++;
		if (Objects.equals(goldValue, predictedValue)) {
			correct++;
			getValue(goldValue).tp++;
		} else {
			getValue(goldValue).fp++;
			getValue(predictedValue).fp++;
		}
	}

	private Classification getValue(T value) {
		return classifications.computeIfAbsent(value, k -> new Classification());
	}

	public void logResults(Logger logger, String name) {
		StringBuilder sb = new StringBuilder("\n");

		AsciiTable cscores = new AsciiTable();
		cscores.addRow(name, "precision", "recall   ", "f-measure");
		cscores.addRule();

		double precisionSum = 0.0;
		double recallSum = 0.0;
		double fmeasureSum = 0.0;
		for (Map.Entry<T, Classification> e : classifications.entrySet()) {
			double precision = 0.0;
			double recall = 0.0;
			double fmeasure = 0.0;
			double tp = e.getValue().tp;
			double fp = e.getValue().fp;
			double fn = e.getValue().fn;
			if (e.getValue().tp > 0.0) {
				precision = 1.0 * tp / (tp + fp);
				recall = 1.0 * tp / (tp + fn);
				fmeasure = 2.0 * precision * recall / (precision + recall);
				recallSum += recall;
				precisionSum += precision;
				fmeasureSum += fmeasure;
			}

			cscores.addRow(
					e.getKey(),
					String.format("%.04f", precision),
					String.format("%.04f", recall),
					String.format("%.04f", fmeasure)
			);
		}

		cscores.getRenderer().setCWC(new CWC_LongestLine());
		sb.append(cscores.render()).append("\n\n");

		sb.append("Accuracy: \t").append(1.0 * correct / tokenCount).append("\n");
		sb.append("Precision:\t").append(precisionSum / classifications.size()).append("\n");
		sb.append("Recall:   \t").append(recallSum / classifications.size()).append("\n");
		sb.append("F-Measure:\t").append(fmeasureSum / classifications.size()).append("\n");

		logger.log(Level.INFO, sb.toString());
	}


	private class Classification {
		int fp = 0;
		int tp = 0;
		int fn = 0;
	}
}
