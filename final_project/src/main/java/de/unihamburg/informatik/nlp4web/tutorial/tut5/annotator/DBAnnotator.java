package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation;

public class DBAnnotator extends JCasAnnotator_ImplBase {

	public static final String PARAM_STOP_WORDS = "stopwords";
	@ConfigurationParameter(name = PARAM_STOP_WORDS,
			description = "Stop words for the language",
			mandatory = true)
	private File stopwordFile;

	List<String> stopWords = new ArrayList<>();

    public static final String PARAM_VIEW = "VIEW";
    @ConfigurationParameter(name = PARAM_VIEW, description = "The view to read from", mandatory = true)
    private String view;
	
	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);

		if(stopwordFile == null) {
			stopWords = new ArrayList<>();
		}
		else {
			try {
				for (String stopword: FileUtils.readFileToString(stopwordFile, "UTF-8").split("[\\s\n]")) {
					stopWords.add(stopword);
				}
			}
			catch (Exception e) {
				stopWords = new ArrayList<>();
			}
		}
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		
		JCas docView;
		String dbView;
		try {
			docView = jcas.getView(CAS.NAME_DEFAULT_SOFA);
			dbView = jcas.getView(view).getDocumentText();
		} catch (CASException e) {
			throw new AnalysisEngineProcessException(e);
		}
		
//		String docText;
//		JCas newsCas;
//		try {
//			docText = jcas.getView(CAS.NAME_DEFAULT_SOFA).getDocumentText();
//			newsCas = jcas.createView("NEWS-VIEW");
//		} catch (CASException e) {
//			throw new AnalysisEngineProcessException(e);
//		}
//		logger.log(Level.INFO, docText);
		
//		// a new sentence always starts with a new line
//		if (tbText.charAt(0) != '\n') {
//			tbText = "\n" + tbText;
//		}
//
		String[] tokens = dbView.split("(\r\n|\n)");
//		Sentence sentence = null;
		int idx = 0;
//		Token token = null;
//		POS posTag;
//		String pos;
//		boolean initSentence = false;
		
		FakeNewsAnnotation fnAnnotation = null;
		StringBuffer newsText = new StringBuffer();
		for (String line : tokens) {
			String[] splitted = line.split("\t");
			if(splitted.length < 2)
				continue;
			
			switch(splitted[0]) {
				case "--NEWS--":
					fnAnnotation = new FakeNewsAnnotation(docView);
					break;
				case "--ENDNEWS--":
					fnAnnotation.setBegin(idx);
					fnAnnotation.setEnd(newsText.length());
					fnAnnotation.addToIndexes();
					idx = newsText.length();
					break;
				case "--NEWSID--":
					fnAnnotation.setId(Long.parseLong(splitted[1]));
					break;
				case "--AUTHORS--":
					fnAnnotation.setAuthors(splitted[1]);
					break;
				case "--KEYWORDS--":
					break;
				case "--REAL--":
					fnAnnotation.setGoldValue(Boolean.parseBoolean(splitted[1]));
					break;
				case "--SOURCE--":
					fnAnnotation.setSource(splitted[1]);
					break;
				case "--TITLE--":
					// we need the original title here wähä
					fnAnnotation.setTitle(splitted[1]);
					newsText.append(preprocessed(splitted[1] + "\n"));
					break;
				case "--TEXT--":
					// we need the original body text here
					fnAnnotation.setBody(splitted[1]);
//					newsText.append(preprocessed(splitted[1] + "\n"));
					break;
				case "--SHARECOUNT--":
					fnAnnotation.setShareCount(Long.parseLong(splitted[1]));
					break;
				case "--SHAREUSERCOUNT--":
					fnAnnotation.setShareUserCount(Long.parseLong(splitted[1]));
					break;
				case "--MAXUSERSHARES--":
					fnAnnotation.setMaxUserShareCount(Long.parseLong(splitted[1]));
					break;
				default:
					break;
			}
		}
		docView.setSofaDataString(newsText.toString(), "text/plain");
	}


	String preprocessed(String aTitle) {

		for(String stopWord:stopWords) {
			aTitle = aTitle.replace(" "+stopWord+" ", " ");
		};
		return aTitle.replaceAll("[^a-zA-Z0-9.!?]", " ").replace("  ", " ");
	}

}
