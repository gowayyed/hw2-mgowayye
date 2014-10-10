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
import ts.Token;

/**
 * This analysis engine is responsible for searching a dictionary of gene names for the gene
 * 
 * @author gowayyed
 *
 */
public class DictionaryAnnotator extends JCasAnnotator_ImplBase {

  public static final String PROCESSOR_ID = "DicrionaryModel";

  /**
   * The stream to write the predictions to.
   */
  private FileWriter outStream;

  private ArrayList<String> dictionary;

  /**
   * The main process method that loads the model, predict the gene names and save the predictions
   * to a file.
   */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    dictionary = new ArrayList<String>();
    try {
      outStream = new FileWriter(new File(Config.outputFilename));
    } catch (IOException e) {
      e.printStackTrace();
    }
    Iterator<Annotation> fs = aJCas.getAnnotationIndex().iterator();
    ArrayList<Sentence> sentences = new ArrayList<Sentence>();
    while (fs.hasNext()) {
      Annotation ann = fs.next();
      if (ann.getClass() == Sentence.class) {
        sentences.add((Sentence) ann);
      }
    }
    // loadDictionary();
    searchForGenes(sentences);
  }

  private void loadDictionary() {
    BufferedReader br;
    try {
      br = new BufferedReader(new FileReader(new File(Config.dictionaryFilename)));
      String line;
      while ((line = br.readLine()) != null) {
        dictionary.add(line.trim());
      }
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * uses the crf to predict a label for each {@link Token}. The fpipe is used to transform features
   * in {@link Token} to features in {@link cc.mallet.types.Token}.
   * 
   * @param crf
   * @param sentences
   * @param fpipe
   */
  private void searchForGenes(List<Sentence> sentences) {
    for (Sentence sentence : sentences) {
      try {
        extractGenesFromDictionary(sentence);
      } catch (CASException e) {
        e.printStackTrace();
      }
    }
    try {
      outStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private ArrayList<PredictedGene> extractGenesFromDictionary(Sentence sentence)
          throws CASException {
    ArrayList<PredictedGene> res = new ArrayList<PredictedGene>();
    FSArray tokensFS = sentence.getTokens();
    Token[] tokens = new Token[tokensFS.size()];
    for (int i = 0; i < tokensFS.size(); i++) {
      tokens[i] = (Token) tokensFS.get(i);
    }
    Token t;
    String gname = "";
    int f = -1, st = -1, en = -1;
    BufferedReader br;
    try {
      br = new BufferedReader(new FileReader(new File(Config.dictionaryFilename)));
      while ((gname = br.readLine()) != null) {
        gname = gname.trim();
        f = sentence.getText().indexOf(gname);
        if (f > -1)
          for (int i = 0; i < tokens.length || (st != -1 && en != -1); i++) {
            t = tokens[i];
            if (t.getStartIndex() == f)
              st = i;
            if (t.getEndIndex() == f + gname.length())
              en = i;
          }
        if (st != -1 && en != -1)
          addGenePrediction(sentence, st, en + 1);
        f = -1;
        st = -1;
        en = -1;
      }
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }

  private void addGenePrediction(Sentence sentence, int st, int en) {
    try {
      PredictedGene gene = new PredictedGene(this.PROCESSOR_ID, sentence, st, en, 1); // TODO check
                                                                                      // how to add
                                                                                      // more
                                                                                      // accurate
                                                                                      // confidence
                                                                                      // value
      gene.addToIndexes();
      sentence.addPredictedGene(gene);
    } catch (CASException e) {
      e.printStackTrace();
    }
  }
}