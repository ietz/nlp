package de.unihamburg.informatik.nlp4web.tutorial.tut5.fni;

import com.google.gson.Gson;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.DBAnnotator;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.FeatureAnnotator;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.reader.ConfigurationReader;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.util.OnlineRequestObject;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.writer.OnlineResultWriter;
import org.apache.uima.UIMAException;
import org.cleartk.ml.jar.GenericJarClassifierFactory;

import java.io.File;
import java.io.IOException;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;
import static spark.Spark.*;
import static spark.Spark.post;

public class ExecuteOnlineFakeNewsIdentification {

    public static void classifyOnlineText(String title, String text, String language, File modelDirectory, File stopwordsFile) throws UIMAException, IOException {
        runPipeline(
                createReader(ConfigurationReader.class,
                        ConfigurationReader.PARAM_NEWS_TITLE, title,
                        ConfigurationReader.PARAM_NEWS_TEXT, text,
                        ConfigurationReader.PARAM_LANGUAGE, language,
                        ConfigurationReader.PARAM_VIEW, "DB-VIEW"),
                createEngine(DBAnnotator.class,
                        DBAnnotator.PARAM_VIEW, "DB-VIEW",
                        DBAnnotator.PARAM_STOP_WORDS, stopwordsFile),
            	/*createEngine(AnnotationWriter.class,
    				AnnotationWriter.PARAM_DBVIEW, "DB-VIEW"),*/
                createEngine(FeatureAnnotator.class,
                        FeatureAnnotator.PARAM_IS_TRAINING, false,
                        FeatureAnnotator.PARAM_DIRECTORY_NAME, modelDirectory,
                        FeatureAnnotator.PARAM_TF_IDF_URI,
                        FeatureAnnotator.createTokenTfIdfDataURI(modelDirectory),
                        FeatureAnnotator.PARAM_TF_IDF_CENTROID_SIMILARITY_URI,
                        FeatureAnnotator.createIdfCentroidSimilarityDataURI(modelDirectory),
                        FeatureAnnotator.PARAM_MINMAX_URI,
                        FeatureAnnotator.createMinMaxDataURI(modelDirectory),
                        GenericJarClassifierFactory.PARAM_CLASSIFIER_JAR_PATH, modelDirectory + "/model.jar"),
                createEngine(OnlineResultWriter.class)
        );
    }

    public static void main(String[] args) {
        String modelPath = "src/test/resources/model/";
        new File(modelPath).mkdirs();
        File modelDirectory = new File(modelPath);
        File stopWordsFile = new File("src/main/resources/stopwords.txt");
        String language = "en";
        Gson gson = new Gson();

        externalStaticFileLocation("src/main/resources/public/");

        staticFileLocation("/public");

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        post("/isFake", (req, res) -> {
            OnlineRequestObject obj = gson.fromJson(req.body(), OnlineRequestObject.class);
            classifyOnlineText(obj.getTitle(), obj.getBody(), language, modelDirectory, stopWordsFile);
            boolean real = OnlineResultWriter.real;
            return real;
        });
    }

}
