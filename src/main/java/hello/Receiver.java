package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.*;
import java.util.concurrent.*;

@Service
class Receiver implements Consumer<Event<AnchoredRequest>> {

    @Autowired
    CountDownLatch latch;

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public void accept(Event<AnchoredRequest> anchoredRequestEvent) {
        QuoteResource quoteResource =
                restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", QuoteResource.class);
        ConcurrentMap<Integer, String> quoteMap = anchoredRequestEvent.getData().getQuoteMap();
        quoteMap.put(anchoredRequestEvent.getData().getQuoteNumber(), quoteResource.getValue().getQuote());
//        System.out.println("Quote " + anchoredRequestEvent.getData().getQuoteNumber() + ": " + quoteResource.getValue
//                ().getQuote());
        latch.countDown();
    }
}