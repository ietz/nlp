package de.unihamburg.informatik.nlp4web.tutorial.tut5.reader;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.FeatureAnnotator;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.db.FakeNewsNet;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.db.NewsModel;
import org.apache.uima.UimaContext;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import java.io.IOException;
import java.util.List;

public class ConfigurationReader
    extends JCasCollectionReader_ImplBase
{

    public static final String PARAM_VIEW = "VIEW";
    @ConfigurationParameter(name = PARAM_VIEW, description = "The view to write to", mandatory = true)
    private String view;

    public static final String PARAM_NEWS_TITLE = "NEWS_TITLE";
    @ConfigurationParameter(name = PARAM_NEWS_TITLE, description = "The news title", mandatory = true)
    private String news_title;
	
    public static final String PARAM_NEWS_TEXT = "NEWS_TEXT";
    @ConfigurationParameter(name = PARAM_NEWS_TEXT, description = "The news text", mandatory = true)
    private String news_text;

    public static final String PARAM_LANGUAGE = "language";
    @ConfigurationParameter(name = PARAM_LANGUAGE, defaultValue ="en", description = "default language for the text", mandatory = true)
    private String language;

//    private int i;
    private List<NewsModel> news;
    private boolean initialized;
    
    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);

        initialized = false;
    }

    @Override
    public boolean hasNext() throws IOException, CollectionException {
        return !initialized;
    }

    @Override
    public Progress[] getProgress() {
        return new Progress[] { new ProgressImpl(initialized ? 1 : 0, 1, Progress.ENTITIES) };
    }

    @Override
    public void getNext(JCas j) throws IOException, CollectionException {
    	
    	JCas dbView = null;
		try {
			dbView = j.createView(view);
		} catch (CASException e1) {
			e1.printStackTrace();
		}

		NewsModel newsModel = new NewsModel(news_title, news_text);

    	StringBuilder sb = new StringBuilder();
        sb.append(newsModel.toString());

    	
        dbView.setDocumentText(sb.toString());
        j.setDocumentLanguage(language);
        dbView.setDocumentLanguage(language);
    	
        initialized = true;
    }

}
