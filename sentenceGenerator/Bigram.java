package sentenceGenerator;

/**
 * Created by ryanhaines on 9/28/17.
 */
public class Bigram extends NGram {

    private String secondWord;
    private double secondWordCount;


    public Bigram(String firstWord, String secondWord, double firstWordCount, double secondWordCount){
        super(firstWord, firstWordCount);
        this.secondWord = secondWord;
        this.secondWordCount = secondWordCount;
        this.mleProbability = 0.0;
        this.addOneProbability = 0.0;
    }


    @Override
    public String toString(){
        String str;
            str = "First word: " + firstWord + "\nFirst word count: " + firstWordcount +
                    "\nSecond word: " + secondWord + "\nSecond word count: " + secondWordCount +
                    "\nMle probability: " + mleProbability;
            return str;
    }

    public String getSecondWord() {
        return secondWord;
    }

    public double getSecondWordCount() {
        return secondWordCount;
    }

}
