/**
 * 
 */
package org.insight.SingleGene;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.jena.ontology.Individual;

public class LoadHpa {

	// the input file location
	private static final String fileLocation = "C:/Exp/proteinatlas.xml";

	static Integer Antibody = 1;
	static Integer AB_CellExpression = 1;
	static Integer ACE_SubAssay = 1;
	static Integer ACE_Data = 1;
	static Integer ACE_AssayImage = 1;
	static Integer ACE_Image = 1;
	static Integer AB_ProteinArray = 1;
	static Integer APA_Image = 1;
	static Integer APA_Verification = 1;
	static Integer AB_TargetWeights = 1;
	static Integer AB_TissueExpression = 1;
	static Integer ATE_Data = 1;
	static Integer ATE_Patient = 1;
	static Integer ATE_TissueCell = 1;
	static Integer ATE_Image = 1;
	static Integer ATE_Summary = 1;
	static Integer ATE_Validation = 1;
	static Integer AB_WesternBlot = 1;
	static Integer AWB_BlotLanes = 1;
	static Integer AWB_Image = 1;
	static Integer AWB_Verification = 1;
	static Integer CellExpression = 1;
	static Integer CE_Data = 1;
	static Integer CE_Image = 1;
	static Integer CE_Summary = 1;
	static Integer CE_Verification = 1;
	static Integer Gene = 1;
	static Integer Identifier = 1;
	static Integer ProteinClasses = 1;
	static Integer ProteinClass = 1;
	static Integer ProteinEvidence = 1;
	static Integer Evidence = 1;
	static Integer RnaExpression = 1;
	static Integer RE_Data = 1;
	static Integer RnaTissueCategory = 1;
	static Integer TissueExpression = 1;
	static Integer TE_Data = 1;
	static Integer TE_TissueCell = 1;
	static Integer TE_Image = 1;
	static Integer TE_Summary = 1;
	static Integer TE_Verification = 1;

	private static OntoModel ontoModel = new OntoModel();

