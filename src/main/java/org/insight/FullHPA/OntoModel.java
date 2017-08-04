/**
 * 
 */
package org.insight.FullHPA;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.shared.Lock;
import org.apache.jena.util.FileManager;

public class OntoModel {

	private OntModel ontModel = null;
	
	static final String OWL_PATH = "/home/rezkar/ontology/proteinatlas.owl";
	static final String OWL_BASE = "http://data.bioinfo.deri.ie/gene/";
	Integer avaialbleInstances = 1;
	public OntoModel() {
		ontModel = ModelFactory.createOntologyModel();
		ontModel.read(FileManager.get().open(OWL_PATH), "");
	}
	
	public void saveOntoModel() {
		// Update the Model into the OWL file.
		OutputStream out;
		try {
			System.out.println("Ontology Saving started!");
			ontModel.enterCriticalSection(Lock.WRITE);
			try {
				out = new FileOutputStream(OutputPreference.outputFileLocation);
				ontModel.write(out, OutputPreference.outputFormat);
				out.close();
			} finally {
				ontModel.leaveCriticalSection();
			}
			System.out.println("Ontology Saved!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Property getProperty(String name) {
		Property property = ontModel.getProperty(OWL_BASE + name);
		return property;
	}
	
	public Individual createIndividual(String name, Integer index) {
		
		OntClass ontClass = ontModel.getOntClass(OWL_BASE + name);
		Individual geneIndividual = ontModel.createIndividual(OWL_BASE + name + index, ontClass);
		geneIndividual.addLiteral(getProperty("index"), index);
		/*ExtendedIterator<?> et = ontClass.listInstances();
		Integer avaialbleInstances = 0;
		while(et.hasNext()) {
			et.next();
			avaialbleInstances++;
		}
		et.close();
		Individual geneIndividual = ontModel.createIndividual(OWL_BASE + name + (avaialbleInstances + 1), ontClass);*/
		
		
		//saveOntoModel();
		
		return geneIndividual;
	}
	
	public Individual createIndividual(String name, String value) {
		
		OntClass ontClass = ontModel.getOntClass(OWL_BASE + name);
		Individual geneIndividual = ontModel.createIndividual(value, ontClass);
		
		return geneIndividual;
	}
}
