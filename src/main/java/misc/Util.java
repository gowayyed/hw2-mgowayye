package misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

import ae.TestingAnnotator;
import cc.mallet.fst.CRF;

/**
 * a simple Util class
 * 
 * @author gowayyed
 *
 */
public class Util {

  /**
   * loads the {@link CRF} model from the file to be used in testing.
   * 
   * @return
   */
  public static CRF loadModel() {
    InputStream is = TestingAnnotator.class.getClassLoader().getResourceAsStream(Config.modelFile);
    CRF crf = null;
    try {
      ObjectInputStream in = new ObjectInputStream(is);
      crf = (CRF) in.readObject();
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return crf;
  }

  /**
   * serializes a {@link CRF} model obtained after training to a file.
   * 
   * @param crf
   */
  public static void saveModel(CRF crf) {
    crf.write(new File(Config.modelFile));
  }
}
