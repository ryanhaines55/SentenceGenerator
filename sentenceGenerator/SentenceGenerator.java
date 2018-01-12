package sentenceGenerator;

import java.util.*;

/**
 * Created by ryanhaines on 9/12/17.
 */
public class SentenceGenerator {

    private LanguageModel languageModel;


    public SentenceGenerator(LanguageModel languageModel){
        this.languageModel = languageModel;
    }

    // Generates a sentence of a given length
    public String generate(int sentenceLength){
        String generatedSentence = "";
        String previouslySelectedWord = "";
        for(int i=0; i<sentenceLength; i++){
            if(i == 0){
                previouslySelectedWord = pickFirstWord(languageModel.getUniGrams());
                generatedSentence += previouslySelectedWord + " ";
            }
            else if(i == sentenceLength-1){
                previouslySelectedWord = pickNextWord(languageModel.getBiGrams(), previouslySelectedWord);
                generatedSentence += previouslySelectedWord;
            }
            else {
                previouslySelectedWord = pickNextWord(languageModel.getBiGrams(), previouslySelectedWord);
                generatedSentence += previouslySelectedWord + " ";
            }
        }

        return generatedSentence;
    }

    // Selects a word to begin a sentence
    private String pickFirstWord(List<Unigram> unigrams){

        // Word which will be returned
        String selectedWord = "";

        // All uni-grams
        HashMap<String, Integer> numberLine = assignWeight(getUnigramMLEValues(unigrams));

        // Key set for HashMap
        Set<String> numberLineKeys = numberLine.keySet();

        // Random object
        Random randomIndex = new Random();

        // Max bound of the number line
        int maxBound = getMaxBound(numberLine);

        // Random location corresponding to the word that will be selected
        int selectedIndex = randomIndex.nextInt(maxBound) + 1;

        if(selectedIndex > maxBound){
            System.out.println("Selected index is greater than max bound check logic");
                System.exit(0);
        }


        // Counter for current location of weights
        int currentIndexValue = 0;

        // Loop through words and continuously add their weights until passing selected index
        for(String s : numberLineKeys){
            currentIndexValue += numberLine.get(s);
            if(currentIndexValue < 0){
                System.out.println("Selected index is negative check logic");
                break;
            }
            // selected index not reached yet
            if(currentIndexValue < selectedIndex){
                continue;
            }
            // selected index has been passed and falls in the range of the current strings weight
            if((currentIndexValue > selectedIndex) && (currentIndexValue <= maxBound)){
                selectedWord = s;
                break;
            }
        }

        return selectedWord;
    }

    // Selects a new word based on the history of the previous word
    private String pickNextWord(List<Bigram> bigrams, String previouslyPicked){

        // Word which will be returned
        String selectedWord = "";

        // Get bi-grams that match previous word and assign weights
        HashMap<String, Integer> numberLine = assignWeight(getBigramMLEValues(bigrams, previouslyPicked));

        // Key set for HashMap
        Set<String> numberLineKeys = numberLine.keySet();

        // Random object
        Random randomIndex = new Random();

        // Max bound of the number line
        int maxBound = getMaxBound(numberLine);

        // Random location corresponding to the word that will be selected
        int selectedIndex = randomIndex.nextInt(maxBound) + 1;

        if(selectedIndex > maxBound){
            System.out.println("Selected index is greater than max bound check logic");
            System.exit(0);
        }

        // Counter for current location of weights
        int currentIndexValue = 0;

        // Loop through words and continuously add their weights until passing selected index
        for(String s : numberLineKeys){
            currentIndexValue += numberLine.get(s);
            if(currentIndexValue < 0){
                System.out.println("Selected index is negative check logic");
                break;
            }
            // selected index not reached yet
            if(currentIndexValue < selectedIndex){
                continue;
            }
            // selected index has been passed and falls in the range of the current strings weight
            if((currentIndexValue > selectedIndex) && (currentIndexValue <= maxBound)){
                selectedWord = s;
                break;
            }
        }

        return selectedWord;

    }

    // Gathers all mle values for bi-grams that have the same first word as previously picked word
    private HashMap<String, Double> getBigramMLEValues(List<Bigram> bigrams, String previousGram){

        HashMap<String, Double> numberLine = new HashMap<>();
        for(Bigram b : bigrams){
            if(b.getFirstWord().equals(previousGram)){
                numberLine.put(b.getSecondWord(), b.getMleProbability());
            }
        }
        return numberLine;
    }

    // Gathers all mle values for unigrams
    private HashMap<String, Double> getUnigramMLEValues(List<Unigram> unigrams){
        HashMap<String, Double> numberLine = new HashMap<>();
        //Collections.sort(unigrams, new CompareByMLE());
        for(Unigram u : unigrams){
            numberLine.put(u.getFirstWord(), u.getMleProbability());
        }
        return numberLine;
    }

    // Converts mle values into an integer weight
    private HashMap<String, Integer> assignWeight(HashMap<String, Double> nGramMap){

        int maxBound = 10000;
        Set<String> nGramSet = nGramMap.keySet();
        HashMap<String, Integer> weightedNGramMap = new HashMap<>();

        for(String s : nGramSet){
            double mleValue = nGramMap.get(s) * maxBound;
            if(mleValue < 1.0){
                weightedNGramMap.put(s, 1);
            }
            else{
                weightedNGramMap.put(s, (int)(Math.round(mleValue)));
            }
        }
        return weightedNGramMap;
    }

    // Returns the upper bound of all combined weights
    private int getMaxBound(HashMap<String, Integer> numberLine){
        Set<String> numberLineKeys = numberLine.keySet();
        int maxBound = 0;
        for(String s : numberLineKeys){
            maxBound += numberLine.get(s);
        }
        return maxBound;
    }


}
