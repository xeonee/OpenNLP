import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class OpenNLPNER implements Runnable
{

    static TokenizerModel tm = null;
    static TokenNameFinderModel locModel = null;
    String doc;
    NameFinderME myNameFinder;
    TokenizerME wordBreaker;
    SentenceDetector sd;

    public OpenNLPNER()
    {
    }

    public OpenNLPNER(String document, SentenceDetector sd, NameFinderME mf, TokenizerME wordBreaker)
    {
        System.out.println("got doc");
        this.sd = sd;
        this.myNameFinder = mf;
        this.wordBreaker = wordBreaker;
        doc = document;
    }

    private static List<String> getMyDocsFromSomewhere() throws IOException
    {
    	String sCurrentLine;
    	List<String> lst = new ArrayList<String>();
        //this should return an object that has all the info about the doc you want
    	BufferedReader br = new BufferedReader(new FileReader("D://msg.txt"));
    	while ((sCurrentLine = br.readLine()) != null) {
			lst.add(sCurrentLine);
		}
        return lst;
    }

    public static void main(String[] args)
    {
        try
        {
            String modelPath = "D:\\Projects\\TT_HOME\\opennlp-models\\";

            if (tm == null)
            {
                //user does normal namefinder instantiations...
                InputStream stream = new FileInputStream(new File(modelPath + "en-token.bin"));
                // new SentenceDetectorME(new SentenceModel(new FileInputStream(new File(modelPath + "en-sent.zip"))));
                tm = new TokenizerModel(stream);
                // new TokenizerME(tm);
                locModel = new TokenNameFinderModel(new FileInputStream(new File(modelPath + "en-ner-location.bin")));
                //  new NameFinderME(locModel);
            }


            System.out.println("getting data");
            List<String> docs = getMyDocsFromSomewhere();
            System.out.println("done getting data");
            // FileWriter fw = new FileWriter("C:\\apache\\modelbuilder\\sentences.txt");




            for (String doc : docs)
            {
//            	System.out.println(doc);
                //you could also use the runnable here and launch in a diff thread
                new OpenNLPNER(doc,
                        new SentenceDetectorME(new SentenceModel(new FileInputStream(new File(modelPath + "en-sent.bin")))),
                        new NameFinderME(locModel), new TokenizerME(tm)).run();

            }

            System.out.println("done");


        } catch (Exception ex)
        {
            System.out.println(ex);
        }


    }

    @Override
    public void run()
    {
        try
        {
            process(doc);
        } catch (Exception ex)
        {
            System.out.println(ex);
        }
    }

    public void process(String document) throws Exception
    {

          System.out.println(document);
        //  user instantiates the non static entitylinkerproperty object and constructs is with a pointer to the prop file they need to use
        String modelPath = "C:\\apache\\entitylinker\\";


        //input document
        myNameFinder.clearAdaptiveData();
        //user splits doc to sentences
        String[] sentences = sd.sentDetect(document);
        //get the sentence spans
        Span[] sentenceSpans = sd.sentPosDetect(document);
        Span[][] allnamesInDoc = new Span[sentenceSpans.length][];
        String[][] allTokensInDoc = new String[sentenceSpans.length][];

        for (int sentenceIndex = 0; sentenceIndex < sentences.length; sentenceIndex++)
        {
            String[] stringTokens = wordBreaker.tokenize(sentences[sentenceIndex]);
            Span[] tokenSpans = wordBreaker.tokenizePos(sentences[sentenceIndex]);
            Span[] spans = myNameFinder.find(stringTokens);
            allnamesInDoc[sentenceIndex] = spans;
            allTokensInDoc[sentenceIndex] = stringTokens;
        }

        //now access the data like this...
        for (int s = 0; s < sentenceSpans.length; s++)
        {
            Span[] namesInSentence = allnamesInDoc[s];
            String[] tokensInSentence = allTokensInDoc[s];
            String[] entities = Span.spansToStrings(namesInSentence, tokensInSentence);
            for (String entity : entities)
            {
                //start building up the XML here....
                System.out.println(entity + " Was in setnence " + s + " @ " + namesInSentence[s].toString());
            }
        }
    }
}