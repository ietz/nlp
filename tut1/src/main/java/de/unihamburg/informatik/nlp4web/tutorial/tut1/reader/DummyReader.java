package de.unihamburg.informatik.nlp4web.tutorial.tut1.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

/**
 * 
 * This class gets text from a source (in this case it is hard coded, where a
 * JCAS is created for each text in a List (named texts))
 *
 */

public class DummyReader extends JCasCollectionReader_ImplBase {
    // A source where the corrextion read gets the texts and add it to the JCAS
    // Each text in the list (texts) will be used to create a JCAS
    
    List<String> texts;
    int idx = 0;

    //
    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);

        // ASSIGNMENT 1. Here 1) read the file, 2) for each line in the file, create a
        // JCAS. You can store each line into the texts list so that you do not need to
        // change the getNext, getProgress, and hasNext methods.
        
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("news.txt").getFile());
        
        try {
			texts = Files.lines(file.toPath()).collect(Collectors.toList());
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}
        
        // Output: Read 24 lines
        getLogger().log(
			Level.INFO,
			String.format("Read %d lines\n\n", texts.size())
		);
    }

    @Override
    public void getNext(JCas jcas) throws IOException, CollectionException {
        // add the text to the JCAS. All annotation (AEs) will be based on this text
        jcas.setDocumentText(texts.get(idx));
        jcas.setDocumentLanguage("en");
        idx++;
    }

    @Override
    public Progress[] getProgress() {
        return new Progress[] { new ProgressImpl(idx + 1, texts.size(), Progress.ENTITIES) };
    }

    @Override
    public boolean hasNext() throws IOException, CollectionException {
        return idx < texts.size();
    }

}
