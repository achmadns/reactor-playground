package com.github.achmadns.lab;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultPollingEndpoint;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.core.dispatch.ThreadPoolExecutorDispatcher;

import static java.lang.Runtime.getRuntime;
import static reactor.Environment.get;

@ComponentScan
@EnableAutoConfiguration
@EnableAspectJAutoProxy
public class App {

    static {
        Environment.initializeIfEmpty();
    }

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Action action;
    @Autowired
    private CamelContext camel;
    @Autowired
    private EventBus pool;

    @Bean
    EventBus pool() {
        return EventBus.create(get(), new ThreadPoolExecutorDispatcher(getRuntime().availableProcessors() * 2, 1024));
    }

    @Bean
    Resource resource() {
        return new Resource();
    }

    @Bean
    Action action(Resource resource, EventBus pool) {
        return new Action(resource, pool);
    }

    @Bean
    RouteBuilder route() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(endpoint()).process(exchange -> {
                    pool.notify("processed", Event.wrap(exchange.getIn().getBody()));
                    log.info("{} was processed.", exchange.getIn().getBody());
                });
            }

            // a simple custom endpoint without creating full fledged component
            private DefaultPollingEndpoint endpoint() {
                return new DefaultPollingEndpoint() {
                    final DefaultProducer producer = new DefaultProducer(this) {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            log.info("Got exchange: {}", exchange);
                        }
                    };

                    @Override
                    protected String createEndpointUri() {
                        return "dummy:in";
                    }

                    @Override
                    public Producer createProducer() throws Exception {
                        return producer;
                    }

                    @Override
                    public Consumer createConsumer(Processor processor) throws Exception {
                        return new ScheduledPollConsumer(this, processor) {
                            @Override
                            protected int poll() throws Exception {
                                final String response = action.get();
                                if (null != response) {
                                    final Exchange exchange = getEndpoint().createExchange();
                                    exchange.getIn().setBody(response);
                                    getProcessor().process(exchange);
                                    return 1;
                                }
                                return 0;
                            }

                            @Override
                            protected void doStart() throws Exception {
                                super.doStart();
                            }

                            @Override
                            protected void doSuspend() throws Exception {
                                log.info("Consumer was suspended.");
                            }

                            @Override
                            protected void doResume() throws Exception {
                                log.info("Consumer was resumed.");
                            }
                        };
                    }

                    @Override
                    public boolean isSingleton() {
                        return true;
                    }

                    @Override
                    public CamelContext getCamelContext() {
                        return camel;
                    }

                    @Override
                    protected void doSuspend() throws Exception {
                        log.info("Endpoint was suspended.");
                    }

                    @Override
                    protected void doResume() throws Exception {
                        log.info("Endpoint was resumed.");
                    }
                };
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
