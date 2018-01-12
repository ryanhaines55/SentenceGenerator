package sentenceGenerator;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by ryanhaines on 9/12/17.
 */
public class LanguageModel {

    private int totalWordSize;
    private int uniqueWordSize;
    private List<Unigram> uniGrams;
    private List<Bigram> biGrams;
    private HashMap<String, Double> uniGramMap;
    private HashMap<String, Double> biGramMap;


    public LanguageModel() throws IOException {
        uniGramMap = readUnigrams("Replace with corpus file", uniGramMap);
        biGramMap = readBigrams("Replace with corpus file", uniGramMap, biGramMap);
        uniGrams = createUniGrams(uniGramMap, uniGrams);
        biGrams = createBiGrams(uniGramMap, biGramMap, biGrams);
        totalWordSize = totalWordCount(uniGramMap);
        uniqueWordSize = uniGrams.size();
        calculateUnigramMleProbability(uniGrams);
        calculateBigramMleProbability(biGrams);
    }

    // Creates a combined language model for a training and held out corpus used with smoothing
    public LanguageModel(LanguageModel trainCorpusModel, LanguageModel heldoutCorpusModel){
        uniGramMap = combineMap(trainCorpusModel.getUniGramMap(), heldoutCorpusModel.getUniGramMap());
        biGramMap = combineMap(trainCorpusModel.getBiGramMap(), heldoutCorpusModel.getBiGramMap());
        uniGrams = createUniGrams(uniGramMap, uniGrams);
        biGrams = createBiGrams(uniGramMap, biGramMap, biGrams);
        totalWordSize = trainCorpusModel.getTotalWordSize() + heldoutCorpusModel.getTotalWordSize();
        uniqueWordSize = uniGrams.size();
    }

    // Creates an index of all unique words and their counts
    private HashMap<String,Double> readUnigrams(String corpus, HashMap<String, Double> uniGramMa) throws IOException {
        // Scan all the tokens and strip all non alphanumerical characters in specified corpus
        File corpusFile = new File(corpus);
        Scanner strip = new Scanner(corpusFile);
        String gram = "";
        uniGramMa = new HashMap<>();
        while (strip.hasNextLine()){
            gram = strip.next();
            gram = gram.replaceAll("[^A-Za-z0-9]", "");
            gram = gram.toLowerCase();
            if(gram.equals(" ")){
                continue;
            }
            else if(gram.equals(gram.toUpperCase()) && !gram.equals("I")){
                continue;
            }
            else if(!uniGramMa.containsKey(gram)){
                uniGramMa.put(gram, 0.0);

            }

            uniGramMa.put(gram, (uniGramMa.get(gram) + 1));
        }
        strip.close();
        return uniGramMa;
    }

    // Creates an index of all unique pairs of words and their counts
    private HashMap<String, Double> readBigrams(String corpus, HashMap<String,Double> unigramsCount, HashMap<String,Double> biGramsCount) throws IOException{

        // Scan through the tokens again and copy every 2 to create bigrams
        File corpusFile = new File(corpus);
        Scanner strip = new Scanner(corpusFile);
        String first;
        String second;
        String bigram = "";
        String[] array = new String[2];

        first = strip.next();
        first = first.replaceAll("[^A-Za-z0-9]", "");
        first = first.toLowerCase();
        biGramsCount = new HashMap<>();
        while(strip.hasNextLine()){
            second = strip.next();
            second = second.replaceAll("[^A-Za-z0-9]", "");
            second = second.toLowerCase();
            bigram = first + " " + second;
            if(second.equals(" ")){
                continue;
            }
            else if(second.equals(second.toUpperCase()) && !second.equals("I")){
                continue;
            }
            else if(!biGramsCount.containsKey(bigram)){
                array = bigram.split("\\s");
                if(array.length == 1){
                }
                else{
                    biGramsCount.put(bigram, 0.0);
                }

            }
            biGramsCount.put(bigram, (biGramsCount.get(bigram)+1));
            first = second;

        }

        strip.close();
        return biGramsCount;

    }

