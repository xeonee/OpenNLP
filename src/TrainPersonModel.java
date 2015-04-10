import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.featuregen.AggregatedFeatureGenerator;
import opennlp.tools.util.featuregen.PreviousMapFeatureGenerator;
import opennlp.tools.util.featuregen.TokenClassFeatureGenerator;
import opennlp.tools.util.featuregen.TokenFeatureGenerator;
import opennlp.tools.util.featuregen.WindowFeatureGenerator;


public class TrainPersonModel {
	public static void main(String[] args) throws IOException {
		int iterations = 100;
		int cutoff = 5;
		String baseDir = "D:\\Projects\\TT_HOME\\opennlp-models\\train\\";
		String destDir = baseDir;
		File inFile = new File(baseDir,"eShopping-order.train");
		
		NameSampleDataStream nss = new NameSampleDataStream(new PlainTextByLineStream(new java.io.FileReader(inFile)));
		
		AggregatedFeatureGenerator featureGenerators =
				new AggregatedFeatureGenerator(
				new WindowFeatureGenerator(
				new TokenFeatureGenerator(), 3, 3),
				new WindowFeatureGenerator(
				new TokenClassFeatureGenerator(), 3, 3),
				new PreviousMapFeatureGenerator()
				);
		
		// train model.
		TokenNameFinderModel model = NameFinderME.train(
				"en", // language
				"person", // type
				nss,
				featureGenerators,
				Collections.<String,Object>emptyMap(),
				iterations,
				cutoff);
		
		FileOutputStream outFileStream = new FileOutputStream(new File(destDir, "eShopping-order.bin"));
		model.serialize(outFileStream);
		
	}
 



}