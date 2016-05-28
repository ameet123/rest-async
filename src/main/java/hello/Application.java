package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.core.*;
import reactor.core.dispatch.*;

import java.util.concurrent.*;

import static reactor.bus.selector.Selectors.$;

/**
 * this application publishes a set of messages which are processed by a receiver
 * additionally, the results of the requests are put back into a concurrent map
 * This map is made into an event which is passed as a set of requests to the receivers.
 * The publisher can be sent a collection of requests in this manner and
 * the publisher will break it up into individual tasks, then fire those tasks to
 * the receiver and in the process collect the results and send it back as a return value.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application implements CommandLineRunner {

    private static final int NUMBER_OF_QUOTES = 10;
    /**
     * this is the alternative dispatcher
     */
    private static final Dispatcher RING_DISPATCHER = new RingBufferDispatcher("Quote", 32);


    @Bean
    Environment env() {
        return Environment.initializeIfEmpty()
                .assignErrorJournal();
    }

    /**
     * we can change the dispatcher to either thread pool or ring buffer
     * new RingBufferDispatcher("Quote", 32)
     *
     * @param env
     * @return
     */
    @Bean
    EventBus createEventBus(Environment env) {
        return EventBus.create(env, Environment.THREAD_POOL);
    }

    @Autowired
    private EventBus eventBus;

    @Autowired
    private Receiver receiver;

    @Autowired
    private Publisher publisher;

    @Bean
    public CountDownLatch latch() {
        return new CountDownLatch(NUMBER_OF_QUOTES);
    }

    @Override
    public void run(String... args) throws Exception {
        eventBus.on($("quotes"), receiver);
        ConcurrentMap<Integer, String> results = publisher.publishQuotes(NUMBER_OF_QUOTES);
        publisher.processResults(results);
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext app = SpringApplication.run(Application.class, args);

        app.getBean(CountDownLatch.class).await(1, TimeUnit.SECONDS);
        app.getBean(Environment.class).shutdown();
    }
}