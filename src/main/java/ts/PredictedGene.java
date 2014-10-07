

/* First created by JCasGen Tue Oct 07 15:43:18 EDT 2014 */
package ts;

import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import edu.cmu.deiis.types.Annotation;


/** 
 * Updated by JCasGen Tue Oct 07 16:18:28 EDT 2014
 * XML source: /home/gowayyed/workspace/11791/hw2-mgowayye/src/main/resources/descriptors/deiis_types.xml
 * @generated */
public class PredictedGene extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(PredictedGene.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected PredictedGene() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public PredictedGene(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public PredictedGene(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public PredictedGene(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  public PredictedGene(String processor_ID, Sentence sentence, int st, int en, double c) throws CASException {
    super(sentence.getCAS().getJCas());
    setCasProcessorId(processor_ID);
    setSentence(sentence);
    setStartIndex(st);
    setEndIndex(en);
    setConfidence(c);
  }

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: sentence

  /** getter for sentence - gets 
   * @generated
   * @return value of the feature 
   */
  public Sentence getSentence() {
    if (PredictedGene_Type.featOkTst && ((PredictedGene_Type)jcasType).casFeat_sentence == null)
      jcasType.jcas.throwFeatMissing("sentence", "ts.PredictedGene");
    return (Sentence)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PredictedGene_Type)jcasType).casFeatCode_sentence)));}
    
  /** setter for sentence - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSentence(Sentence v) {
    if (PredictedGene_Type.featOkTst && ((PredictedGene_Type)jcasType).casFeat_sentence == null)
      jcasType.jcas.throwFeatMissing("sentence", "ts.PredictedGene");
    jcasType.ll_cas.ll_setRefValue(addr, ((PredictedGene_Type)jcasType).casFeatCode_sentence, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: startIndex

  /** getter for startIndex - gets 
   * @generated
   * @return value of the feature 
   */
  public int getStartIndex() {
    if (PredictedGene_Type.featOkTst && ((PredictedGene_Type)jcasType).casFeat_startIndex == null)
      jcasType.jcas.throwFeatMissing("startIndex", "ts.PredictedGene");
    return jcasType.ll_cas.ll_getIntValue(addr, ((PredictedGene_Type)jcasType).casFeatCode_startIndex);}
    
  /** setter for startIndex - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setStartIndex(int v) {
    if (PredictedGene_Type.featOkTst && ((PredictedGene_Type)jcasType).casFeat_startIndex == null)
      jcasType.jcas.throwFeatMissing("startIndex", "ts.PredictedGene");
    jcasType.ll_cas.ll_setIntValue(addr, ((PredictedGene_Type)jcasType).casFeatCode_startIndex, v);}    
   
    
  //*--------------*
  //* Feature: endIndex

  /** getter for endIndex - gets 
   * @generated
   * @return value of the feature 
   */
  public int getEndIndex() {
    if (PredictedGene_Type.featOkTst && ((PredictedGene_Type)jcasType).casFeat_endIndex == null)
      jcasType.jcas.throwFeatMissing("endIndex", "ts.PredictedGene");
    return jcasType.ll_cas.ll_getIntValue(addr, ((PredictedGene_Type)jcasType).casFeatCode_endIndex);}
    
  /** setter for endIndex - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEndIndex(int v) {
    if (PredictedGene_Type.featOkTst && ((PredictedGene_Type)jcasType).casFeat_endIndex == null)
      jcasType.jcas.throwFeatMissing("endIndex", "ts.PredictedGene");
    jcasType.ll_cas.ll_setIntValue(addr, ((PredictedGene_Type)jcasType).casFeatCode_endIndex, v);}    
  }

    