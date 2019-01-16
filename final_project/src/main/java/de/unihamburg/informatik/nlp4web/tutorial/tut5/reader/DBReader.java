package de.unihamburg.informatik.nlp4web.tutorial.tut5.reader;

import java.io.IOException;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.FeatureAnnotator;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.db.FakeNewsNet;
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

    @ConfigurationParameter(name = FeatureAnnotator.PARAM_IS_TRAINING, mandatory = true)
    private boolean isTrain;

//    private int i;
    private List<NewsModel> news;
    private boolean initialized;
    
    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);

//        i = 0;
        FakeNewsNet newsNet = FakeNewsNet.load(db);
        this.news = isTrain ? newsNet.getTrain() : newsNet.getTest();
        
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
    	
    	StringBuilder sb = new StringBuilder();
    	for(NewsModel newsModel : news) {
    		sb.append(newsModel.toString());
    	}
    	
        dbView.setDocumentText(sb.toString());
        j.setDocumentLanguage(language);
        dbView.setDocumentLanguage(language);
    	
        initialized = true;

//        String s = news.get(i).toString();
//        i++;
    }

}
