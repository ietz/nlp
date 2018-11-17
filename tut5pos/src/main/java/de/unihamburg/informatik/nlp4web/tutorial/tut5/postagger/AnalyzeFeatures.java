package de.unihamburg.informatik.nlp4web.tutorial.tut5.postagger;

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.evaluation.Scorekeeper;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.cleartk.ml.feature.extractor.TypePathExtractor;
/*import org.cleartk.classifier.feature.extractor.simple.TypePathExtractor;*/
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

/**
 * This class analaysis the classification result and prints the precision and
 * recall for each pos tag and prints the average precision and recall for all
 * pos tags and the accuracy
 *
 *
 */
public class AnalyzeFeatures extends JCasAnnotator_ImplBase
{
	public static final String PARAM_TOKEN_VALUE_PATH = "TokenValuePath";
	public static final String PARAM_INPUT_FILE = "InputFile";
	/**
	 * To make this class general, the path to the feature that is used
	 * for the evaluation the tokenValuePath has to be set to the feature
	 * e.g. for the pos value: pos/PosValue is used
	 * (works only for token: de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token)
	 */
	@ConfigurationParameter(name = PARAM_TOKEN_VALUE_PATH, mandatory = true)
	private String tokenValuePath;
	@ConfigurationParameter(name = PARAM_INPUT_FILE, mandatory = true)
	private String inputFile;
	Logger logger = UIMAFramework.getLogger(AnalyzeFeatures.class);


	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		try {
			logger.log(Level.INFO, "Start analyzing results");

			TypePathExtractor<Token> extractor;
			extractor = new TypePathExtractor<>(Token.class, tokenValuePath);
			String line;
			String[] splitLine;
			BufferedReader reader = new BufferedReader(
					new FileReader(inputFile));

			Scorekeeper<String> scorekeeper = new Scorekeeper<>();

			for (Sentence sentence : select(jCas, Sentence.class)) {
				reader.readLine();
				List<Token> tokens = selectCovered(jCas, Token.class, sentence);
				for (Token token : tokens) {
					line = reader.readLine();
					splitLine = line.split("\\s");
					String trueValue = splitLine[1];
					String classifiedValue = extractor.extract(jCas, token).get(0).getValue().toString();

					if (splitLine[0].equals(token.getCoveredText())) {
						scorekeeper.track(trueValue, classifiedValue);
					} else {
						logger.log(
								Level.WARNING,
								"Token of predicting file does not match to text ("
										+ splitLine[0] + "!="
										+ token.getCoveredText() + ")");
					}

				}
			}
			reader.close();

			scorekeeper.logResults(logger, "Pos-Tag");
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}
}
