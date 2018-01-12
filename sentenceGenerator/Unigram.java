package sentenceGenerator;

/**
 * Created by ryanhaines on 9/28/17.
 */
public class Unigram extends NGram {


    public Unigram(String word, double count){
        super(word, count);
        this.mleProbability = 0.0;
        this.addOneProbability = 0.0;
    }



}
