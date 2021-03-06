
/* First created by JCasGen Thu Jan 03 16:42:17 CET 2019 */
package de.unihamburg.informatik.nlp4web.tutorial.tut5.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Sun Jan 13 17:04:12 CET 2019
 * @generated */
public class FakeNewsAnnotation_Type extends Annotation_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = FakeNewsAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
 
  /** @generated */
  final Feature casFeat_source;
  /** @generated */
  final int     casFeatCode_source;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getSource(int addr) {
        if (featOkTst && casFeat_source == null)
      jcas.throwFeatMissing("source", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_source);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSource(int addr, String v) {
        if (featOkTst && casFeat_source == null)
      jcas.throwFeatMissing("source", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_source, v);}
    
  
 
  /** @generated */
  final Feature casFeat_goldValue;
  /** @generated */
  final int     casFeatCode_goldValue;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getGoldValue(int addr) {
        if (featOkTst && casFeat_goldValue == null)
      jcas.throwFeatMissing("goldValue", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_goldValue);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setGoldValue(int addr, boolean v) {
        if (featOkTst && casFeat_goldValue == null)
      jcas.throwFeatMissing("goldValue", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_goldValue, v);}
    
  
 
  /** @generated */
  final Feature casFeat_predictedValue;
  /** @generated */
  final int     casFeatCode_predictedValue;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getPredictedValue(int addr) {
        if (featOkTst && casFeat_predictedValue == null)
      jcas.throwFeatMissing("predictedValue", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_predictedValue);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPredictedValue(int addr, boolean v) {
        if (featOkTst && casFeat_predictedValue == null)
      jcas.throwFeatMissing("predictedValue", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_predictedValue, v);}
    
  
 
  /** @generated */
  final Feature casFeat_authors;
  /** @generated */
  final int     casFeatCode_authors;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getAuthors(int addr) {
        if (featOkTst && casFeat_authors == null)
      jcas.throwFeatMissing("authors", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_authors);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setAuthors(int addr, String v) {
        if (featOkTst && casFeat_authors == null)
      jcas.throwFeatMissing("authors", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_authors, v);}
    
  
 
  /** @generated */
  final Feature casFeat_id;
  /** @generated */
  final int     casFeatCode_id;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public long getId(int addr) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return ll_cas.ll_getLongValue(addr, casFeatCode_id);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setId(int addr, long v) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    ll_cas.ll_setLongValue(addr, casFeatCode_id, v);}
    
  
 
  /** @generated */
  final Feature casFeat_shareCount;
  /** @generated */
  final int     casFeatCode_shareCount;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public long getShareCount(int addr) {
        if (featOkTst && casFeat_shareCount == null)
      jcas.throwFeatMissing("shareCount", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return ll_cas.ll_getLongValue(addr, casFeatCode_shareCount);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setShareCount(int addr, long v) {
        if (featOkTst && casFeat_shareCount == null)
      jcas.throwFeatMissing("shareCount", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    ll_cas.ll_setLongValue(addr, casFeatCode_shareCount, v);}
    
  
 
  /** @generated */
  final Feature casFeat_title;
  /** @generated */
  final int     casFeatCode_title;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getTitle(int addr) {
        if (featOkTst && casFeat_title == null)
      jcas.throwFeatMissing("title", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_title);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTitle(int addr, String v) {
        if (featOkTst && casFeat_title == null)
      jcas.throwFeatMissing("title", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_title, v);}
    
  
 
  /** @generated */
  final Feature casFeat_body;
  /** @generated */
  final int     casFeatCode_body;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getBody(int addr) {
        if (featOkTst && casFeat_body == null)
      jcas.throwFeatMissing("body", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_body);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setBody(int addr, String v) {
        if (featOkTst && casFeat_body == null)
      jcas.throwFeatMissing("body", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_body, v);}
    
  
 
  /** @generated */
  final Feature casFeat_shareUserCount;
  /** @generated */
  final int     casFeatCode_shareUserCount;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public long getShareUserCount(int addr) {
        if (featOkTst && casFeat_shareUserCount == null)
      jcas.throwFeatMissing("shareUserCount", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return ll_cas.ll_getLongValue(addr, casFeatCode_shareUserCount);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setShareUserCount(int addr, long v) {
        if (featOkTst && casFeat_shareUserCount == null)
      jcas.throwFeatMissing("shareUserCount", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    ll_cas.ll_setLongValue(addr, casFeatCode_shareUserCount, v);}
    
  
 
  /** @generated */
  final Feature casFeat_maxUserShareCount;
  /** @generated */
  final int     casFeatCode_maxUserShareCount;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public long getMaxUserShareCount(int addr) {
        if (featOkTst && casFeat_maxUserShareCount == null)
      jcas.throwFeatMissing("maxUserShareCount", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    return ll_cas.ll_getLongValue(addr, casFeatCode_maxUserShareCount);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setMaxUserShareCount(int addr, long v) {
        if (featOkTst && casFeat_maxUserShareCount == null)
      jcas.throwFeatMissing("maxUserShareCount", "de.unihamburg.informatik.nlp4web.tutorial.tut5.type.FakeNewsAnnotation");
    ll_cas.ll_setLongValue(addr, casFeatCode_maxUserShareCount, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public FakeNewsAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_source = jcas.getRequiredFeatureDE(casType, "source", "uima.cas.String", featOkTst);
    casFeatCode_source  = (null == casFeat_source) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_source).getCode();

 
    casFeat_goldValue = jcas.getRequiredFeatureDE(casType, "goldValue", "uima.cas.Boolean", featOkTst);
    casFeatCode_goldValue  = (null == casFeat_goldValue) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_goldValue).getCode();

 
    casFeat_predictedValue = jcas.getRequiredFeatureDE(casType, "predictedValue", "uima.cas.Boolean", featOkTst);
    casFeatCode_predictedValue  = (null == casFeat_predictedValue) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_predictedValue).getCode();

 
    casFeat_authors = jcas.getRequiredFeatureDE(casType, "authors", "uima.cas.String", featOkTst);
    casFeatCode_authors  = (null == casFeat_authors) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_authors).getCode();

 
    casFeat_id = jcas.getRequiredFeatureDE(casType, "id", "uima.cas.Long", featOkTst);
    casFeatCode_id  = (null == casFeat_id) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_id).getCode();

 
    casFeat_shareCount = jcas.getRequiredFeatureDE(casType, "shareCount", "uima.cas.Long", featOkTst);
    casFeatCode_shareCount  = (null == casFeat_shareCount) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_shareCount).getCode();

 
    casFeat_title = jcas.getRequiredFeatureDE(casType, "title", "uima.cas.String", featOkTst);
    casFeatCode_title  = (null == casFeat_title) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_title).getCode();

 
    casFeat_body = jcas.getRequiredFeatureDE(casType, "body", "uima.cas.String", featOkTst);
    casFeatCode_body  = (null == casFeat_body) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_body).getCode();

 
    casFeat_shareUserCount = jcas.getRequiredFeatureDE(casType, "shareUserCount", "uima.cas.Long", featOkTst);
    casFeatCode_shareUserCount  = (null == casFeat_shareUserCount) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_shareUserCount).getCode();

 
    casFeat_maxUserShareCount = jcas.getRequiredFeatureDE(casType, "maxUserShareCount", "uima.cas.Long", featOkTst);
    casFeatCode_maxUserShareCount  = (null == casFeat_maxUserShareCount) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_maxUserShareCount).getCode();

  }
}



    