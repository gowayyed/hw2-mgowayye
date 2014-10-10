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
import java.util.List;

import misc.Base;
import misc.Base.Tag;
import misc.Config;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import ts.PredictedGene;
import ts.Sentence;

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

  private ArrayList<PredictedGene> finalGenes;

  public static final String PROCESSOR_ID = "Final";

  /**
   * The main process method that loads the model, predict the gene names and save the predictions
   * to a file.
   */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    finalGenes = new ArrayList<PredictedGene>();
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
    PredictedGene gene;
    int sid = 0;
    while (fs.hasNext()) {
      Annotation ann = fs.next();
      if (ann.getClass() == Sentence.class) {
        System.out.println(sid++);
        sen = (Sentence) ann;
        sentences.add(sen);
        genes = sen.getPredictedGenes();
        size = genes.size();
        for (int i = 0; i < size; i++) {
          gene = (PredictedGene) genes.get(i);
          // I am just oring the two processors
          if (gene.getCasProcessorId() == DictionaryAnnotator.PROCESSOR_ID
          // ||gene.getCasProcessorId() == TestingAnnotator.PROCESSOR_ID
          )
          finalGenes.add(gene);
//          addOrMergePredictedGene(gene);
        }
      }
    }
    writeFinalPredictionsToFile();
    evaluate();
  }

  @SuppressWarnings("static-access")
  public void addOrMergePredictedGene(PredictedGene gene) {
    List<PredictedGene> overlapping = new ArrayList<PredictedGene>();
    for (int i = 0; i < finalGenes.size(); i++) {
      if (gene.overlaps(finalGenes.get(i)))
        overlapping.add(finalGenes.get(i));
    }
    if (overlapping.size() == 0) {
      finalGenes.add(gene);
    } else {
      for (PredictedGene og : overlapping) {
        finalGenes.remove(og);
        try {
          finalGenes.add(new PredictedGene(this.PROCESSOR_ID, gene.getSentence(), Math.min(
                  gene.getStartIndex(), og.getStartIndex()), Math.max(gene.getEndIndex(),
                  og.getEndIndex()), 1));
        } catch (CASException e) {
          e.printStackTrace();
        }
      }
    }
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
  private void writeFinalPredictionsToFile() {
    for (PredictedGene gene : finalGenes)
      writePrediction(gene.getSentence(), gene.getStartIndex(), gene.getEndIndex());
    try {
      outStream.close();
    } catch (IOException e) {
      e.printStackTrace();
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