package sentenceGenerator;

/**
 * Created by ryanhaines on 9/12/17.
 */
public class WordProcess {

    // Calculates the MLE probablity of a word given its count in the corpus
    // and the overall size of the corpus
    public static double maxEstimationUni(Unigram unigram, int totalWordSize){
        return unigram.getFirstWordcount()/(double)totalWordSize;
    }

    // Calculates the MLE probability of bigrams
    // Takes a bigram as input and estimates the probability of
    // occurrence of the second word following the first word
    // EX: "This is" find # of times that "is" appears after "this"
    public static double maxEstimationBi(Bigram bigram){
        // SecondWordCount implies number of times that given bi-gram occurs
        return bigram.getSecondWordCount()/(double)bigram.getFirstWordcount();
    }


}
