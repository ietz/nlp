package de.unihamburg.informatik.nlp4web.tutorial.tut5.reader;

import java.io.IOException;
import java.util.List;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.db.FakeNewsNet;
import org.apache.uima.UimaContext;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.db.DBUtils;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.db.NewsModel;

public class DBReader
    extends JCasCollectionReader_ImplBase
{

    public static final String PARAM_VIEW = "VIEW";
    @ConfigurationParameter(name = PARAM_VIEW, description = "The view to write to", mandatory = true)
    private String view;
	
    public static final String PARAM_DB = "DB";
    @ConfigurationParameter(name = PARAM_DB, description = "The DB to be read", mandatory = true)
    private String db;

    public static final String PARAM_LANGUAGE = "language";
    @ConfigurationParameter(name = PARAM_LANGUAGE, defaultValue ="en", description = "default language for the text", mandatory = true)
    private String language;

    public static final String PARAM_FROM = "FROM";
    @ConfigurationParameter(name = PARAM_FROM, description = "The id from where to start (included)", mandatory = true)
    private int from = 0;

    public static final String PARAM_TO = "TO";
    @ConfigurationParameter(name = PARAM_TO, description = "The id where to end (not included)", mandatory = true)
    private int to = 0;
    
    int i = 0;
    int size = 0;
    List<NewsModel> news;
    
    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);

        FakeNewsNet newsNet = FakeNewsNet.load(db);
        this.news = newsNet.getNews();

        i = from;
        size = to;
    }

    @Override
    public boolean hasNext() throws IOException, CollectionException {
        return i < size;
    }

    @Override
    public Progress[] getProgress() {
        return new Progress[] { new ProgressImpl(i, size, Progress.ENTITIES) };
    }

    @Override
    public void getNext(JCas j) throws IOException, CollectionException {
    	JCas dbView = null;
		try {
			dbView = j.createView(view);
		} catch (CASException e1) {
			e1.printStackTrace();
		}
    	
        String s = news.get(i).toString();
        dbView.setDocumentText(s);
        j.setDocumentLanguage(language);
        dbView.setDocumentLanguage(language);
        i++;
    }

}
