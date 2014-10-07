package misc;

import org.apache.uima.jcas.cas.DoubleArray;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.StringArray;

import ts.Sentence;
import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.LabelSequence;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;

/**
 * This class is responsible for converting the features in {@link ts.Token} to the features in
 * {@link Token}. It extends the {@link Pipe} to be able to be run on each {@link Instance}
 * 
 * @author gowayyed
 *
 */
public class ExtractionPipe extends Pipe {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * whether we should add labels: while training, or not: while testing.
   */
  private boolean addLabels = false;

  /**
   * constructs the pipe
   * 
   * @param possibleLabels
   * @param addLabels
   */
  public ExtractionPipe(String[] possibleLabels, boolean addLabels) {
    LabelAlphabet alphabet = new LabelAlphabet();
    for (int i = 0; i < possibleLabels.length; i++) {
      alphabet.lookupIndex(possibleLabels[i]);
    }
    setTargetAlphabet(alphabet);
    this.addLabels = addLabels;
  }

  /**
   * The main methods that is run over each {@link Instance}.
   */
  @Override
  public Instance pipe(Instance carrier) {
    Sentence sentence = (Sentence) carrier.getData();
    FSArray tokens = sentence.getTokens();

    TokenSequence data = new TokenSequence(tokens.size());
    // target is only used when this is training
    LabelSequence target = new LabelSequence((LabelAlphabet) getTargetAlphabet(), tokens.size());
    StringBuffer source = new StringBuffer();
    String text = "";

    for (int i = 0; i < tokens.size(); i++) {
      ts.Token originalToken = sentence.getTokens(i);
      text = originalToken.getText();
      Token token = new Token(text);

      // import feature from the original token
      StringArray featureNames = originalToken.getFeatureNames();
      DoubleArray featureVectors = originalToken.getFeatureVector();

      for (int j = 0; j < featureNames.size(); j++) {
        token.setFeatureValue(featureNames.get(j), featureVectors.get(j));
      }

      // Add token to data
      data.add(token);
      if (addLabels)
        if (originalToken.getLabel() != null)
          target.add(originalToken.getLabel());
        else
          target.add("O");
      else
        target.add("N");

      source.append(token.getText());
      source.append(" ");
    }

    carrier.setData(data);
    carrier.setTarget(target);
    carrier.setSource(source);
    return carrier;
  }
}
