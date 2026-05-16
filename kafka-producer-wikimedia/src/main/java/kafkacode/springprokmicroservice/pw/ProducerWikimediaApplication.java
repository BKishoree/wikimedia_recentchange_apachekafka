package kafkacode.springprokmicroservice.pw;

import kafkacode.springprokmicroservice.pw.kafka.producer.WikimediaEventChangesProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerWikimediaApplication implements CommandLineRunner {

    @Autowired
    private WikimediaEventChangesProducer wikimediaEventChangesProducer;

    public static void main(String[] args) {
        SpringApplication.run(ProducerWikimediaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        wikimediaEventChangesProducer.sendMessage();
    }
}
