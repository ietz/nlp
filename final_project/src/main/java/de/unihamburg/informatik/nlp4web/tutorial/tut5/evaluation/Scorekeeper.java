package de.unihamburg.informatik.nlp4web.tutorial.tut5.evaluation;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Scorekeeper {

    int tp = 0;
    int tn = 0;
    int fp = 0;
    int fn = 0;


    public void track(boolean goldValue, boolean predictedValue) {
        if (goldValue && predictedValue) {
            tp++;
        } else if (!goldValue && !predictedValue) {
            tn++;
        } else if (goldValue && !predictedValue) {
            fn++;
        } else {
            fp++;
        }
    }

    public void logResults(Logger logger, String name) {
        StringBuilder sb = new StringBuilder("\n");

        sb.append("\tgtrue\tgfalse\n");
        sb.append(String.format("ptrue\t%d\t%d\n", tp, fp));
        sb.append(String.format("pfalse\t%d\t%d\n\n\n", fn, fp));


        double accuracy = 1.0 * (tp + tn) / (tp + tn + fp + fn);
        double precision = 1.0 * tp / (tp + fp);
        double recall = 1.0 * tp / (tp + fn);
        double f1 = 2 * precision * recall / (precision + recall);

        sb.append("Accuracy: \t").append(accuracy).append("\n");
        sb.append("Precision:\t").append(precision).append("\n");
        sb.append("Recall:   \t").append(recall).append("\n");
        sb.append("F-Measure:\t").append(f1).append("\n\n");

        sb.append("For Google Sheets: ").append(String.format("%f\t%f\t%f\t%f\n", accuracy, precision, recall, f1));

        logger.log(Level.INFO, sb.toString());
    }

}