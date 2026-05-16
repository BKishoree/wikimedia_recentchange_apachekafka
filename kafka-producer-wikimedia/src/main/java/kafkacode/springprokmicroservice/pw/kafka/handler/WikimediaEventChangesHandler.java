package kafkacode.springprokmicroservice.pw.kafka.handler;

import com.launchdarkly.eventsource.MessageEvent;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class WikimediaEventChangesHandler implements BackgroundEventHandler {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public WikimediaEventChangesHandler(KafkaTemplate<String, String> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void onOpen() throws Exception {
        log.info("OPENED CONNECTION");
    }

    @Override
    public void onClosed() throws Exception {
        log.info("CONNECTION CLOSED");
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) throws Exception {
        log.info("EVENT RECEIVED");
        log.info("Event data {}", messageEvent.getData());
        kafkaTemplate.send(topic, messageEvent.getData());
    }

    @Override
    public void onComment(String comment) throws Exception {

    }

    @Override
    public void onError(Throwable t) {
        log.error("ERROR OCCURRED", t);
    }
}
