package com.cybernetig.genetics.bioinfo.bam.OriDepth;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.samtools.SAMFileHeader;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMFileReader.ValidationStringency;
import net.sf.samtools.SAMSequenceRecord;

public class DepthCounter {
	private String filename;
	private HashMap<String, ArrayList<Depth>> storage;
	
	public DepthCounter(String filename){
		this.filename = filename;
		storage = new HashMap<String, ArrayList<Depth>>();
	}
	
	public void calc(){
		SAMFileReader.setDefaultValidationStringency(ValidationStringency.SILENT);
		SAMFileReader sam = new SAMFileReader(new File(filename));
		processHeader(sam);
		
		for(SAMRecord rec: sam){
			if(rec.getReadUnmappedFlag()) continue;
			rec.getReadNegativeStrandFlag();
			String refname = rec.getReferenceName();
			ArrayList<Depth> ref = storage.get(refname);
			for(int i = rec.getAlignmentStart(); i <= rec.getAlignmentEnd(); i++){
				Depth depth = ref.get(i-1);
				if(rec.getReadNegativeStrandFlag()){
					depth.reverse++;
				}
				else{
					depth.forward++;
				}
			}
		}
		sam.close();
		
		printResults();
	}
	
	private void processHeader(SAMFileReader sam){
		SAMFileHeader header = sam.getFileHeader();
		for(int i = 0; i < header.getSequenceDictionary().size(); i++){
			SAMSequenceRecord ref = header.getSequence(i);
			ArrayList<Depth> array = new ArrayList<Depth>();
			for(int j = 0; j < ref.getSequenceLength(); j++){
				array.add(new Depth());
			}
			storage.put(ref.getSequenceName(), array);
		}
	}
	
	private void printResults(){
		for(String refname : storage.keySet()){
			ArrayList<Depth> ref = storage.get(refname);
			for(int i = 0; i < ref.size(); i++){
				System.out.println(refname + "\t" + (i+1) + "\t" + ref.get(i).forward + "\t" + ref.get(i).reverse);
			}
		}
	}
}
