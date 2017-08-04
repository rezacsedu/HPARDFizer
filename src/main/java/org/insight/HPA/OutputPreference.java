package org.insight.HPA;

public interface OutputPreference {
	public static String outputFileLocation = "AllGenes/proteinatlas.n3";
	public static String outputFormat = "N3"; 
	
	/**
	 * Note: Sample File Format and File Extension
	 * 
	 * 	TURTLE					proteinatlas.ttl
	 * 	TTL						proteinatlas.ttl
	 * 	Turtle					proteinatlas.ttl
	 * 	N-TRIPLES				proteinatlas.ntl
	 * 	N-TRIPLE				proteinatlas.ntl
	 * 	NT						proteinatlas.ntl
	 * 	JSON-LD					proteinatlas.json
	 * 	RDF/XML-ABBREV			proteinatlas.xml
	 * 	RDF/XML					proteinatlas.xml
	 * 	N3						proteinatlas.n3
	 * 	RDF/JSON				proteinatlas.json
	 * 
	 */
}
