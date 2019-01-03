

/* First created by JCasGen Thu Jan 03 16:42:17 CET 2019 */
package de.unihamburg.informatik.nlp4web.tutorial.tut5.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Jan 03 17:05:12 CET 2019
 * XML source: C:/Development/Git/nlp/final_project/src/main/resources/desc/type/FakeNewsAnnotation.xml
 * @generated */
public class FakeNewsAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(FakeNewsAnnotation.class);
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
  protected FakeNewsAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public FakeNewsAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public FakeNewsAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public FakeNewsAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
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
  //* Feature: source

  /** getter for source - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSource() {
    if (FakeNewsAnnotation_Type.featOkTst && ((FakeNewsAnnotation_Type)jcasType).casFeat_source == null)
      jcasType.jcas.throwFeatMissing("source", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((FakeNewsAnnotation_Type)jcasType).casFeatCode_source);}
    
  /** setter for source - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSource(String v) {
    if (FakeNewsAnnotation_Type.featOkTst && ((FakeNewsAnnotation_Type)jcasType).casFeat_source == null)
      jcasType.jcas.throwFeatMissing("source", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((FakeNewsAnnotation_Type)jcasType).casFeatCode_source, v);}    
   
    
  //*--------------*
  //* Feature: goldValue

  /** getter for goldValue - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getGoldValue() {
    if (FakeNewsAnnotation_Type.featOkTst && ((FakeNewsAnnotation_Type)jcasType).casFeat_goldValue == null)
      jcasType.jcas.throwFeatMissing("goldValue", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((FakeNewsAnnotation_Type)jcasType).casFeatCode_goldValue);}
    
  /** setter for goldValue - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGoldValue(boolean v) {
    if (FakeNewsAnnotation_Type.featOkTst && ((FakeNewsAnnotation_Type)jcasType).casFeat_goldValue == null)
      jcasType.jcas.throwFeatMissing("goldValue", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((FakeNewsAnnotation_Type)jcasType).casFeatCode_goldValue, v);}    
   
    
  //*--------------*
  //* Feature: predictedValue

  /** getter for predictedValue - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getPredictedValue() {
    if (FakeNewsAnnotation_Type.featOkTst && ((FakeNewsAnnotation_Type)jcasType).casFeat_predictedValue == null)
      jcasType.jcas.throwFeatMissing("predictedValue", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((FakeNewsAnnotation_Type)jcasType).casFeatCode_predictedValue);}
    
  /** setter for predictedValue - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPredictedValue(boolean v) {
    if (FakeNewsAnnotation_Type.featOkTst && ((FakeNewsAnnotation_Type)jcasType).casFeat_predictedValue == null)
      jcasType.jcas.throwFeatMissing("predictedValue", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((FakeNewsAnnotation_Type)jcasType).casFeatCode_predictedValue, v);}    
   
    
  //*--------------*
  //* Feature: authors

  /** getter for authors - gets 
   * @generated
   * @return value of the feature 
   */
  public String getAuthors() {
    if (FakeNewsAnnotation_Type.featOkTst && ((FakeNewsAnnotation_Type)jcasType).casFeat_authors == null)
      jcasType.jcas.throwFeatMissing("authors", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((FakeNewsAnnotation_Type)jcasType).casFeatCode_authors);}
    
  /** setter for authors - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAuthors(String v) {
    if (FakeNewsAnnotation_Type.featOkTst && ((FakeNewsAnnotation_Type)jcasType).casFeat_authors == null)
      jcasType.jcas.throwFeatMissing("authors", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((FakeNewsAnnotation_Type)jcasType).casFeatCode_authors, v);}    
   
    
  //*--------------*
  //* Feature: id

  /** getter for id - gets 
   * @generated
   * @return value of the feature 
   */
  public long getId() {
    if (FakeNewsAnnotation_Type.featOkTst && ((FakeNewsAnnotation_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return jcasType.ll_cas.ll_getLongValue(addr, ((FakeNewsAnnotation_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(long v) {
    if (FakeNewsAnnotation_Type.featOkTst && ((FakeNewsAnnotation_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    jcasType.ll_cas.ll_setLongValue(addr, ((FakeNewsAnnotation_Type)jcasType).casFeatCode_id, v);}    
  }

    