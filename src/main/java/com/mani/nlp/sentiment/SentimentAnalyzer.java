package com.mani.nlp.sentiment;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Manindar
 */
public class SentimentAnalyzer {

//    This method written based on stanford-corenlp-3.4 version
    public String getSentiment(String text, StanfordCoreNLP pipeline) {

        int mainSentiment = 2;
        int longest;
        if (text != null && text.length() > 0) {
            longest = 0;
            Annotation annotation = pipeline.process(text);
            for (Object sentence : (List) annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = (Tree) ((CoreMap) sentence).get(SentimentCoreAnnotations.AnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }
        if (mainSentiment <= 1) {
            return "negative";
        } else if (mainSentiment == 2) {
            return "neutral";
        } else {
            return "positive";
        }
    }

    public static void main(String[] s) {
        SentimentAnalyzer sa = new SentimentAnalyzer();
        Properties properties = new Properties();
//        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        properties.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);
        String statement = "The movie was good";
        System.out.println(statement + "---> " + sa.getSentiment(statement, pipeline));
        statement = "I wateched movie";
        System.out.println(statement + "---> " + sa.getSentiment(statement, pipeline));
        statement = "The movie was not good";
        System.out.println(statement + "---> " + sa.getSentiment(statement, pipeline));
    }
}
