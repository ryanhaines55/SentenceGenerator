package sentenceGenerator;

import java.util.Comparator;

/**
 * Created by ryanhaines on 9/29/17.
 */
public class CompareByMLE implements Comparator<NGram> {

    public int compare(NGram o1, NGram o2){
        return Double.compare(o1.getMleProbability(), o2.getMleProbability());
    }

}
