package hello;

import java.util.concurrent.*;

/**
 * Created by ameet.chaubal on 5/27/2016.
 * something that can be used to create an event,
 * we need two things - an integer and a List to pack into
 */
public class AnchoredRequest {
    int quoteNumber;
    ConcurrentMap<Integer, String> quoteMap;

    public AnchoredRequest(int quoteNumber, ConcurrentMap<Integer, String> quoteMap) {
        this.quoteNumber = quoteNumber;
        this.quoteMap = quoteMap;
    }

    public ConcurrentMap<Integer, String> getQuoteMap() {
        return quoteMap;
    }

    public void setQuoteMap(ConcurrentMap<Integer, String> quoteMap) {
        this.quoteMap = quoteMap;
    }

    public int getQuoteNumber() {

        return quoteNumber;
    }

    public void setQuoteNumber(int quoteNumber) {
        this.quoteNumber = quoteNumber;
    }
}
