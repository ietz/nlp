package de.unihamburg.informatik.nlp4web.tutorial.tut5.writer;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.evaluation.Scorekeeper;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Logger;

/**
 * The AnnotationWriter can be used to print Annotations in the text.<br>
 * In it's current implementation it prints all NamedEntityAnnotations.
 * @author timsf
 *
 */
public class OnlineResultWriter extends JCasConsumer_ImplBase {

	public static boolean real;

	public static final String LF = System.getProperty("line.separator");

	int tot = 0;
	int corr = 0;

	private Scorekeeper scorekeeper;

	private Logger logger;

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);

		logger = getLogger();
		this.scorekeeper = new Scorekeeper();
	}


	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		for (FakeNewsAnnotation fakeNewsAnnotation : JCasUtil.select(jCas, FakeNewsAnnotation.class)) {
			boolean predicted = fakeNewsAnnotation.getPredictedValue();
			boolean gold = fakeNewsAnnotation.getGoldValue();

			real = predicted;

			this.scorekeeper.track(gold, predicted);
		}

	}

	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		super.collectionProcessComplete();

		this.scorekeeper.logResults(logger, "Test");
	}
}
