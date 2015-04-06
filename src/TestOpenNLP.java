import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

public class TestOpenNLP {

	public static void main(String[] args) throws InvalidFormatException, FileNotFoundException, IOException {
		String modelPath = "D:\\Projects\\TT_HOME\\opennlp-models\\";
		String[] models = {"money","person"};
		//		String[] sentences = {"Former first lady Nancy Reagan was taken to a " + "suburban Los Angeles " +"hospital "+"as a precaution" +"Sunday after a " +
		//				"fall at her home, an " + "aide said. ",
		//				"The 86-year-old Reagan will remain overnight for " + "observation at a hospital in Santa Monica, California, " +"said Joanne " +
		//		"Drake, chief of staff for the Reagan Foundation on 23rd July."};
		
		String[] sentences = {"Hi Reagan, your Order No.HKD-18827-1717841 for the money $993 is placed and will be delivered tomorrow. Thank you!", 
				"We are delighted to inform you that Order No.HKD-18827-1717841 has been delivered.",
				"Final part of your Order No. HKD-18827-1717841 is shipped via GoJavas Tracking No. DELHLTP273756.",
				"The order with docket number : DELHLTP273756 has been delivered by GoJaVAS.", 
				"Hi, we will be at your address today to attempt delivery of HEALTHKART order, tracking no. DELHLTP273756.",
				"We wish to confirm the receipt of your HEALTHKART order with us vide AWB  DELHLTP273756. We will attempt the delivery in next 2 to 4 days. Regards Team Gojavas",
				"I have money $229"};
		
		//		NameFinderME finder = new NameFinderME(new TokenNameFinderModel(new FileInputStream(new File(modelPath + "en-ner-organization.bin"))));
		NameFinderME[] finders = new NameFinderME[models.length];

		for (int mi = 0; mi < models.length; mi++) {
			finders[mi] = new NameFinderME(new TokenNameFinderModel(new FileInputStream(new File(modelPath, "en-ner-" + models[mi] + ".bin"))));
		}

		Tokenizer tokenizer = SimpleTokenizer.INSTANCE;

		for (int si = 0; si < sentences.length; si++) {
			List<Annotation> allAnnotations = new ArrayList<Annotation>();
			String[] tokens = tokenizer.tokenize(sentences[si]);
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
			System.out.println("ttype: " + names[si].getType());
		}
	}

}
