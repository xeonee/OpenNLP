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
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;


public class OpenNLP_NER_MultiNames {

	public static void main(String[] args) throws InvalidFormatException, FileNotFoundException, IOException {
		String modelPath = "D:\\Projects\\TT_HOME\\opennlp-models\\";
				
		String[] sentences = {
				"Former first lady Nancy Reagan was taken to a " +
						"suburban Los Angeles " +
						"hospital "+"as a precaution" +"Sunday after a " +
						"fall at her home, an " +
						"aide said. ",
						"The 86-year-old Reagan will remain overnight for " +
								"observation at a hospital in Santa Monica, California, " +
								"said Joanne " +
		"Drake, chief of staff for the Reagan Foundation on 23rd July."};

		NameFinderME finder = new NameFinderME(new TokenNameFinderModel(new FileInputStream(new File(modelPath + "en-ner-organization.bin"))));
		
		Tokenizer tokenizer = SimpleTokenizer.INSTANCE;

		for (int si = 0; si < sentences.length; si++) {
			String[] tokens = tokenizer.tokenize(sentences[si]);
			Span[] names = finder.find(tokens);
			displayNames(names, tokens);
		}

		finder.clearAdaptiveData();
	}

	private static void displayNames(Span[] names, String[] tokens) {
		for (int si = 0; si < names.length; si++) {
			StringBuilder cb = new StringBuilder();
			for (int ti = names[si].getStart(); ti < names[si].getEnd(); ti++) {
				cb.append(tokens[ti]).append(" ");
			}
			System.out.println(cb.substring(0, cb.length() - 1));
			System.out.println("ttype: " + names[si].getType());
		}
	}

	private static List<String> getPesonalModel() throws IOException {
		 String sCurrentLine;
		List<String> lst = new ArrayList<String>();
		//this should return an object that has all the info about the doc you want
		BufferedReader br = new BufferedReader(new FileReader("D://msg.txt"));
		while ((sCurrentLine = br.readLine()) != null) {
			lst.add(sCurrentLine);
		}
		return lst;
	}
	
}