    // Create uni-gram objects for all words
    private List<Unigram> createUniGrams(HashMap<String,Double> uniGramMap, List<Unigram> nGrams){
        Set<String> uniGramKeys = uniGramMap.keySet();
        nGrams = new ArrayList<>();
        for(String s : uniGramKeys){
           nGrams.add(new Unigram(s, uniGramMap.get(s)));
        }
        return nGrams;
    }

    // Create bi-gram objects for all bi-grams
    private List<Bigram> createBiGrams(HashMap<String,Double> uniGramMap, HashMap<String,Double> biGramMap, List<Bigram> nGrams){
        Set<String> biGramKeys = biGramMap.keySet();
        nGrams = new ArrayList<>();
        for(String s : biGramKeys){
            String[] temp = s.split(" ");
            nGrams.add(new Bigram(temp[0], temp[1], uniGramMap.get(temp[0]), biGramMap.get(s)));
        }
        return nGrams;
    }

    // Search for a uni-gram object by specifying a possible value of a word in the corpus
    public Unigram findWord(String wordToFind){
        for(Unigram n : uniGrams){
            if(n.getFirstWord().equals(wordToFind)){
                return n;
            }
        }
        return null;
    }

    // Gets the total number of words in the language model
    public int totalWordCount(HashMap<String,Double> unigramMap){

        int totCount = 0;

        Set<String> keyConvert = unigramMap.keySet();

        List<String> keySet = new ArrayList<>(keyConvert);

        for(int i=0; i<keySet.size(); i++){

            totCount += unigramMap.get(keySet.get(i));

        }

        return totCount;

    }

    // Finds a bigram in the language model if it exists
    public Bigram findWordPair(String wordPairToFind){
        for(Bigram b : biGrams){
            if((b.getFirstWord() + " " + b.getSecondWord()).equals(wordPairToFind)){
                return b;
            }
        }
        return null;
    }

    // Returns the unique number of words in the corpus
    public int getUniqueWordSize() {
        return uniqueWordSize;
    }

    // Returns the total number of words in the corpus
    public int getTotalWordSize() {
        return totalWordSize;
    }

    private void calculateUnigramMleProbability(List<Unigram> unigramList){
        for(Unigram u : unigramList){
            u.setMleProbability(WordProcess.maxEstimationUni(u, totalWordSize));
        }
    }

    private void calculateBigramMleProbability(List<Bigram> bigramList){
        for(Bigram b : bigramList){
            b.setMleProbability(WordProcess.maxEstimationBi(b));
        }
    }

    public List<Unigram> getUniGrams() {
        return uniGrams;
    }

    public List<Bigram> getBiGrams() {
        return biGrams;
    }

    public HashMap<String, Double> getBiGramMap() {
        return biGramMap;
    }

    public HashMap<String, Double> getUniGramMap() {
        return uniGramMap;
    }

    // Takes in two maps of word to word count and makes a new map of unique words for both original maps
    public HashMap<String,Double> combineMap(HashMap<String,Double> map80, HashMap<String,Double> map20){

        System.out.println("Combining Maps...");

        Set<String> keyConvert80 = map80.keySet();

        List<String> keySet80 = new ArrayList<>(keyConvert80);

        HashMap<String, Double> mapAll = new HashMap<>();

        System.out.println("Map size " + keySet80.size());

        for(int i=0; i<keySet80.size(); i++){

            mapAll.put(keySet80.get(i), map80.get(keySet80.get(i)));

        }

        Set<String> keyConvert20 = map20.keySet();

        List<String> keySet20 = new ArrayList<>(keyConvert20);

        System.out.println("Map size " + keySet20.size());

        for(int i=0; i<keySet20.size(); i++){

            if(!keyConvert80.contains(keySet20.get(i))){

                mapAll.put(keySet20.get(i), map20.get(keySet20.get(i)));

            }

        }

        return mapAll;

    }


}
