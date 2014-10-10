
/* First created by JCasGen Tue Oct 07 15:43:18 EDT 2014 */
package ts;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import edu.cmu.deiis.types.Annotation_Type;

/** 
 * Updated by JCasGen Tue Oct 07 21:32:14 EDT 2014
 * @generated */
public class PredictedGene_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (PredictedGene_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = PredictedGene_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new PredictedGene(addr, PredictedGene_Type.this);
  			   PredictedGene_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new PredictedGene(addr, PredictedGene_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = PredictedGene.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("ts.PredictedGene");
 
  /** @generated */
  final Feature casFeat_sentence;
  /** @generated */
  final int     casFeatCode_sentence;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getSentence(int addr) {
        if (featOkTst && casFeat_sentence == null)
      jcas.throwFeatMissing("sentence", "ts.PredictedGene");
    return ll_cas.ll_getRefValue(addr, casFeatCode_sentence);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSentence(int addr, int v) {
        if (featOkTst && casFeat_sentence == null)
      jcas.throwFeatMissing("sentence", "ts.PredictedGene");
    ll_cas.ll_setRefValue(addr, casFeatCode_sentence, v);}
    
  
 
  /** @generated */
  final Feature casFeat_startIndex;
  /** @generated */
  final int     casFeatCode_startIndex;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getStartIndex(int addr) {
        if (featOkTst && casFeat_startIndex == null)
      jcas.throwFeatMissing("startIndex", "ts.PredictedGene");
    return ll_cas.ll_getIntValue(addr, casFeatCode_startIndex);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setStartIndex(int addr, int v) {
        if (featOkTst && casFeat_startIndex == null)
      jcas.throwFeatMissing("startIndex", "ts.PredictedGene");
    ll_cas.ll_setIntValue(addr, casFeatCode_startIndex, v);}
    
  
 
  /** @generated */
  final Feature casFeat_endIndex;
  /** @generated */
  final int     casFeatCode_endIndex;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getEndIndex(int addr) {
        if (featOkTst && casFeat_endIndex == null)
      jcas.throwFeatMissing("endIndex", "ts.PredictedGene");
    return ll_cas.ll_getIntValue(addr, casFeatCode_endIndex);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEndIndex(int addr, int v) {
        if (featOkTst && casFeat_endIndex == null)
      jcas.throwFeatMissing("endIndex", "ts.PredictedGene");
    ll_cas.ll_setIntValue(addr, casFeatCode_endIndex, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public PredictedGene_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_sentence = jcas.getRequiredFeatureDE(casType, "sentence", "ts.Sentence", featOkTst);
    casFeatCode_sentence  = (null == casFeat_sentence) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_sentence).getCode();

 
    casFeat_startIndex = jcas.getRequiredFeatureDE(casType, "startIndex", "uima.cas.Integer", featOkTst);
    casFeatCode_startIndex  = (null == casFeat_startIndex) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_startIndex).getCode();

 
    casFeat_endIndex = jcas.getRequiredFeatureDE(casType, "endIndex", "uima.cas.Integer", featOkTst);
    casFeatCode_endIndex  = (null == casFeat_endIndex) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_endIndex).getCode();

  }
}



    