	public static void load() {

		// get the XML file handler
		try {
			FileInputStream fileInputStream = new FileInputStream(fileLocation);
			XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(fileInputStream);

			int index = 1;

			while (xmlStreamReader.hasNext()) {
				int eventCode = xmlStreamReader.next();
				if (XMLStreamConstants.START_ELEMENT == eventCode
						&& xmlStreamReader.getLocalName().equalsIgnoreCase(ElementNode.proteinAtlas)) {

					while (xmlStreamReader.hasNext()) {

						eventCode = xmlStreamReader.next();

						if (XMLStreamConstants.END_ELEMENT == eventCode
								&& xmlStreamReader.getLocalName().equalsIgnoreCase(ElementNode.proteinAtlas)) {
							break;
						} else {
							if (XMLStreamConstants.START_ELEMENT == eventCode
									&& xmlStreamReader.getLocalName().equalsIgnoreCase(ElementNode.entry)) {
								System.out.println(index);
								// if (index >= 1 && index <= 10) {

								// ontoModel.saveOntoModel();
								System.out.println("Element - " + index);

								boolean process = false;
								String geneID = "";
								int attributesCount = xmlStreamReader.getAttributeCount();
								for (int i = 0; i < attributesCount; i++) {
									System.out.println("Entry: " + xmlStreamReader.getAttributeLocalName(i) + " "
											+ xmlStreamReader.getAttributeValue(i));
									String url = xmlStreamReader.getAttributeLocalName(i);

									if (url.equalsIgnoreCase("url")) {
										geneID = xmlStreamReader.getAttributeValue(i);
										if (geneID.equalsIgnoreCase("http://v16.proteinatlas.org/ENSG00000026508"))
											process = true;
									}
								}

								if (process) {
									String baseInstnace = geneID.substring("http://v16.proteinatlas.org/".length(),
											geneID.length());
									System.out.println("identifierIndividual"+ baseInstnace);
									Individual geneIndividual = ontoModel.createIndividual(PROTEINATLAS.Gene,
											"http://data.bioinfo.deri.ie/gene/" + baseInstnace);
									geneIndividual.addLiteral(ontoModel.getProperty(PROTEINATLAS.ID), geneID);
									while (xmlStreamReader.hasNext()) {
										eventCode = xmlStreamReader.next();
										if (XMLStreamConstants.END_ELEMENT == eventCode
												&& xmlStreamReader.getLocalName().equalsIgnoreCase(ElementNode.entry)) {
											break;
										} else {
											if (XMLStreamConstants.START_ELEMENT == eventCode) {
												if (xmlStreamReader.getLocalName().equalsIgnoreCase(ElementNode.name)) {
													String name = xmlStreamReader.getElementText();
													geneIndividual.addLiteral(ontoModel.getProperty(PROTEINATLAS.name),
															name);
												} else if (xmlStreamReader.getLocalName()
														.equalsIgnoreCase(ElementNode.synonym)) {
													String synonym = xmlStreamReader.getElementText();
													geneIndividual.addLiteral(
															ontoModel.getProperty(PROTEINATLAS.geneSynonym), synonym);
												} else if (xmlStreamReader.getLocalName()
														.equalsIgnoreCase(ElementNode.identifier)) {
													Individual identifierIndividual = ontoModel
															.createIndividual(PROTEINATLAS.Identifier, Identifier++);
													geneIndividual.addProperty(
															ontoModel.getProperty(PROTEINATLAS.isIdentifiedBy),
															identifierIndividual);
													int identifierAttributesCount = xmlStreamReader.getAttributeCount();
													for (int i = 0; i < identifierAttributesCount; i++) {
														String attribute = xmlStreamReader.getAttributeLocalName(i);
														if (attribute.equalsIgnoreCase(PROTEINATLAS.ID)) {
															String value = xmlStreamReader.getAttributeValue(i);
															identifierIndividual.addLiteral(
																	ontoModel.getProperty(PROTEINATLAS.ID), value);
														}
													}

													/*
													 * while
													 * (xmlStreamReader.hasNext(
													 * )) { eventCode =
													 * xmlStreamReader.next();
													 * if (XMLStreamConstants.
													 * END_ELEMENT == eventCode
													 * && xmlStreamReader.
													 * getLocalName().
													 * equalsIgnoreCase(
													 * ElementNode.identifier))
													 * { break; } else { if
													 * (XMLStreamConstants.
													 * START_ELEMENT ==
													 * eventCode &&
													 * xmlStreamReader.
													 * getLocalName().
													 * equalsIgnoreCase(
													 * ElementNode.xref)) { int
													 * xrefAttributesCount =
													 * xmlStreamReader.
													 * getAttributeCount(); for
													 * (int i = 0; i <
													 * xrefAttributesCount; i++)
													 * { System.out.println(
													 * "xref: " +
													 * xmlStreamReader.
													 * getAttributeLocalName(i)
													 * + " " + xmlStreamReader.
													 * getAttributeValue(i)); }
													 * } } }
													 */
												} else if (xmlStreamReader.getLocalName()
														.equalsIgnoreCase(ElementNode.proteinClasses)) {

													while (xmlStreamReader.hasNext()) {
														eventCode = xmlStreamReader.next();
														if (XMLStreamConstants.END_ELEMENT == eventCode
																&& xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.proteinClasses)) {
															break;
														} else {
															if (XMLStreamConstants.START_ELEMENT == eventCode
																	&& xmlStreamReader.getLocalName().equalsIgnoreCase(
																			ElementNode.proteinClass)) {
																Individual proteinClassIndividual = ontoModel
																		.createIndividual(PROTEINATLAS.ProteinClass,
																				ProteinClass++);
																geneIndividual.addProperty(
																		ontoModel.getProperty(
																				PROTEINATLAS.hasProteinClass),
																		proteinClassIndividual);
																int proteinClassAttributesCount = xmlStreamReader
																		.getAttributeCount();
																for (int i = 0; i < proteinClassAttributesCount; i++) {
																	String attribute = xmlStreamReader
																			.getAttributeLocalName(i);
																	String value = xmlStreamReader.getAttributeValue(i);
																	if (attribute.equalsIgnoreCase(PROTEINATLAS.ID)) {
																		proteinClassIndividual.addLiteral(
																				ontoModel.getProperty(PROTEINATLAS.ID),
																				value);
																	} else if (attribute
																			.equalsIgnoreCase(PROTEINATLAS.name)) {
																		proteinClassIndividual.addLiteral(ontoModel
																				.getProperty(PROTEINATLAS.name), value);
																	}
																}
															}
														}
													}
												} else if (xmlStreamReader.getLocalName()
														.equalsIgnoreCase(ElementNode.proteinEvidence)) {
													/*
													 * int
													 * proteinEvidenceClassAttributesCount
													 * = xmlStreamReader.
													 * getAttributeCount(); for
													 * (int i = 0; i <
													 * proteinEvidenceClassAttributesCount;
													 * i++) {
													 * System.out.println(
													 * "proteinEvidence: " +
													 * xmlStreamReader.
													 * getAttributeLocalName(i)
													 * + " " + xmlStreamReader.
													 * getAttributeValue(i)); }
													 */

													while (xmlStreamReader.hasNext()) {
														eventCode = xmlStreamReader.next();
														if (XMLStreamConstants.END_ELEMENT == eventCode
																&& xmlStreamReader.getLocalName().equalsIgnoreCase(
																		ElementNode.proteinEvidence)) {
															break;
														} else {
															if (XMLStreamConstants.START_ELEMENT == eventCode
																	&& xmlStreamReader.getLocalName()
																			.equalsIgnoreCase(ElementNode.evidence)) {
																Individual evidenceIndividual = ontoModel
																		.createIndividual(PROTEINATLAS.Evidence,
																				Evidence++);
																geneIndividual.addProperty(
																		ontoModel.getProperty(
																				PROTEINATLAS.hasProteinEvidence),
																		evidenceIndividual);
																int evidenceAttributesCount = xmlStreamReader
																		.getAttributeCount();
																for (int i = 0; i < evidenceAttributesCount; i++) {
																	String attribute = xmlStreamReader
																			.getAttributeLocalName(i);
																	String value = xmlStreamReader.getAttributeValue(i);
																	if (attribute
																			.equalsIgnoreCase(PROTEINATLAS.source)) {
																		evidenceIndividual
																				.addLiteral(
																						ontoModel.getProperty(
																								PROTEINATLAS.source),
																						value);
																	} else if (attribute.equalsIgnoreCase("evidence")) {
																		evidenceIndividual.addLiteral(ontoModel
																				.getProperty(PROTEINATLAS.name), value);
																	}
																}
															}
														}
													}
												} else if (xmlStreamReader.getLocalName()
														.equalsIgnoreCase(ElementNode.tissueExpression)) {
													Individual tissueExpressionIndividual = ontoModel.createIndividual(
															PROTEINATLAS.TissueExpression, TissueExpression++);
													geneIndividual.addProperty(
															ontoModel.getProperty(PROTEINATLAS.hasTissueExpression),
															tissueExpressionIndividual);
													int tissueExpressionClassAttributesCount = xmlStreamReader
															.getAttributeCount();
													for (int i = 0; i < tissueExpressionClassAttributesCount; i++) {
														String attribute = xmlStreamReader.getAttributeLocalName(i);
														String value = xmlStreamReader.getAttributeValue(i);
														if (attribute.equalsIgnoreCase(PROTEINATLAS.type)) {
															tissueExpressionIndividual.addLiteral(
																	ontoModel.getProperty(PROTEINATLAS.type), value);
														} else if (attribute
																.equalsIgnoreCase(PROTEINATLAS.technology)) {
															tissueExpressionIndividual.addLiteral(
																	ontoModel.getProperty(PROTEINATLAS.technology),
																	value);
														} else if (attribute.equalsIgnoreCase(PROTEINATLAS.assayType)) {
															tissueExpressionIndividual.addLiteral(
																	ontoModel.getProperty(PROTEINATLAS.assayType),
																	value);
														}
													}

													while (xmlStreamReader.hasNext()) {
														eventCode = xmlStreamReader.next();
														if (XMLStreamConstants.END_ELEMENT == eventCode
																&& xmlStreamReader.getLocalName().equalsIgnoreCase(
																		ElementNode.tissueExpression)) {
															break;
														} else {
															if (XMLStreamConstants.START_ELEMENT == eventCode) {
																if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.summary)) {
																	Individual summaryIndividual = ontoModel
																			.createIndividual(PROTEINATLAS.TE_Summary,
																					TE_Summary++);
																	tissueExpressionIndividual.addProperty(
																			ontoModel.getProperty(
																					PROTEINATLAS.hasSummary),
																			summaryIndividual);
																	int summaryAttributesCount = xmlStreamReader
																			.getAttributeCount();
																	for (int i = 0; i < summaryAttributesCount; i++) {
																		String attribute = xmlStreamReader
																				.getAttributeLocalName(i);
																		String value = xmlStreamReader
																				.getAttributeValue(i);
																		if (attribute
																				.equalsIgnoreCase(PROTEINATLAS.type)) {
																			summaryIndividual
																					.addLiteral(
																							ontoModel.getProperty(
																									PROTEINATLAS.type),
																							value);
																		}
																	}
																	String desc = xmlStreamReader.getElementText();
																	summaryIndividual
																			.addLiteral(
																					ontoModel.getProperty(
																							PROTEINATLAS.description),
																					desc);
																} else if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.verification)) {
																	Individual verificationIndividual = ontoModel
																			.createIndividual(
																					PROTEINATLAS.TE_Verification,
																					TE_Verification++);
																	tissueExpressionIndividual.addProperty(
																			ontoModel.getProperty(
																					PROTEINATLAS.hasVerification),
																			verificationIndividual);
																	int verificationAttributesCount = xmlStreamReader
																			.getAttributeCount();
																	for (int i = 0; i < verificationAttributesCount; i++) {
																		String attribute = xmlStreamReader
																				.getAttributeLocalName(i);
																		String value = xmlStreamReader
																				.getAttributeValue(i);
																		if (attribute
																				.equalsIgnoreCase(PROTEINATLAS.type)) {
																			verificationIndividual
																					.addLiteral(
																							ontoModel.getProperty(
																									PROTEINATLAS.type),
																							value);
																		} else if (attribute.equalsIgnoreCase(
																				PROTEINATLAS.description)) {
																			verificationIndividual.addLiteral(
																					ontoModel.getProperty(
																							PROTEINATLAS.description),
																					value);
																		}
																	}
																	String reliability = xmlStreamReader
																			.getElementText();
																	verificationIndividual.addLiteral(
																			ontoModel.getProperty(
																					PROTEINATLAS.reliability),
																			reliability);
																} else if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.image)) {
																	/*
																	 * int
																	 * imageAttributesCount
																	 * =
																	 * xmlStreamReader
																	 * .
																	 * getAttributeCount
																	 * (); for
																	 * (int i =
																	 * 0; i <
																	 * imageAttributesCount;
																	 * i++) {
																	 * System.
																	 * out.
																	 * println(
																	 * "image: "
																	 * +
																	 * xmlStreamReader
																	 * .
																	 * getAttributeLocalName
																	 * (i) + " "
																	 * +
																	 * xmlStreamReader
																	 * .
																	 * getAttributeValue
																	 * (i)); }
																	 */
																	Individual imageIndividual = ontoModel
																			.createIndividual(PROTEINATLAS.TE_Image,
																					TE_Image++);
																	tissueExpressionIndividual.addProperty(
																			ontoModel.getProperty(
																					PROTEINATLAS.hasVerification),
																			imageIndividual);
																	while (xmlStreamReader.hasNext()) {
																		eventCode = xmlStreamReader.next();
																		if (XMLStreamConstants.END_ELEMENT == eventCode
																				&& xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.image)) {
																			break;
																		} else {
																			if (XMLStreamConstants.START_ELEMENT == eventCode) {
																				if (xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.tissue)) {
																					String tissue = xmlStreamReader
																							.getElementText();
																					imageIndividual.addLiteral(
																							ontoModel.getProperty(
																									PROTEINATLAS.tissue),
																							tissue);
																				} else if (xmlStreamReader
																						.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.imageUrl)) {
																					String imageUrl = xmlStreamReader
																							.getElementText();
																					imageIndividual.addLiteral(
																							ontoModel.getProperty(
																									PROTEINATLAS.imageUrl),
																							imageUrl);
																				}
																			}
																		}
																	}
																} else if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.data)) {
																	Individual dataIndividual = ontoModel
																			.createIndividual(PROTEINATLAS.TE_Data,
																					TE_Data++);
																	tissueExpressionIndividual.addProperty(
																			ontoModel.getProperty(PROTEINATLAS.hasData),
																			dataIndividual);
																	while (xmlStreamReader.hasNext()) {
																		eventCode = xmlStreamReader.next();
																		if (XMLStreamConstants.END_ELEMENT == eventCode
																				&& xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.data)) {
																			break;
																		} else {
																			if (XMLStreamConstants.START_ELEMENT == eventCode) {
																				if (xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.tissue)) {
																					String tissue = xmlStreamReader
																							.getElementText();
																					dataIndividual.addLiteral(
																							ontoModel.getProperty(
																									PROTEINATLAS.tissue),
																							tissue);
																				} else if (xmlStreamReader
																						.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.level)) {
																					/*
																					 * int
																					 * levelAttributesCount
																					 * =
																					 * xmlStreamReader
																					 * .
																					 * getAttributeCount
																					 * (
																					 * )
																					 * ;
																					 * for
																					 * (int
																					 * i
																					 * =
																					 * 0;
																					 * i
																					 * <
																					 * levelAttributesCount;
																					 * i
																					 * +
																					 * +
																					 * )
																					 * {
																					 * System
																					 * .
																					 * out
																					 * .
																					 * println(
																					 * "level: "
																					 * +
																					 * xmlStreamReader
																					 * .
																					 * getAttributeLocalName
																					 * (
																					 * i)
																					 * +
																					 * " "
																					 * +
																					 * xmlStreamReader
																					 * .
																					 * getAttributeValue
																					 * (
																					 * i
																					 * )
																					 * )
																					 * ;
																					 * }
																					 */
																					String expressionLevel = xmlStreamReader
																							.getElementText();
																					dataIndividual.addLiteral(
																							ontoModel.getProperty(
																									PROTEINATLAS.expressionLevel),
																							expressionLevel);
																				} else if (xmlStreamReader
																						.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.tissueCell)) {
																					Individual tissueCellIndividual = ontoModel
																							.createIndividual(
																									PROTEINATLAS.TE_TissueCell,
																									TE_TissueCell++);
																					dataIndividual.addProperty(
																							ontoModel.getProperty(
																									PROTEINATLAS.hasTissueCell),
																							tissueCellIndividual);
																					while (xmlStreamReader.hasNext()) {
																						eventCode = xmlStreamReader
																								.next();
																						if (XMLStreamConstants.END_ELEMENT == eventCode
																								&& xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.tissueCell)) {
																							break;
																						} else {
																							if (XMLStreamConstants.START_ELEMENT == eventCode) {
																								if (xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.cellType)) {
																									String cellType = xmlStreamReader
																											.getElementText();
																									tissueCellIndividual
																											.addLiteral(
																													ontoModel
																															.getProperty(
																																	PROTEINATLAS.cellType),
																													cellType);
																								} else if (xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.level)) {
																									/*
																									 * int
																									 * levelAttributesCount
																									 * =
																									 * xmlStreamReader
																									 * .
																									 * getAttributeCount
																									 * (
																									 * )
																									 * ;
																									 * for
																									 * (int
																									 * i
																									 * =
																									 * 0;
																									 * i
																									 * <
																									 * levelAttributesCount;
																									 * i
																									 * +
																									 * +
																									 * )
																									 * {
																									 * System
																									 * .
																									 * out
																									 * .
																									 * println(
																									 * "level: "
																									 * +
																									 * xmlStreamReader
																									 * .
																									 * getAttributeLocalName
																									 * (
																									 * i)
																									 * +
																									 * " "
																									 * +
																									 * xmlStreamReader
																									 * .
																									 * getAttributeValue
																									 * (
																									 * i
																									 * )
																									 * )
																									 * ;
																									 * }
																									 */
																									String expressionLevel = xmlStreamReader
																											.getElementText();
																									tissueCellIndividual
																											.addLiteral(
																													ontoModel
																															.getProperty(
																																	PROTEINATLAS.expressionLevel),
																													expressionLevel);
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												} else if (xmlStreamReader.getLocalName()
														.equalsIgnoreCase(ElementNode.cellExpression)) {
													Individual cellExpressionIndividual = ontoModel.createIndividual(
															PROTEINATLAS.CellExpression, CellExpression++);
													geneIndividual.addProperty(
															ontoModel.getProperty(PROTEINATLAS.hasCellExpression),
															cellExpressionIndividual);
													int cellExpressionAttributesCount = xmlStreamReader
															.getAttributeCount();
													for (int i = 0; i < cellExpressionAttributesCount; i++) {
														String attribute = xmlStreamReader.getAttributeLocalName(i);
														String value = xmlStreamReader.getAttributeValue(i);
														if (attribute.equalsIgnoreCase(PROTEINATLAS.type)) {
															cellExpressionIndividual.addLiteral(
																	ontoModel.getProperty(PROTEINATLAS.type), value);
														} else if (attribute
																.equalsIgnoreCase(PROTEINATLAS.technology)) {
															cellExpressionIndividual.addLiteral(
																	ontoModel.getProperty(PROTEINATLAS.technology),
																	value);
														}
													}
													while (xmlStreamReader.hasNext()) {
														eventCode = xmlStreamReader.next();
														if (XMLStreamConstants.END_ELEMENT == eventCode
																&& xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.cellExpression)) {
															break;
														} else {
															if (XMLStreamConstants.START_ELEMENT == eventCode) {
																if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.summary)) {
																	Individual summaryIndividual = ontoModel
																			.createIndividual(PROTEINATLAS.CE_Summary,
																					CE_Summary++);
																	cellExpressionIndividual.addProperty(
																			ontoModel.getProperty(
																					PROTEINATLAS.hasSummary),
																			summaryIndividual);
																	String description = xmlStreamReader
																			.getElementText();
																	summaryIndividual.addLiteral(
																			ontoModel.getProperty(
																					PROTEINATLAS.description),
																			description);
																} else if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.verification)) {
																	/*
																	 * int
																	 * verificationAttributesCount
																	 * =
																	 * xmlStreamReader
																	 * .
																	 * getAttributeCount
																	 * (); for
																	 * (int i =
																	 * 0; i <
																	 * verificationAttributesCount;
																	 * i++) {
																	 * System.
																	 * out.
																	 * println(
																	 * "verification: "
																	 * +
																	 * xmlStreamReader
																	 * .
																	 * getAttributeLocalName
																	 * (i) + " "
																	 * +
																	 * xmlStreamReader
																	 * .
																	 * getAttributeValue
																	 * (i)); }
																	 */

																	Individual verificationIndividual = ontoModel
																			.createIndividual(
																					PROTEINATLAS.CE_Verification,
																					CE_Verification++);
																	cellExpressionIndividual.addProperty(
																			ontoModel.getProperty(
																					PROTEINATLAS.hasVerification),
																			verificationIndividual);
																	String reliability = xmlStreamReader
																			.getElementText();
																	verificationIndividual.addLiteral(
																			ontoModel.getProperty(
																					PROTEINATLAS.reliability),
																			reliability);
																} else if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.image)) {
																	/*
																	 * int
																	 * imageAttributesCount
																	 * =
																	 * xmlStreamReader
																	 * .
																	 * getAttributeCount
																	 * (); for
																	 * (int i =
																	 * 0; i <
																	 * imageAttributesCount;
																	 * i++) {
																	 * System.
																	 * out.
																	 * println(
																	 * "image: "
																	 * +
																	 * xmlStreamReader
																	 * .
																	 * getAttributeLocalName
																	 * (i) + " "
																	 * +
																	 * xmlStreamReader
																	 * .
																	 * getAttributeValue
																	 * (i)); }
																	 */
																	Individual imageIndividual = ontoModel
																			.createIndividual(PROTEINATLAS.CE_Image,
																					CE_Image++);
																	cellExpressionIndividual.addProperty(
																			ontoModel
																					.getProperty(PROTEINATLAS.hasImage),
																			imageIndividual);

																	while (xmlStreamReader.hasNext()) {
																		eventCode = xmlStreamReader.next();
																		if (XMLStreamConstants.END_ELEMENT == eventCode
																				&& xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.image)) {
																			break;
																		} else {
																			if (XMLStreamConstants.START_ELEMENT == eventCode
																					&& xmlStreamReader.getLocalName()
																							.equalsIgnoreCase(
																									ElementNode.imageUrl)) {
																				String imageUrl = xmlStreamReader
																						.getElementText();
																				imageIndividual.addLiteral(
																						ontoModel.getProperty(
																								PROTEINATLAS.imageUrl),
																						imageUrl);
																			}
																		}
																	}
																} else if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.data)) {
																	Individual dataIndividual = ontoModel
																			.createIndividual(PROTEINATLAS.CE_Data,
																					CE_Data++);
																	cellExpressionIndividual.addProperty(
																			ontoModel.getProperty(PROTEINATLAS.hasData),
																			dataIndividual);
																	while (xmlStreamReader.hasNext()) {
																		eventCode = xmlStreamReader.next();
																		if (XMLStreamConstants.END_ELEMENT == eventCode
																				&& xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.data)) {
																			break;
																		} else {
																			if (XMLStreamConstants.START_ELEMENT == eventCode
																					&& xmlStreamReader.getLocalName()
																							.equalsIgnoreCase(
																									ElementNode.location)) {
																				/*
																				 * int
																				 * locationAttributesCount
																				 * =
																				 * xmlStreamReader
																				 * .
																				 * getAttributeCount
																				 * (
																				 * )
																				 * ;
																				 * for
																				 * (int
																				 * i
																				 * =
																				 * 0;
																				 * i
																				 * <
																				 * locationAttributesCount;
																				 * i
																				 * +
																				 * +
																				 * )
																				 * {
																				 * System
																				 * .
																				 * out
																				 * .
																				 * println(
																				 * "location: "
																				 * +
																				 * xmlStreamReader
																				 * .
																				 * getAttributeLocalName
																				 * (
																				 * i)
																				 * +
																				 * " "
																				 * +
																				 * xmlStreamReader
																				 * .
																				 * getAttributeValue
																				 * (
																				 * i
																				 * )
																				 * )
																				 * ;
																				 * }
																				 */

																				String location = xmlStreamReader
																						.getElementText();
																				dataIndividual.addLiteral(
																						ontoModel.getProperty(
																								PROTEINATLAS.location),
																						location);
																			}
																		}
																	}
																}
															}
														}
													}
												} else if (xmlStreamReader.getLocalName()
														.equalsIgnoreCase(ElementNode.rnaExpression)) {
													Individual rnaExpressionIndividual = ontoModel.createIndividual(
															PROTEINATLAS.RnaExpression, RnaExpression++);
													geneIndividual.addProperty(
															ontoModel.getProperty(PROTEINATLAS.hasRnaExpression),
															rnaExpressionIndividual);
													int rnaExpressionAttributesCount = xmlStreamReader
															.getAttributeCount();
													for (int i = 0; i < rnaExpressionAttributesCount; i++) {
														String attribute = xmlStreamReader.getAttributeLocalName(i);
														String value = xmlStreamReader.getAttributeValue(i);
														if (attribute.equalsIgnoreCase(PROTEINATLAS.technology)) {
															rnaExpressionIndividual.addLiteral(
																	ontoModel.getProperty(PROTEINATLAS.technology),
																	value);
														}
													}

													while (xmlStreamReader.hasNext()) {
														eventCode = xmlStreamReader.next();
														if (XMLStreamConstants.END_ELEMENT == eventCode
																&& xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.rnaExpression)) {
															break;
														} else {
															if (XMLStreamConstants.START_ELEMENT == eventCode) {
																if (xmlStreamReader.getLocalName().equalsIgnoreCase(
																		ElementNode.rnaTissueCategory)) {
																	Individual rnaTissueCategoryIndividual = ontoModel
																			.createIndividual(
																					PROTEINATLAS.RnaTissueCategory,
																					RnaTissueCategory++);
																	rnaExpressionIndividual.addProperty(
																			ontoModel.getProperty(
																					PROTEINATLAS.hasRnaTissueCategory),
																			rnaTissueCategoryIndividual);

																	int rnaTissueCategoryAttributesCount = xmlStreamReader
																			.getAttributeCount();
																	for (int i = 0; i < rnaTissueCategoryAttributesCount; i++) {
																		String attribute = xmlStreamReader
																				.getAttributeLocalName(i);
																		String value = xmlStreamReader
																				.getAttributeValue(i);
																		if (attribute.equalsIgnoreCase(
																				PROTEINATLAS.description)) {
																			rnaTissueCategoryIndividual.addLiteral(
																					ontoModel.getProperty(
																							PROTEINATLAS.description),
																					value);
																		}
																	}
																	String name = xmlStreamReader.getElementText();
																	rnaTissueCategoryIndividual.addLiteral(
																			ontoModel.getProperty(PROTEINATLAS.name),
																			name);
																} else if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.data)) {
																	Individual dataIndividual = ontoModel
																			.createIndividual(PROTEINATLAS.RE_Data,
																					RE_Data++);
																	rnaExpressionIndividual.addProperty(
																			ontoModel.getProperty(PROTEINATLAS.hasData),
																			dataIndividual);
																	while (xmlStreamReader.hasNext()) {
																		eventCode = xmlStreamReader.next();
																		if (XMLStreamConstants.END_ELEMENT == eventCode
																				&& xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.data)) {
																			break;
																		} else {
																			if (XMLStreamConstants.START_ELEMENT == eventCode) {
																				if (xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.cellLine)) {
																					String cellLine = xmlStreamReader
																							.getElementText();
																					dataIndividual.addLiteral(
																							ontoModel.getProperty(
																									PROTEINATLAS.cellLine),
																							cellLine);
																				} else if (xmlStreamReader
																						.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.level)) {
																					int levelAttributesCount = xmlStreamReader
																							.getAttributeCount();
																					for (int i = 0; i < levelAttributesCount; i++) {
																						String attribute = xmlStreamReader
																								.getAttributeLocalName(
																										i);
																						String value = xmlStreamReader
																								.getAttributeValue(i);
																						if (attribute.equalsIgnoreCase(
																								PROTEINATLAS.type)) {
																							dataIndividual.addLiteral(
																									ontoModel
																											.getProperty(
																													PROTEINATLAS.type),
																									value);
																						} else if (attribute
																								.equalsIgnoreCase(
																										PROTEINATLAS.tpm)) {
																							dataIndividual.addLiteral(
																									ontoModel
																											.getProperty(
																													PROTEINATLAS.tpm),
																									Float.parseFloat(
																											value));
																						}
																					}
																					String level = xmlStreamReader
																							.getElementText();
																					dataIndividual.addLiteral(
																							ontoModel.getProperty(
																									PROTEINATLAS.level),
																							level);
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												} else if (xmlStreamReader.getLocalName()
														.equalsIgnoreCase(ElementNode.antibody)) {
													Individual antibodyIndividual = ontoModel
															.createIndividual(PROTEINATLAS.Antibody, Antibody++);
													geneIndividual.addProperty(
															ontoModel.getProperty(PROTEINATLAS.hasAntibody),
															antibodyIndividual);
													int antibodyAttributesCount = xmlStreamReader.getAttributeCount();
													for (int i = 0; i < antibodyAttributesCount; i++) {
														String attribute = xmlStreamReader.getAttributeLocalName(i);
														String value = xmlStreamReader.getAttributeValue(i);
														if (attribute.equalsIgnoreCase(PROTEINATLAS.ID)) {
															antibodyIndividual.addLiteral(
																	ontoModel.getProperty(PROTEINATLAS.ID), value);
														}
													}

													while (xmlStreamReader.hasNext()) {
														eventCode = xmlStreamReader.next();
														if (XMLStreamConstants.END_ELEMENT == eventCode
																&& xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.antibody)) {
															break;
														} else {
															if (XMLStreamConstants.START_ELEMENT == eventCode) {
																if (xmlStreamReader.getLocalName().equalsIgnoreCase(
																		ElementNode.antigenSequence)) {
																	String antigenSequence = xmlStreamReader
																			.getElementText();
																	antibodyIndividual.addLiteral(
																			ontoModel.getProperty(
																					PROTEINATLAS.antigenSequence),
																			antigenSequence);
																} else if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(
																				ElementNode.antibodyTargetWeights)) {
																	while (xmlStreamReader.hasNext()) {
																		eventCode = xmlStreamReader.next();
																		if (XMLStreamConstants.END_ELEMENT == eventCode
																				&& xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.antibodyTargetWeights)) {
																			break;
																		} else {
																			if (XMLStreamConstants.START_ELEMENT == eventCode
																					&& xmlStreamReader.getLocalName()
																							.equalsIgnoreCase(
																									ElementNode.weight)) {
																				Individual weightIndividual = ontoModel
																						.createIndividual(
																								PROTEINATLAS.AB_TargetWeights,
																								AB_TargetWeights++);
																				antibodyIndividual.addProperty(
																						ontoModel.getProperty(
																								PROTEINATLAS.hasWeight),
																						weightIndividual);
																				int weightAttributesCount = xmlStreamReader
																						.getAttributeCount();
																				for (int i = 0; i < weightAttributesCount; i++) {
																					String attribute = xmlStreamReader
																							.getAttributeLocalName(i);
																					String value = xmlStreamReader
																							.getAttributeValue(i);
																					if (attribute.equalsIgnoreCase(
																							PROTEINATLAS.value)) {
																						weightIndividual.addLiteral(
																								ontoModel.getProperty(
																										PROTEINATLAS.value),
																								value);
																					} else if (attribute
																							.equalsIgnoreCase(
																									PROTEINATLAS.unit)) {
																						weightIndividual.addLiteral(
																								ontoModel.getProperty(
																										PROTEINATLAS.unit),
																								value);
																					}
																				}
																			}
																		}
																	}
																} else if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(
																				ElementNode.tissueExpression)) {
																	Individual tissueExpressionIndividual = ontoModel
																			.createIndividual(
																					PROTEINATLAS.AB_TissueExpression,
																					AB_TissueExpression++);
																	antibodyIndividual.addProperty(
																			ontoModel.getProperty(
																					PROTEINATLAS.hasTissueExpression),
																			tissueExpressionIndividual);
																	int tissueExpressionAttributesCount = xmlStreamReader
																			.getAttributeCount();
																	for (int i = 0; i < tissueExpressionAttributesCount; i++) {
																		String attribute = xmlStreamReader
																				.getAttributeLocalName(i);
																		String value = xmlStreamReader
																				.getAttributeValue(i);
																		if (attribute.equalsIgnoreCase(
																				PROTEINATLAS.technology)) {
																			tissueExpressionIndividual.addLiteral(
																					ontoModel.getProperty(
																							PROTEINATLAS.technology),
																					value);
																		} else if (attribute.equalsIgnoreCase(
																				PROTEINATLAS.assayType)) {
																			tissueExpressionIndividual.addLiteral(
																					ontoModel.getProperty(
																							PROTEINATLAS.assayType),
																					value);
																		}
																	}
																	while (xmlStreamReader.hasNext()) {
																		eventCode = xmlStreamReader.next();
																		if (XMLStreamConstants.END_ELEMENT == eventCode
																				&& xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.tissueExpression)) {
																			break;
																		} else {
																			if (XMLStreamConstants.START_ELEMENT == eventCode) {
																				if (xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.summary)) {
																					Individual summaryIndividual = ontoModel
																							.createIndividual(
																									PROTEINATLAS.ATE_Summary,
																									ATE_Summary++);
																					tissueExpressionIndividual
																							.addProperty(
																									ontoModel
																											.getProperty(
																													PROTEINATLAS.hasSummary),
																									summaryIndividual);
																					int summaryAttributesCount = xmlStreamReader
																							.getAttributeCount();
																					for (int i = 0; i < summaryAttributesCount; i++) {
																						String attribute = xmlStreamReader
																								.getAttributeLocalName(
																										i);
																						String value = xmlStreamReader
																								.getAttributeValue(i);
																						if (attribute.equalsIgnoreCase(
																								PROTEINATLAS.type)) {
																							summaryIndividual
																									.addLiteral(
																											ontoModel
																													.getProperty(
																															PROTEINATLAS.type),
																											value);
																						}
																					}
																					String description = xmlStreamReader
																							.getElementText();
																					summaryIndividual.addLiteral(
																							ontoModel.getProperty(
																									PROTEINATLAS.description),
																							description);
																				} else if (xmlStreamReader
																						.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.validation)) {
																					Individual validationIndividual = ontoModel
																							.createIndividual(
																									PROTEINATLAS.ATE_Validation,
																									ATE_Validation++);
																					tissueExpressionIndividual
																							.addProperty(
																									ontoModel
																											.getProperty(
																													PROTEINATLAS.hasValidation),
																									validationIndividual);
																					int validationAttributesCount = xmlStreamReader
																							.getAttributeCount();
																					for (int i = 0; i < validationAttributesCount; i++) {
																						String attribute = xmlStreamReader
																								.getAttributeLocalName(
																										i);
																						String value = xmlStreamReader
																								.getAttributeValue(i);
																						if (attribute.equalsIgnoreCase(
																								PROTEINATLAS.type)) {
																							validationIndividual
																									.addLiteral(
																											ontoModel
																													.getProperty(
																															PROTEINATLAS.type),
																											value);
																						}
																					}
																					String message = xmlStreamReader
																							.getElementText();
																					validationIndividual.addLiteral(
																							ontoModel.getProperty(
																									PROTEINATLAS.message),
																							message);
																				} else if (xmlStreamReader
																						.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.image)) {
																					Individual imageIndividual = ontoModel
																							.createIndividual(
																									PROTEINATLAS.ATE_Image,
																									ATE_Image++);
																					tissueExpressionIndividual
																							.addProperty(
																									ontoModel
																											.getProperty(
																													PROTEINATLAS.hasImage),
																									imageIndividual);
																					int imageAttributesCount = xmlStreamReader
																							.getAttributeCount();
																					for (int i = 0; i < imageAttributesCount; i++) {
																						String attribute = xmlStreamReader
																								.getAttributeLocalName(
																										i);
																						String value = xmlStreamReader
																								.getAttributeValue(i);
																						if (attribute.equalsIgnoreCase(
																								PROTEINATLAS.description)) {
																							imageIndividual.addLiteral(
																									ontoModel
																											.getProperty(
																													PROTEINATLAS.description),
																									value);
																						}
																					}
																					while (xmlStreamReader.hasNext()) {
																						eventCode = xmlStreamReader
																								.next();
																						if (XMLStreamConstants.END_ELEMENT == eventCode
																								&& xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.image)) {
																							break;
																						} else {
																							if (XMLStreamConstants.START_ELEMENT == eventCode
																									&& xmlStreamReader
																											.getLocalName()
																											.equalsIgnoreCase(
																													ElementNode.imageUrl)) {
																								String imageUrl = xmlStreamReader
																										.getElementText();
																								imageIndividual
																										.addLiteral(
																												ontoModel
																														.getProperty(
																																PROTEINATLAS.imageUrl),
																												imageUrl);
																							}
																						}
																					}
																				} else if (xmlStreamReader
																						.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.data)) {
																					Individual dataIndividual = ontoModel
																							.createIndividual(
																									PROTEINATLAS.ATE_Data,
																									ATE_Data++);
																					tissueExpressionIndividual
																							.addProperty(
																									ontoModel
																											.getProperty(
																													PROTEINATLAS.hasData),
																									dataIndividual);
																					while (xmlStreamReader.hasNext()) {
																						eventCode = xmlStreamReader
																								.next();
																						if (XMLStreamConstants.END_ELEMENT == eventCode
																								&& xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.data)) {
																							break;
																						} else {
																							if (XMLStreamConstants.START_ELEMENT == eventCode) {
																								if (xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.tissue)) {
																									String tissue = xmlStreamReader
																											.getElementText();
																									dataIndividual
																											.addLiteral(
																													ontoModel
																															.getProperty(
																																	PROTEINATLAS.tissue),
																													tissue);
																								} else if (xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.tissueCell)) {
																									Individual tissueCellIndividual = ontoModel
																											.createIndividual(
																													PROTEINATLAS.ATE_TissueCell,
																													ATE_TissueCell++);
																									dataIndividual
																											.addProperty(
																													ontoModel
																															.getProperty(
																																	PROTEINATLAS.hasTissueCell),
																													tissueCellIndividual);
																									while (xmlStreamReader
																											.hasNext()) {
																										eventCode = xmlStreamReader
																												.next();
																										if (XMLStreamConstants.END_ELEMENT == eventCode
																												&& xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.tissueCell)) {
																											break;
																										} else {
																											if (XMLStreamConstants.START_ELEMENT == eventCode) {
																												if (xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.cellType)) {
																													String cellType = xmlStreamReader
																															.getElementText();
																													tissueCellIndividual
																															.addLiteral(
																																	ontoModel
																																			.getProperty(
																																					PROTEINATLAS.cellType),
																																	cellType);
																												} else if (xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.level)) {
																													int levelAttributesCount = xmlStreamReader
																															.getAttributeCount();
																													if (levelAttributesCount > 1)
																														levelAttributesCount = 1;
																													for (int i = 0; i < levelAttributesCount; i++) {
																														String attribute = xmlStreamReader
																																.getAttributeLocalName(
																																		i);
																														String value = xmlStreamReader
																																.getAttributeValue(
																																		i);
																														if (attribute
																																.equalsIgnoreCase(
																																		PROTEINATLAS.type)) {
																															if (value
																																	.equalsIgnoreCase(
																																			"staining")) {
																																String staining = xmlStreamReader
																																		.getElementText();
																																tissueCellIndividual
																																		.addLiteral(
																																				ontoModel
																																						.getProperty(
																																								PROTEINATLAS.stainingLevel),
																																				staining);
																															} else if (value
																																	.equalsIgnoreCase(
																																			"intensity")) {
																																String intensity = xmlStreamReader
																																		.getElementText();
																																tissueCellIndividual
																																		.addLiteral(
																																				ontoModel
																																						.getProperty(
																																								PROTEINATLAS.intensityLevel),
																																				intensity);
																															}
																														}
																													}
																												} else if (xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.quantity)) {
																													String quantity = xmlStreamReader
																															.getElementText();
																													tissueCellIndividual
																															.addLiteral(
																																	ontoModel
																																			.getProperty(
																																					PROTEINATLAS.quantity),
																																	quantity);
																												} else if (xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.location)) {
																													String location = xmlStreamReader
																															.getElementText();
																													tissueCellIndividual
																															.addLiteral(
																																	ontoModel
																																			.getProperty(
																																					PROTEINATLAS.location),
																																	location);
																												}
																											}
																										}
																									}
																								} else if (xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.patient)) {
																									Individual patientIndividual = ontoModel
																											.createIndividual(
																													PROTEINATLAS.ATE_Patient,
																													ATE_Patient++);
																									dataIndividual
																											.addProperty(
																													ontoModel
																															.getProperty(
																																	PROTEINATLAS.hasPatient),
																													patientIndividual);
																									while (xmlStreamReader
																											.hasNext()) {
																										eventCode = xmlStreamReader
																												.next();
																										if (XMLStreamConstants.END_ELEMENT == eventCode
																												&& xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.patient)) {
																											break;
																										} else {
																											if (XMLStreamConstants.START_ELEMENT == eventCode) {
																												if (xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.sex)) {
																													String sex = xmlStreamReader
																															.getElementText();
																													patientIndividual
																															.addLiteral(
																																	ontoModel
																																			.getProperty(
																																					PROTEINATLAS.sex),
																																	sex);
																												} else if (xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.age)) {
																													String age = xmlStreamReader
																															.getElementText();
																													patientIndividual
																															.addLiteral(
																																	ontoModel
																																			.getProperty(
																																					PROTEINATLAS.age),
																																	Integer.parseInt(
																																			age));
																												} else if (xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.patientId)) {
																													String patientId = xmlStreamReader
																															.getElementText();
																													patientIndividual
																															.addLiteral(
																																	ontoModel
																																			.getProperty(
																																					PROTEINATLAS.patientId),
																																	patientId);
																												} else if (xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.sample)) {
																													/*
																													 * while
																													 * (
																													 * xmlStreamReader
																													 * .
																													 * hasNext
																													 * (
																													 * )
																													 * )
																													 * {
																													 * eventCode
																													 * =
																													 * xmlStreamReader
																													 * .
																													 * next
																													 * (
																													 * )
																													 * ;
																													 * if
																													 * (XMLStreamConstants
																													 * .
																													 * END_ELEMENT
																													 * ==
																													 * eventCode
																													 * &&
																													 * xmlStreamReader
																													 * .
																													 * getLocalName
																													 * (
																													 * )
																													 * .
																													 * equalsIgnoreCase
																													 * (
																													 * ElementNode
																													 * .
																													 * sample
																													 * )
																													 * )
																													 * {
																													 * break;
																													 * }
																													 * else
																													 * {
																													 * if
																													 * (XMLStreamConstants
																													 * .
																													 * START_ELEMENT
																													 * ==
																													 * eventCode)
																													 * {
																													 * if
																													 * (xmlStreamReader
																													 * .
																													 * getLocalName
																													 * (
																													 * )
																													 * .
																													 * equalsIgnoreCase
																													 * (
																													 * ElementNode
																													 * .
																													 * snomedParameters
																													 * )
																													 * )
																													 * {
																													 * while
																													 * (
																													 * xmlStreamReader
																													 * .
																													 * hasNext
																													 * (
																													 * )
																													 * )
																													 * {
																													 * eventCode
																													 * =
																													 * xmlStreamReader
																													 * .
																													 * next
																													 * (
																													 * )
																													 * ;
																													 * if
																													 * (XMLStreamConstants
																													 * .
																													 * END_ELEMENT
																													 * ==
																													 * eventCode
																													 * &&
																													 * xmlStreamReader
																													 * .
																													 * getLocalName
																													 * (
																													 * )
																													 * .
																													 * equalsIgnoreCase
																													 * (
																													 * ElementNode
																													 * .
																													 * snomedParameters
																													 * )
																													 * )
																													 * {
																													 * break;
																													 * }
																													 * else
																													 * {
																													 * if
																													 * (XMLStreamConstants
																													 * .
																													 * START_ELEMENT
																													 * ==
																													 * eventCode
																													 * &&
																													 * xmlStreamReader
																													 * .
																													 * getLocalName
																													 * (
																													 * )
																													 * .
																													 * equalsIgnoreCase
																													 * (
																													 * ElementNode
																													 * .
																													 * snomed
																													 * )
																													 * )
																													 * {
																													 * int
																													 * snomedAttributesCount
																													 * =
																													 * xmlStreamReader
																													 * .
																													 * getAttributeCount
																													 * (
																													 * )
																													 * ;
																													 * for
																													 * (int
																													 * i
																													 * =
																													 * 0;
																													 * i
																													 * <
																													 * snomedAttributesCount;
																													 * i
																													 * +
																													 * +
																													 * )
																													 * {
																													 * System
																													 * .
																													 * out
																													 * .
																													 * println(
																													 * "snomed: "
																													 * +
																													 * xmlStreamReader
																													 * .
																													 * getAttributeLocalName
																													 * (
																													 * i)
																													 * +
																													 * " "
																													 * +
																													 * xmlStreamReader
																													 * .
																													 * getAttributeValue
																													 * (
																													 * i
																													 * )
																													 * )
																													 * ;
																													 * }
																													 * }
																													 * }
																													 * }
																													 * }
																													 * else
																													 * if
																													 * (xmlStreamReader
																													 * .
																													 * getLocalName
																													 * (
																													 * )
																													 * .
																													 * equalsIgnoreCase
																													 * (
																													 * ElementNode
																													 * .
																													 * assayImage
																													 * )
																													 * )
																													 * {
																													 * while
																													 * (
																													 * xmlStreamReader
																													 * .
																													 * hasNext
																													 * (
																													 * )
																													 * )
																													 * {
																													 * eventCode
																													 * =
																													 * xmlStreamReader
																													 * .
																													 * next
																													 * (
																													 * )
																													 * ;
																													 * if
																													 * (XMLStreamConstants
																													 * .
																													 * END_ELEMENT
																													 * ==
																													 * eventCode
																													 * &&
																													 * xmlStreamReader
																													 * .
																													 * getLocalName
																													 * (
																													 * )
																													 * .
																													 * equalsIgnoreCase
																													 * (
																													 * ElementNode
																													 * .
																													 * assayImage
																													 * )
																													 * )
																													 * {
																													 * break;
																													 * }
																													 * else
																													 * {
																													 * if
																													 * (XMLStreamConstants
																													 * .
																													 * START_ELEMENT
																													 * ==
																													 * eventCode
																													 * &&
																													 * xmlStreamReader
																													 * .
																													 * getLocalName
																													 * (
																													 * )
																													 * .
																													 * equalsIgnoreCase
																													 * (
																													 * ElementNode
																													 * .
																													 * image
																													 * )
																													 * )
																													 * {
																													 * int
																													 * imageAttributesCount
																													 * =
																													 * xmlStreamReader
																													 * .
																													 * getAttributeCount
																													 * (
																													 * )
																													 * ;
																													 * for
																													 * (int
																													 * i
																													 * =
																													 * 0;
																													 * i
																													 * <
																													 * imageAttributesCount;
																													 * i
																													 * +
																													 * +
																													 * )
																													 * {
																													 * System
																													 * .
																													 * out
																													 * .
																													 * println(
																													 * "image: "
																													 * +
																													 * xmlStreamReader
																													 * .
																													 * getAttributeLocalName
																													 * (
																													 * i)
																													 * +
																													 * " "
																													 * +
																													 * xmlStreamReader
																													 * .
																													 * getAttributeValue
																													 * (
																													 * i
																													 * )
																													 * )
																													 * ;
																													 * }
																													 * while
																													 * (
																													 * xmlStreamReader
																													 * .
																													 * hasNext
																													 * (
																													 * )
																													 * )
																													 * {
																													 * eventCode
																													 * =
																													 * xmlStreamReader
																													 * .
																													 * next
																													 * (
																													 * )
																													 * ;
																													 * if
																													 * (XMLStreamConstants
																													 * .
																													 * END_ELEMENT
																													 * ==
																													 * eventCode
																													 * &&
																													 * xmlStreamReader
																													 * .
																													 * getLocalName
																													 * (
																													 * )
																													 * .
																													 * equalsIgnoreCase
																													 * (
																													 * ElementNode
																													 * .
																													 * image
																													 * )
																													 * )
																													 * {
																													 * break;
																													 * }
																													 * else
																													 * {
																													 * if
																													 * (XMLStreamConstants
																													 * .
																													 * START_ELEMENT
																													 * ==
																													 * eventCode
																													 * &&
																													 * xmlStreamReader
																													 * .
																													 * getLocalName
																													 * (
																													 * )
																													 * .
																													 * equalsIgnoreCase
																													 * (
																													 * ElementNode
																													 * .
																													 * imageUrl
																													 * )
																													 * )
																													 * {
																													 * System
																													 * .
																													 * out
																													 * .
																													 * println(
																													 * "imageUrl: "
																													 * +
																													 * xmlStreamReader
																													 * .
																													 * getElementText
																													 * (
																													 * )
																													 * )
																													 * ;
																													 * }
																													 * }
																													 * }
																													 * }
																													 * }
																													 * }
																													 * }
																													 * }
																													 * }
																													 * }
																													 */
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																} else if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.cellExpression)) {
																	Individual cellExpressionIndividual = ontoModel
																			.createIndividual(
																					PROTEINATLAS.AB_CellExpression,
																					AB_CellExpression++);
																	antibodyIndividual.addProperty(
																			ontoModel.getProperty(
																					PROTEINATLAS.hasCellExpression),
																			cellExpressionIndividual);
																	int cellExpressionAttributesCount = xmlStreamReader
																			.getAttributeCount();
																	for (int i = 0; i < cellExpressionAttributesCount; i++) {
																		String attribute = xmlStreamReader
																				.getAttributeLocalName(i);
																		String value = xmlStreamReader
																				.getAttributeValue(i);
																		if (attribute.equalsIgnoreCase(
																				PROTEINATLAS.technology)) {
																			cellExpressionIndividual.addLiteral(
																					ontoModel.getProperty(
																							PROTEINATLAS.technology),
																					value);
																		}
																	}
																	while (xmlStreamReader.hasNext()) {
																		eventCode = xmlStreamReader.next();
																		if (XMLStreamConstants.END_ELEMENT == eventCode
																				&& xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.cellExpression)) {
																			break;
																		} else {
																			if (XMLStreamConstants.START_ELEMENT == eventCode) {
																				if (xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.subAssay)) {
																					Individual subAssayIndividual = ontoModel
																							.createIndividual(
																									PROTEINATLAS.ACE_SubAssay,
																									ACE_SubAssay++);
																					cellExpressionIndividual
																							.addProperty(
																									ontoModel
																											.getProperty(
																													PROTEINATLAS.hasSubAssay),
																									subAssayIndividual);
																					/*
																					 * int
																					 * subAssayExpressionAttributesCount
																					 * =
																					 * xmlStreamReader
																					 * .
																					 * getAttributeCount
																					 * (
																					 * )
																					 * ;
																					 * for
																					 * (int
																					 * i
																					 * =
																					 * 0;
																					 * i
																					 * <
																					 * subAssayExpressionAttributesCount;
																					 * i
																					 * +
																					 * +
																					 * )
																					 * {
																					 * System
																					 * .
																					 * out
																					 * .
																					 * println(
																					 * "subAssay: "
																					 * +
																					 * xmlStreamReader
																					 * .
																					 * getAttributeLocalName
																					 * (
																					 * i)
																					 * +
																					 * " "
																					 * +
																					 * xmlStreamReader
																					 * .
																					 * getAttributeValue
																					 * (
																					 * i
																					 * )
																					 * )
																					 * ;
																					 * }
																					 */
																					while (xmlStreamReader.hasNext()) {
																						eventCode = xmlStreamReader
																								.next();
																						if (XMLStreamConstants.END_ELEMENT == eventCode
																								&& xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.subAssay)) {
																							break;
																						} else {
																							if (XMLStreamConstants.START_ELEMENT == eventCode) {
																								if (xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.verification)) {
																									int verificationExpressionAttributesCount = xmlStreamReader
																											.getAttributeCount();
																									for (int i = 0; i < verificationExpressionAttributesCount; i++) {
																										String attribute = xmlStreamReader
																												.getAttributeLocalName(
																														i);
																										String value = xmlStreamReader
																												.getAttributeValue(
																														i);
																										if (attribute
																												.equalsIgnoreCase(
																														PROTEINATLAS.type)) {
																											subAssayIndividual
																													.addLiteral(
																															ontoModel
																																	.getProperty(
																																			PROTEINATLAS.type),
																															value);
																										}
																									}
																									String validationType = xmlStreamReader
																											.getElementText();
																									subAssayIndividual
																											.addLiteral(
																													ontoModel
																															.getProperty(
																																	PROTEINATLAS.validationType),
																													validationType);
																								} else if (xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.image)) {
																									Individual imageIndividual = ontoModel
																											.createIndividual(
																													PROTEINATLAS.ACE_Image,
																													ACE_Image++);
																									subAssayIndividual
																											.addProperty(
																													ontoModel
																															.getProperty(
																																	PROTEINATLAS.hasImage),
																													imageIndividual);
																									int imageAttributesCount = xmlStreamReader
																											.getAttributeCount();
																									for (int i = 0; i < imageAttributesCount; i++) {
																										String attribute = xmlStreamReader
																												.getAttributeLocalName(
																														i);
																										String value = xmlStreamReader
																												.getAttributeValue(
																														i);
																										if (attribute
																												.equalsIgnoreCase(
																														PROTEINATLAS.description)) {
																											imageIndividual
																													.addLiteral(
																															ontoModel
																																	.getProperty(
																																			PROTEINATLAS.description),
																															value);
																										}
																									}
																									while (xmlStreamReader
																											.hasNext()) {
																										eventCode = xmlStreamReader
																												.next();
																										if (XMLStreamConstants.END_ELEMENT == eventCode
																												&& xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.image)) {
																											break;
																										} else {
																											if (XMLStreamConstants.START_ELEMENT == eventCode
																													&& xmlStreamReader
																															.getLocalName()
																															.equalsIgnoreCase(
																																	ElementNode.imageUrl)) {
																												String imageUrl = xmlStreamReader
																														.getElementText();
																												imageIndividual
																														.addLiteral(
																																ontoModel
																																		.getProperty(
																																				PROTEINATLAS.imageUrl),
																																imageUrl);
																											}
																										}
																									}
																								} else if (xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.data)) {
																									Individual dataIndividual = ontoModel
																											.createIndividual(
																													PROTEINATLAS.ACE_Data,
																													ACE_Data++);
																									subAssayIndividual
																											.addProperty(
																													ontoModel
																															.getProperty(
																																	PROTEINATLAS.hasData),
																													dataIndividual);
																									while (xmlStreamReader
																											.hasNext()) {
																										eventCode = xmlStreamReader
																												.next();
																										if (XMLStreamConstants.END_ELEMENT == eventCode
																												&& xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.data)) {
																											break;
																										} else {
																											if (XMLStreamConstants.START_ELEMENT == eventCode) {
																												if (xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.cellLine)) {
																													String cellLine = xmlStreamReader
																															.getElementText();
																													dataIndividual
																															.addLiteral(
																																	ontoModel
																																			.getProperty(
																																					PROTEINATLAS.cellLine),
																																	cellLine);
																												} else if (xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.location)) {
																													/*
																													 * int
																													 * locationAttributesCount
																													 * =
																													 * xmlStreamReader
																													 * .
																													 * getAttributeCount
																													 * (
																													 * )
																													 * ;
																													 * for
																													 * (int
																													 * i
																													 * =
																													 * 0;
																													 * i
																													 * <
																													 * locationAttributesCount;
																													 * i
																													 * +
																													 * +
																													 * )
																													 * {
																													 * System
																													 * .
																													 * out
																													 * .
																													 * println(
																													 * "location: "
																													 * +
																													 * xmlStreamReader
																													 * .
																													 * getAttributeLocalName
																													 * (
																													 * i)
																													 * +
																													 * " "
																													 * +
																													 * xmlStreamReader
																													 * .
																													 * getAttributeValue
																													 * (
																													 * i
																													 * )
																													 * )
																													 * ;
																													 * }
																													 */
																													String location = xmlStreamReader
																															.getElementText();
																													dataIndividual
																															.addLiteral(
																																	ontoModel
																																			.getProperty(
																																					PROTEINATLAS.location),
																																	location);
																												} else if (xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.assayImage)) {
																													while (xmlStreamReader
																															.hasNext()) {
																														eventCode = xmlStreamReader
																																.next();
																														if (XMLStreamConstants.END_ELEMENT == eventCode
																																&& xmlStreamReader
																																		.getLocalName()
																																		.equalsIgnoreCase(
																																				ElementNode.assayImage)) {
																															break;
																														} else {
																															if (XMLStreamConstants.START_ELEMENT == eventCode
																																	&& xmlStreamReader
																																			.getLocalName()
																																			.equalsIgnoreCase(
																																					ElementNode.image)) {
																																Individual assayImageIndividual = ontoModel
																																		.createIndividual(
																																				PROTEINATLAS.ACE_AssayImage,
																																				ACE_AssayImage++);
																																dataIndividual
																																		.addProperty(
																																				ontoModel
																																						.getProperty(
																																								PROTEINATLAS.hasAssayImage),
																																				assayImageIndividual);
																																while (xmlStreamReader
																																		.hasNext()) {
																																	eventCode = xmlStreamReader
																																			.next();
																																	if (XMLStreamConstants.END_ELEMENT == eventCode
																																			&& xmlStreamReader
																																					.getLocalName()
																																					.equalsIgnoreCase(
																																							ElementNode.image)) {
																																		break;
																																	} else {
																																		if (XMLStreamConstants.START_ELEMENT == eventCode) {
																																			if (xmlStreamReader
																																					.getLocalName()
																																					.equalsIgnoreCase(
																																							ElementNode.channel)) {
																																				String channel = xmlStreamReader
																																						.getElementText();
																																				assayImageIndividual
																																						.addLiteral(
																																								ontoModel
																																										.getProperty(
																																												PROTEINATLAS.channel),
																																								channel);
																																			} else if (xmlStreamReader
																																					.getLocalName()
																																					.equalsIgnoreCase(
																																							ElementNode.imageUrl)) {
																																				String imageUrl = xmlStreamReader
																																						.getElementText();
																																				assayImageIndividual
																																						.addLiteral(
																																								ontoModel
																																										.getProperty(
																																												PROTEINATLAS.imageUrl),
																																								imageUrl);
																																			}
																																		}
																																	}
																																}
																															}
																														}
																													}
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																} else if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.westernBlot)) {
																	Individual westernBlotIndividual = ontoModel
																			.createIndividual(
																					PROTEINATLAS.AB_WesternBlot,
																					AB_WesternBlot++);
																	antibodyIndividual.addProperty(
																			ontoModel.getProperty(
																					PROTEINATLAS.hasWesternBlot),
																			westernBlotIndividual);
																	int westernBlotAttributesCount = xmlStreamReader
																			.getAttributeCount();
																	for (int i = 0; i < westernBlotAttributesCount; i++) {
																		String attribute = xmlStreamReader
																				.getAttributeLocalName(i);
																		String value = xmlStreamReader
																				.getAttributeValue(i);
																		if (attribute.equalsIgnoreCase(
																				PROTEINATLAS.technology)) {
																			westernBlotIndividual.addLiteral(
																					ontoModel.getProperty(
																							PROTEINATLAS.technology),
																					value);
																		}
																	}

																	while (xmlStreamReader.hasNext()) {
																		eventCode = xmlStreamReader.next();
																		if (XMLStreamConstants.END_ELEMENT == eventCode
																				&& xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.westernBlot)) {
																			break;
																		} else {
																			if (XMLStreamConstants.START_ELEMENT == eventCode) {
																				if (xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.verification)) {
																					Individual verificationIndividual = ontoModel
																							.createIndividual(
																									PROTEINATLAS.AWB_Verification,
																									AWB_Verification++);
																					westernBlotIndividual.addProperty(
																							ontoModel.getProperty(
																									PROTEINATLAS.hasVerification),
																							verificationIndividual);
																					int verificationAttributesCount = xmlStreamReader
																							.getAttributeCount();
																					for (int i = 0; i < verificationAttributesCount; i++) {
																						String attribute = xmlStreamReader
																								.getAttributeLocalName(
																										i);
																						String value = xmlStreamReader
																								.getAttributeValue(i);
																						if (attribute.equalsIgnoreCase(
																								PROTEINATLAS.description)) {
																							verificationIndividual
																									.addLiteral(
																											ontoModel
																													.getProperty(
																															PROTEINATLAS.description),
																											value);
																						}
																					}
																					String validation = xmlStreamReader
																							.getElementText();
																					verificationIndividual.addLiteral(
																							ontoModel.getProperty(
																									PROTEINATLAS.validation),
																							validation);
																				} /*
																					 * else
																					 * if
																					 * (xmlStreamReader
																					 * .
																					 * getLocalName
																					 * (
																					 * )
																					 * .
																					 * equalsIgnoreCase
																					 * (
																					 * ElementNode
																					 * .
																					 * antibodyDilution
																					 * )
																					 * )
																					 * {
																					 * int
																					 * antibodyDilutionAttributesCount
																					 * =
																					 * xmlStreamReader
																					 * .
																					 * getAttributeCount
																					 * (
																					 * )
																					 * ;
																					 * for
																					 * (int
																					 * i
																					 * =
																					 * 0;
																					 * i
																					 * <
																					 * antibodyDilutionAttributesCount;
																					 * i
																					 * +
																					 * +
																					 * )
																					 * {
																					 * System
																					 * .
																					 * out
																					 * .
																					 * println(
																					 * "antibodyDilution: "
																					 * +
																					 * xmlStreamReader
																					 * .
																					 * getAttributeLocalName
																					 * (
																					 * i)
																					 * +
																					 * " "
																					 * +
																					 * xmlStreamReader
																					 * .
																					 * getAttributeValue
																					 * (
																					 * i
																					 * )
																					 * )
																					 * ;
																					 * }
																					 * }
																					 */ else if (xmlStreamReader
																						.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.image)) {
																					Individual imageIndividual = ontoModel
																							.createIndividual(
																									PROTEINATLAS.AWB_Image,
																									AWB_Image++);
																					westernBlotIndividual.addProperty(
																							ontoModel.getProperty(
																									PROTEINATLAS.hasImage),
																							imageIndividual);
																					int imageAttributesCount = xmlStreamReader
																							.getAttributeCount();
																					for (int i = 0; i < imageAttributesCount; i++) {
																						String attribute = xmlStreamReader
																								.getAttributeLocalName(
																										i);
																						String value = xmlStreamReader
																								.getAttributeValue(i);
																						if (attribute.equalsIgnoreCase(
																								PROTEINATLAS.imageType)) {
																							imageIndividual.addLiteral(
																									ontoModel
																											.getProperty(
																													PROTEINATLAS.imageType),
																									value);
																						}
																					}
																					while (xmlStreamReader.hasNext()) {
																						eventCode = xmlStreamReader
																								.next();
																						if (XMLStreamConstants.END_ELEMENT == eventCode
																								&& xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.image)) {
																							break;
																						} else {
																							if (XMLStreamConstants.START_ELEMENT == eventCode
																									&& xmlStreamReader
																											.getLocalName()
																											.equalsIgnoreCase(
																													ElementNode.imageUrl)) {
																								String imageUrl = xmlStreamReader
																										.getElementText();
																								imageIndividual
																										.addLiteral(
																												ontoModel
																														.getProperty(
																																PROTEINATLAS.imageUrl),
																												imageUrl);
																							}
																						}
																					}
																				} else if (xmlStreamReader
																						.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.blotLanes)) {
																					while (xmlStreamReader.hasNext()) {
																						eventCode = xmlStreamReader
																								.next();
																						if (XMLStreamConstants.END_ELEMENT == eventCode
																								&& xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.blotLanes)) {
																							break;
																						} else {
																							if (XMLStreamConstants.START_ELEMENT == eventCode
																									&& xmlStreamReader
																											.getLocalName()
																											.equalsIgnoreCase(
																													ElementNode.lane)) {
																								/*
																								 * int
																								 * laneAttributesCount
																								 * =
																								 * xmlStreamReader
																								 * .
																								 * getAttributeCount
																								 * (
																								 * )
																								 * ;
																								 * for
																								 * (int
																								 * i
																								 * =
																								 * 0;
																								 * i
																								 * <
																								 * laneAttributesCount;
																								 * i
																								 * +
																								 * +
																								 * )
																								 * {
																								 * System
																								 * .
																								 * out
																								 * .
																								 * println(
																								 * "lane: "
																								 * +
																								 * xmlStreamReader
																								 * .
																								 * getAttributeLocalName
																								 * (
																								 * i)
																								 * +
																								 * " "
																								 * +
																								 * xmlStreamReader
																								 * .
																								 * getAttributeValue
																								 * (
																								 * i
																								 * )
																								 * )
																								 * ;
																								 * }
																								 */
																								while (xmlStreamReader
																										.hasNext()) {
																									eventCode = xmlStreamReader
																											.next();
																									if (XMLStreamConstants.END_ELEMENT == eventCode
																											&& xmlStreamReader
																													.getLocalName()
																													.equalsIgnoreCase(
																															ElementNode.lane)) {
																										break;
																									} else {
																										if (XMLStreamConstants.START_ELEMENT == eventCode
																												&& xmlStreamReader
																														.getLocalName()
																														.equalsIgnoreCase(
																																ElementNode.weight)) {
																											Individual blotLaneIndividual = ontoModel
																													.createIndividual(
																															PROTEINATLAS.AWB_BlotLanes,
																															AWB_BlotLanes++);
																											westernBlotIndividual
																													.addProperty(
																															ontoModel
																																	.getProperty(
																																			PROTEINATLAS.hasBlotLane),
																															blotLaneIndividual);
																											int weightAttributesCount = xmlStreamReader
																													.getAttributeCount();
																											for (int i = 0; i < weightAttributesCount; i++) {
																												String attribute = xmlStreamReader
																														.getAttributeLocalName(
																																i);
																												String value = xmlStreamReader
																														.getAttributeValue(
																																i);
																												if (attribute
																														.equalsIgnoreCase(
																																PROTEINATLAS.value)) {
																													blotLaneIndividual
																															.addLiteral(
																																	ontoModel
																																			.getProperty(
																																					PROTEINATLAS.value),
																																	value);
																												} else if (attribute
																														.equalsIgnoreCase(
																																PROTEINATLAS.unit)) {
																													blotLaneIndividual
																															.addLiteral(
																																	ontoModel
																																			.getProperty(
																																					PROTEINATLAS.unit),
																																	value);
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																} else if (xmlStreamReader.getLocalName()
																		.equalsIgnoreCase(ElementNode.proteinArray)) {
																	Individual proteinArrayIndividual = ontoModel
																			.createIndividual(
																					PROTEINATLAS.AB_ProteinArray,
																					AB_ProteinArray++);
																	antibodyIndividual.addProperty(
																			ontoModel.getProperty(
																					PROTEINATLAS.hasProtein),
																			proteinArrayIndividual);
																	int proteinArrayAttributesCount = xmlStreamReader
																			.getAttributeCount();
																	for (int i = 0; i < proteinArrayAttributesCount; i++) {
																		String attribute = xmlStreamReader
																				.getAttributeLocalName(i);
																		String value = xmlStreamReader
																				.getAttributeValue(i);
																		if (attribute.equalsIgnoreCase(
																				PROTEINATLAS.technology)) {
																			proteinArrayIndividual.addLiteral(
																					ontoModel.getProperty(
																							PROTEINATLAS.technology),
																					value);
																		}
																	}

																	while (xmlStreamReader.hasNext()) {
																		eventCode = xmlStreamReader.next();
																		if (XMLStreamConstants.END_ELEMENT == eventCode
																				&& xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.proteinArray)) {
																			break;
																		} else {
																			if (XMLStreamConstants.START_ELEMENT == eventCode) {
																				if (xmlStreamReader.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.verification)) {
																					Individual verificationIndividual = ontoModel
																							.createIndividual(
																									PROTEINATLAS.APA_Verification,
																									APA_Verification++);
																					proteinArrayIndividual.addProperty(
																							ontoModel.getProperty(
																									PROTEINATLAS.hasVerification),
																							verificationIndividual);
																					int verificationAttributesCount = xmlStreamReader
																							.getAttributeCount();
																					for (int i = 0; i < verificationAttributesCount; i++) {
																						String attribute = xmlStreamReader
																								.getAttributeLocalName(
																										i);
																						String value = xmlStreamReader
																								.getAttributeValue(i);
																						if (attribute.equalsIgnoreCase(
																								PROTEINATLAS.type)) {
																							verificationIndividual
																									.addLiteral(
																											ontoModel
																													.getProperty(
																															PROTEINATLAS.type),
																											value);
																						} else if (attribute
																								.equalsIgnoreCase(
																										PROTEINATLAS.description)) {
																							verificationIndividual
																									.addLiteral(
																											ontoModel
																													.getProperty(
																															PROTEINATLAS.description),
																											value);
																						}
																					}
																					String validation = xmlStreamReader
																							.getElementText();
																					verificationIndividual.addLiteral(
																							ontoModel.getProperty(
																									PROTEINATLAS.validation),
																							validation);
																				} /*
																					 * else
																					 * if
																					 * (xmlStreamReader
																					 * .
																					 * getLocalName
																					 * (
																					 * )
																					 * .
																					 * equalsIgnoreCase
																					 * (
																					 * ElementNode
																					 * .
																					 * antibodyDilution
																					 * )
																					 * )
																					 * {
																					 * int
																					 * antibodyDilutionAttributesCount
																					 * =
																					 * xmlStreamReader
																					 * .
																					 * getAttributeCount
																					 * (
																					 * )
																					 * ;
																					 * for
																					 * (int
																					 * i
																					 * =
																					 * 0;
																					 * i
																					 * <
																					 * antibodyDilutionAttributesCount;
																					 * i
																					 * +
																					 * +
																					 * )
																					 * {
																					 * System
																					 * .
																					 * out
																					 * .
																					 * println(
																					 * "antibodyDilution: "
																					 * +
																					 * xmlStreamReader
																					 * .
																					 * getAttributeLocalName
																					 * (
																					 * i)
																					 * +
																					 * " "
																					 * +
																					 * xmlStreamReader
																					 * .
																					 * getAttributeValue
																					 * (
																					 * i
																					 * )
																					 * )
																					 * ;
																					 * }
																					 * }
																					 */ else if (xmlStreamReader
																						.getLocalName()
																						.equalsIgnoreCase(
																								ElementNode.image)) {
																					Individual imageIndividual = ontoModel
																							.createIndividual(
																									PROTEINATLAS.APA_Image,
																									APA_Image++);
																					proteinArrayIndividual.addProperty(
																							ontoModel.getProperty(
																									PROTEINATLAS.hasImage),
																							imageIndividual);
																					int imageAttributesCount = xmlStreamReader
																							.getAttributeCount();
																					for (int i = 0; i < imageAttributesCount; i++) {
																						String attribute = xmlStreamReader
																								.getAttributeLocalName(
																										i);
																						String value = xmlStreamReader
																								.getAttributeValue(i);
																						if (attribute.equalsIgnoreCase(
																								PROTEINATLAS.imageType)) {
																							imageIndividual.addLiteral(
																									ontoModel
																											.getProperty(
																													PROTEINATLAS.imageType),
																									value);
																						}
																					}
																					while (xmlStreamReader.hasNext()) {
																						eventCode = xmlStreamReader
																								.next();
																						if (XMLStreamConstants.END_ELEMENT == eventCode
																								&& xmlStreamReader
																										.getLocalName()
																										.equalsIgnoreCase(
																												ElementNode.image)) {
																							break;
																						} else {
																							if (XMLStreamConstants.START_ELEMENT == eventCode
																									&& xmlStreamReader
																											.getLocalName()
																											.equalsIgnoreCase(
																													ElementNode.imageUrl)) {
																								String imageUrl = xmlStreamReader
																										.getElementText();
																								imageIndividual
																										.addLiteral(
																												ontoModel
																														.getProperty(
																																PROTEINATLAS.imageUrl),
																												imageUrl);
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
									ontoModel.saveOntoModel();
									System.exit(0);
									break;
								}
								// } else if (index == 11) {
								// ontoModel.saveOntoModel();
								// System.exit(0);
								// break;
								// }
								index++;
							}
						}
					}

				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LoadHpa.load();
	}

}
