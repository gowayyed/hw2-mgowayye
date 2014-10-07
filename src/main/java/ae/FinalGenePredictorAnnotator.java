package ae;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import misc.Base;
import misc.Base.Tag;
import misc.Config;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import ts.Sentence;
import cc.mallet.types.Sequence;

/**
 * This analysis engine is responsible for loading a CRF model and using it to predict gene names,
 * then evaluate the predictions and saves them to an output file.
 * 
 * @author gowayyed
 *
 */
public class FinalGenePredictorAnnotator extends JCasAnnotator_ImplBase {
  /**
   * The stream to write the predictions to.
   */
  private FileWriter outStream;

  
  /**
   * The main process method that loads the model, predict the gene names and save the predictions
   * to a file.
   */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    try {
      outStream = new FileWriter(new File(Config.outputFilename));
    } catch (IOException e) {
      e.printStackTrace();
    }
    Iterator<Annotation> fs = aJCas.getAnnotationIndex().iterator();
    ArrayList<Sentence> sentences = new ArrayList<Sentence>();
    Sentence sen;
    int size;
    FSArray genes;
    while (fs.hasNext()) {
      Annotation ann = fs.next();
      if (ann.getClass() == Sentence.class) {
        sen = (Sentence) ann; 
        sentences.add(sen);
        genes = sen.getPredictedGenes();
        size = genes.size();
        for(int i = 0; i < size; i++)
        {
          genes.get(i);
        }
      }
    }
    evaluate();
  }

  /**
   * This method calculates and displays the F1 score of the predicted gene names.
   * 
   * @param sentences
   */
  private void evaluate() {
    BufferedReader br, pbr;
    try {
      br = new BufferedReader(new FileReader(new File(Config.referenceFilename)));
      pbr = new BufferedReader(new FileReader(new File(Config.outputFilename)));
      HashMap<String, LinkedList<Base.Tag>> tags = Base.getTags(br);
      HashMap<String, LinkedList<Base.Tag>> predictedTags = Base.getTags(pbr);
      br.close();
      pbr.close();
      boolean found = false;
      double truePositive = 0;
      for (String key : tags.keySet()) {
        for (Base.Tag tag : tags.get(key)) {
          found = false;
          if (predictedTags.get(key) != null)
            for (Base.Tag ptag : predictedTags.get(key)) {
              if (tag.contains(ptag))
                found = true; // correct
            }
          if (found)
            truePositive++;
        }
      }
      double precision = truePositive / getMentionsLength(predictedTags);
      double recall = truePositive / getMentionsLength(tags);
      double f1Score = 2 * precision * recall / (precision + recall);
      System.out.println("Precision = " + precision + "recall = " + recall + " F1 score = "
              + f1Score);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the length of the mentions to use in calculating the precision and the recall.
   * 
   * @param tags
   * @return
   */
  private double getMentionsLength(HashMap<String, LinkedList<Tag>> tags) {
    double res = 0;
    for (String key : tags.keySet()) {
      for (Base.Tag tag : tags.get(key)) {
        res++;
      }
    }
    return res;
  }

  /**
   * write the predicted mentions to the output predictions file
   * 
   * @param sentence
   * @param predicted
   */
  private void writeMentions(Sentence sentence, Sequence predicted) {
    for (int i = 0; i < predicted.size(); i++) {
      if ("B".equals(predicted.get(i))) { // each mention starts with B and continues until the next
                                          // O
        int en = i + 1;
        while (en < predicted.size() && "I".equals(predicted.get(en)))
          en++;
        writePrediction(sentence, i, en);
        i = en - 1;
      }
    }
  }
  
  /**
   * writes the predictions in the sentence to the predictions file
   * 
   * @param sentence
   * @param st
   * @param en
   */
  private void writePrediction(Sentence sentence, int st, int en) {
    try {
      outStream.write((sentence.getId() + "|").toCharArray());
      outStream.write((getPreceedingChars(sentence.getText(), sentence.getTokens(st)
              .getStartIndex()) + " ").toCharArray());
      outStream.write((getPreceedingChars(sentence.getText(), sentence.getTokens(en - 1)
              .getEndIndex() - 1) + "|").toCharArray());
      outStream.write((sentence.getText().substring(sentence.getTokens(st).getStartIndex(),
              sentence.getTokens(en - 1).getEndIndex()) + "\n").toCharArray());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * get the count of the preceding characters for a specific index in the string.
   * 
   * @param text
   * @param charIndex
   * @return
   */
  private int getPreceedingChars(String text, int charIndex) {
    int c = 0;
    for (int i = 0; i < charIndex; i++) {
      if (!Character.isWhitespace(text.charAt(i)))
        c++;
    }
    return c;
  }
}