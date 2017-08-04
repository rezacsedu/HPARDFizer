/**
 * 
 */
package org.insight.HPA;

public interface PROTEINATLAS {

	/**
	 * Data Properties
	 */
	static final String age = "age";
	static final String antigenSequence = "antigenSequence";
	static final String assayType = "assayType";
	static final String cellLine = "cellLine";
	static final String cellType = "cellType";
	static final String channel = "channel";
	static final String description = "description";
	static final String expressionLevel = "expressionLevel";
	static final String geneSynonym = "geneSynonym";
	static final String ID = "ID";
	static final String imageType = "imageType";
	static final String imageUrl = "imageUrl";
	static final String intensityLevel = "intensityLevel";
	static final String level = "level";
	static final String location = "location";
	static final String message = "message";
	static final String name = "name";
	static final String patientId = "patientId";
	static final String quantity = "quantity";
	static final String reliability = "reliability";
	static final String sex = "sex";
	static final String source = "source";
	static final String stainingLevel = "stainingLevel";
	static final String technology = "technology";
	static final String tissue = "tissue";
	static final String tpm = "tpm";
	static final String type = "type";
	static final String unit = "unit";
	static final String validation = "validation";
	static final String validationType = "validationType";
	static final String value = "value";

	/**
	 * Object Properties
	 */
	static final String hasAntibody = "hasAntibody";
	static final String hasAssayImage = "hasAssayImage";
	static final String hasBlotLane = "hasBlotLane";
	static final String hasCellExpression = "hasCellExpression";
	static final String hasData = "hasData";
	static final String hasImage = "hasImage";
	static final String hasPatient = "hasPatient";
	static final String hasProtein = "hasProtein";
	static final String hasProteinClass = "hasProteinClass";
	static final String hasProteinEvidence = "hasProteinEvidence";
	static final String hasRnaExpression = "hasRnaExpression";
	static final String hasRnaTissueCategory = "hasRnaTissueCategory";
	static final String hasSubAssay = "hasSubAssay";
	static final String hasSummary = "hasSummary";
	static final String hasTissueCell = "hasTissueCell";
	static final String hasTissueExpression = "hasTissueExpression";
	static final String hasValidation = "hasValidation";
	static final String hasVerification = "hasVerification";
	static final String hasWeight = "hasWeight";
	static final String hasWesternBlot = "hasWesternBlot";
	static final String isIdentifiedBy = "isIdentifiedBy";

	/**
	 * Classes
	 */
	static final String Antibody = "Antibody:";
	static final String AB_CellExpression = "AB_CellExpression:";
	static final String ACE_SubAssay = "ACE_SubAssay:";
	static final String ACE_Data = "ACE_Data:";
	static final String ACE_AssayImage = "ACE_AssayImage:";
	static final String ACE_Image = "ACE_Image:";
	static final String AB_ProteinArray = "AB_ProteinArray:";
	static final String APA_Image = "APA_Image:";
	static final String APA_Verification = "APA_Verification:";
	static final String AB_TargetWeights = "AB_TargetWeights:";
	static final String AB_TissueExpression = "AB_TissueExpression:";
	static final String ATE_Data = "ATE_Data:";
	static final String ATE_Patient = "ATE_Patient:";
	static final String ATE_TissueCell = "ATE_TissueCell:";
	static final String ATE_Image = "ATE_Image:";
	static final String ATE_Summary = "ATE_Summary:";
	static final String ATE_Validation = "ATE_Validation:";
	static final String AB_WesternBlot = "AB_WesternBlot:";
	static final String AWB_BlotLanes = "AWB_BlotLanes:";
	static final String AWB_Image = "AWB_Image:";
	static final String AWB_Verification = "AWB_Verification:";
	static final String CellExpression = "CellExpression:";
	static final String CE_Data = "CE_Data:";
	static final String CE_Image = "CE_Image:";
	static final String CE_Summary = "CE_Summary:";
	static final String CE_Verification = "CE_Verification:";
	static final String Gene = "Gene:";
	static final String Identifier = "Identifier:";
	static final String ProteinClasses = "ProteinClasses:";
	static final String ProteinClass = "ProteinClass:";
	static final String ProteinEvidence = "ProteinEvidence:";
	static final String Evidence = "Evidence:";
	static final String RnaExpression = "RnaExpression:";
	static final String RE_Data = "RE_Data:";
	static final String RnaTissueCategory = "RnaTissueCategory:";
	static final String TissueExpression = "TissueExpression:";
	static final String TE_Data = "TE_Data:";
	static final String TE_TissueCell = "TE_TissueCell:";
	static final String TE_Image = "TE_Image:";
	static final String TE_Summary = "TE_Summary:";
	static final String TE_Verification = "TE_Verification:";
}
