package sentenceGenerator;


/**
 * Created by ryanhaines on 9/27/17.
 */
public class NGram {

    protected String firstWord;
    protected double firstWordcount;
    protected double mleProbability;
    protected double addOneProbability;

    // Constructor for uni-grams
    public NGram(String gramValue, double wordCount){
        this.firstWord = gramValue;
        this.firstWordcount = wordCount;
        this.mleProbability = 0.0;
        this.addOneProbability = 0.0;
    }


    @Override
    public String toString(){
        String str;

        str = "Word: " + firstWord + "\nCount: " + firstWordcount + "\nMle probability: " + mleProbability;

        return str;
    }

    public void setMleProbability(double mleProbability) {
        this.mleProbability = mleProbability;
    }

    public void setAddOneProbability(double addOneProbability) {
        this.addOneProbability = addOneProbability;
    }

    public String getFirstWord() {
        return firstWord;
    }

    public double getFirstWordcount() {
        return firstWordcount;
    }

    public double getMleProbability() {
        return mleProbability;
    }

    public double getAddOneProbability() {
        return addOneProbability;
    }

    public double compareTo(NGram nGram){

        double compareValue = nGram.getMleProbability();

        return this.mleProbability - compareValue;
    }

}
