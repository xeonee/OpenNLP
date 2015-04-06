import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.featuregen.AdaptiveFeatureGenerator;


public class TrainPersonModel {
	public static void main(String[] args) throws IOException {
		int iterations = 100;
		int cutoff = 5;
		String baseDir = "D:\\Projects\\TT_HOME\\opennlp-models\\train\\";
		String destDir = baseDir;
		File inFile = new File(baseDir,"person.train");
		
		NameSampleDataStream nss = new NameSampleDataStream(new PlainTextByLineStream(new java.io.FileReader(inFile)));
		
		// train model.
		TokenNameFinderModel model = NameFinderME.train(
				"en", // language
				"person", // type
				nss,
				(AdaptiveFeatureGenerator) null,
				Collections.<String,Object>emptyMap(),
				iterations,
				cutoff);
		
		FileOutputStream outFileStream = new FileOutputStream(new File(destDir, "person-custom.bin"));
		model.serialize(outFileStream);
		
	}
} 