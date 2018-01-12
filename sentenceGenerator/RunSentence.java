package sentenceGenerator;

import java.io.IOException;


/**
 * Created by ryanhaines on 9/27/17.
 */
public class RunSentence {


    public static void main(String[] args) throws IOException{
        LanguageModel lm = new LanguageModel();
        SentenceGenerator sg = new SentenceGenerator(lm);
        System.out.println(sg.generate(8));
    }



}
