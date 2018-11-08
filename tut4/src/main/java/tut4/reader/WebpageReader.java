package tut4.reader;

import java.io.IOException;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WebpageReader
    extends JCasCollectionReader_ImplBase
{

    public static final String PARAM_URL = "URL";
    @ConfigurationParameter(name = PARAM_URL, description = "The URL of the website to be read", mandatory = true)
    private String url;

    public static final String PARAM_LANGUAGE = "language";
    @ConfigurationParameter(name = PARAM_LANGUAGE, defaultValue ="en", description = "default language for the text", mandatory = true)
    private String language;

    public static final String PARAM_SELECTOR = "SELECTOR";
    @ConfigurationParameter(name = PARAM_SELECTOR, description = "The selector to find element. (Optional)", mandatory = false)
    private String selector = null;

    Document document;
    Elements domElements;
    int i = 0;
    int size = 0;

    @Override
    public void initialize(UimaContext context)
        throws ResourceInitializationException
    {
        super.initialize(context);

        try {
            document = Jsoup.connect(url).get();

            if (selector == null) {
                domElements = document.select("body");
            }
            else {
                domElements = document.select(selector);
            }

            size = domElements.size();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean hasNext()
        throws IOException, CollectionException
    {
        return i < size;
    }

    @Override
    public Progress[] getProgress()
    {
        return new Progress[] { new ProgressImpl(i, size, Progress.ENTITIES) };
    }

    @Override
    public void getNext(JCas j)
        throws IOException, CollectionException
    {
        String s = domElements.get(i).text();
        j.setDocumentText(s);
        j.setDocumentLanguage(language);
        i++;
    }

}
