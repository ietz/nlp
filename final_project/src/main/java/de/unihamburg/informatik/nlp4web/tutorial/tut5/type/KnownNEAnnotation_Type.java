
/* First created by JCasGen Sat Nov 17 23:32:24 CET 2018 */
package de.unihamburg.informatik.nlp4web.tutorial.tut5.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Sat Nov 17 23:32:31 CET 2018
 * @generated */
public class KnownNEAnnotation_Type extends Annotation_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = KnownNEAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.unihamburg.informatik.nlp4web.tutorial.tut5.type.KnownNEAnnotation");
 
  /** @generated */
  final Feature casFeat_TypeOf;
  /** @generated */
  final int     casFeatCode_TypeOf;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getTypeOf(int addr) {
        if (featOkTst && casFeat_TypeOf == null)
      jcas.throwFeatMissing("TypeOf", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.KnownNEAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_TypeOf);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTypeOf(int addr, String v) {
        if (featOkTst && casFeat_TypeOf == null)
      jcas.throwFeatMissing("TypeOf", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.KnownNEAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_TypeOf, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public KnownNEAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_TypeOf = jcas.getRequiredFeatureDE(casType, "TypeOf", "uima.cas.String", featOkTst);
    casFeatCode_TypeOf  = (null == casFeat_TypeOf) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_TypeOf).getCode();

  }
}



    