import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

public class TestOpenNLP {

	public static void main(String[] args) throws InvalidFormatException, FileNotFoundException, IOException {
		String modelPath = "D:\\Projects\\TT_HOME\\opennlp-models\\train\\";
		String[] models = {"eShopping-order"};
		//		String[] sentences = {"Former first lady Nancy Reagan was taken to a " + "suburban Los Angeles " +"hospital "+"as a precaution" +"Sunday after a " +
		//				"fall at her home, an " + "aide said. ",
		//				"The 86-year-old Reagan will remain overnight for " + "observation at a hospital in Santa Monica, California, " +"said Joanne " +
		//		"Drake, chief of staff for the Reagan Foundation on 23rd July."};
		
//		String[] sentences = {"Hi Reagan, your Order No. HKD-18827-1717841 for the amount 993 is placed and will be delivered tomorrow at 12 PM. Thank you!",
//				"Hi Nancy, your Order No.HKD-18827-1717841 for the amount 123 million, is placed and will be delivered tomorrow. Thank you!",
//				"Hi Reagan, your Order No.HKD-18827-1717841 for the amount $999 is placed and will be delivered tomorrow. Thank you!", "Hi Amardeep said",
//				"Items in order OD40705102573 have been shipped. Do check email for tracking details. Invoice will be sent to your registered email within 24 hours of delivery."};
		
		//		NameFinderME finder = new NameFinderME(new TokenNameFinderModel(new FileInputStream(new File(modelPath + "en-ner-organization.bin"))));
		
		List<String> sentences = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader("D:\\Projects\\TT_HOME\\opennlp-models\\train\\eShopping.txt"));
		String line;
	    while ((line = br.readLine()) != null) {
	    	if(line.startsWith("--"))
	    		sentences.add(line);
	    }
		
		
//		String[] sentences = new String[i];
		
		NameFinderME[] finders = new NameFinderME[models.length];

		for (int mi = 0; mi < models.length; mi++) {
			finders[mi] = new NameFinderME(new TokenNameFinderModel(new FileInputStream(new File(modelPath, "" + models[mi] + ".bin"))));
		}

		Tokenizer tokenizer = OrderTokenizer.INSTANCE;

		for (int si = 0; si < sentences.size(); si++) {
			List<Annotation> allAnnotations = new ArrayList<Annotation>();
			String[] tokens = tokenizer.tokenize(sentences.get(si));
			for (int fi = 0; fi < finders.length; fi++) {
				Span[] names = finders[fi].find(tokens);
				displayNames(names, tokens);
			}
		}

		for (NameFinderME nameFinderME : finders) {
			nameFinderME.clearAdaptiveData();
		}
	}

	private static void displayNames(Span[] names, String[] tokens) {
		for (int si = 0; si < names.length; si++) {
			StringBuilder cb = new StringBuilder();
			for (int ti = names[si].getStart(); ti < names[si].getEnd(); ti++) {
				cb.append(tokens[ti]).append(" ");
			}
			System.out.println(cb.substring(0, cb.length() - 1));
//			System.out.println("ttype: " + names[si].getType());
		}
	}

}
