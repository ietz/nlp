

/* First created by JCasGen Sat Nov 17 23:32:24 CET 2018 */
package de.unihamburg.informatik.nlp4web.tutorial.tut5.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Nov 17 23:32:31 CET 2018
 * XML source: C:/Users/Derp/Projects/nlp/tut5pos/src/main/resources/desc/type/KnownNEAnnotation.xml
 * @generated */
public class KnownNEAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(KnownNEAnnotation.class);
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
  protected KnownNEAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public KnownNEAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public KnownNEAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public KnownNEAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: TypeOf

  /** getter for TypeOf - gets 
   * @generated
   * @return value of the feature 
   */
  public String getTypeOf() {
    if (KnownNEAnnotation_Type.featOkTst && ((KnownNEAnnotation_Type)jcasType).casFeat_TypeOf == null)
      jcasType.jcas.throwFeatMissing("TypeOf", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.KnownNEAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((KnownNEAnnotation_Type)jcasType).casFeatCode_TypeOf);}
    
  /** setter for TypeOf - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTypeOf(String v) {
    if (KnownNEAnnotation_Type.featOkTst && ((KnownNEAnnotation_Type)jcasType).casFeat_TypeOf == null)
      jcasType.jcas.throwFeatMissing("TypeOf", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.KnownNEAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((KnownNEAnnotation_Type)jcasType).casFeatCode_TypeOf, v);}    
  }

    