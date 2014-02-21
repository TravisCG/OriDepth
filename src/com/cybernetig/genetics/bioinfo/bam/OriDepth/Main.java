package com.cybernetig.genetics.bioinfo.bam.OriDepth;

/**
 * Read a bam file and create coverage where you can see the forward and reverse orientations
 * @author Travis
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DepthCounter dc = new DepthCounter(args[0]);
		dc.calc();
	}

}
