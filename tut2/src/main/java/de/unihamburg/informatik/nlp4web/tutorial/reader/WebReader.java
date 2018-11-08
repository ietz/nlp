package de.unihamburg.informatik.nlp4web.tutorial.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class WebReader extends JCasCollectionReader_ImplBase {
	
	private List<String> comments;
	private int i;
	

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		
		try {
			Document doc = Jsoup.connect("http://fairytalesoftheworld.com/quick-reads/rumpelstilskin/").get();
			
			i = 0;
			comments = doc.select(".entry-content p").stream()
					.map(Element::text)
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean hasNext() throws IOException, CollectionException {
		return i < comments.size();
	}

	public Progress[] getProgress() {
		Progress pageProgress = new ProgressImpl(i, comments.size(), "Comments");
		return new Progress[] {pageProgress};
	}

	@Override
	public void getNext(JCas jCas) throws IOException, CollectionException {
		jCas.setDocumentText(comments.get(i));
		jCas.setDocumentLanguage("en");
		i++;
	}
}
