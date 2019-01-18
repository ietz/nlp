package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.apache.commons.io.FileUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
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
        int fakeidx = 0;
		int idx = 0;
//		Token token = null;
//		POS posTag;
//		String pos;
//		boolean initSentence = false;
		
		Token token = null;
        Sentence sentence = null;
		FakeNewsAnnotation fnAnnotation = null;
		StringBuffer newsText = new StringBuffer();
		for (String line : tokens) {
			String[] splitted = line.split("\t");
			if(splitted.length < 2)
				continue;
			
			switch(splitted[0]) {
				case "--NEWS--":
					sentence = new Sentence(docView);
					fnAnnotation = new FakeNewsAnnotation(docView);
					break;
				case "--ENDNEWS--":
					fnAnnotation.setBegin(fakeidx);
					fnAnnotation.setEnd(newsText.length());
					fnAnnotation.addToIndexes();
                    fakeidx = newsText.length();
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
					String text = preprocessed(splitted[1]) + "\n";
					newsText.append(text);

					// create sentence annotation
                    sentence.setBegin(idx);
                    sentence.setEnd(newsText.length()-1);
                    sentence.addToIndexes();

	                String[] words = text.split("\\s+");
	                int current = 0;
	                int position = 0;
	                int tokenStart = 0;
	                int tokenEnd = 0;
	                
	                for (String word : words){
	                	do{
	                		position = text.indexOf(word, current);
	                	} while (position < current);
	                	current = position + 1;
	                	tokenStart = idx + position;
	                	tokenEnd = tokenStart + word.length();
	                	token = new Token(docView, tokenStart, tokenEnd);
	                	token.addToIndexes();
	                }
                    idx = newsText.length();
					break;
				case "--TEXT--":
					// we need the original body text here#
                    fnAnnotation.setBody(splitted[1]);

                    String bodytext = preprocessed(splitted[1]);


                    Properties props = new Properties();
                    // set the list of annotators to run
                    props.setProperty("annotators", "tokenize,ssplit");
                    // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
                    props.setProperty("coref.algorithm", "neural");
                    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

                    // create a document object
                    CoreDocument document = new CoreDocument(bodytext);
                    // annnotate the document
                    pipeline.annotate(document);
                    // examples

                    for(CoreLabel coretoken : document.tokens()) {
                        Token myToken = new Token(docView, idx + coretoken.beginPosition(), idx + coretoken.endPosition());
                        myToken.addToIndexes();
                    }

                    for(CoreSentence coresentence : document.sentences()) {
                        int sentenceStart = coresentence.tokens().get(0).beginPosition();
                        int sentenceEnd = coresentence.tokens().get(coresentence.tokens().size() -1).endPosition();

                        Sentence mySentence = new Sentence(docView, idx + sentenceStart, idx + sentenceEnd);
                        mySentence.addToIndexes();
//                        idx = idx + coresentence.text().length() + 1;
                    }
					newsText.append(preprocessed(splitted[1]));
                    idx = idx + bodytext.length();
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
