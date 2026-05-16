package kafkacode.springprokmicroservice.pw.kafka.producer;

import com.launchdarkly.eventsource.ConnectStrategy;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import kafkacode.springprokmicroservice.pw.kafka.handler.WikimediaEventChangesHandler;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WikimediaEventChangesProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${stream-wikimedia-url}")
    private String streamWikimediaUrl;

    public WikimediaEventChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage() throws InterruptedException {
        String topic = "wikimedia_info_updates";

        //to read real time stream data from wikimedia, we use event source
        BackgroundEventHandler backgroundEventHandler = new WikimediaEventChangesHandler(kafkaTemplate, topic);

//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .header("User-Agent", "Wikimedia-Client")
                    .build();

            return chain.proceed(request);
        }).build();

        EventSource.Builder builder = new EventSource.Builder(ConnectStrategy.http(URI.create(streamWikimediaUrl))
                .httpClient(okHttpClient));

        BackgroundEventSource backgroundEventSource = new BackgroundEventSource.Builder(backgroundEventHandler,
                builder).build();
        backgroundEventSource.start();

        TimeUnit.MINUTES.sleep(10);
//        TimeUnit.MINUTES.sleep(2);
        backgroundEventSource.close();
    }
}
