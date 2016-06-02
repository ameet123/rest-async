package hello;

import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Publisher {

    @Autowired
    EventBus eventBus;

    @Autowired
    CountDownLatch latch;

    public ConcurrentMap<Integer, String> publishQuotes(int numberOfQuotes) throws InterruptedException {
        long start = System.currentTimeMillis();

        AtomicInteger counter = new AtomicInteger(1);
        ConcurrentMap<Integer, String> quoteMap = new ConcurrentHashMap<>(20);
        AnchoredRequest ar;
        for (int i = 0; i < numberOfQuotes; i++) {
            ar = new AnchoredRequest(i, quoteMap);
            // this is where each request is fired and processed asynchronously
            eventBus.notify("quotes", Event.wrap(ar));
        }

        latch.await();

        long elapsed = System.currentTimeMillis() - start;

        System.out.println("Elapsed time: " + elapsed + "ms");
        System.out.println("Total entries:" + quoteMap.size());
        return quoteMap;
    }

    public void processResults(ConcurrentMap<Integer, String> quoteMap) {
        for (Map.Entry<Integer, String> e : quoteMap.entrySet()) {
            System.out.println(e.getKey() + "=>\t" + e.getValue());
        }
    }

}