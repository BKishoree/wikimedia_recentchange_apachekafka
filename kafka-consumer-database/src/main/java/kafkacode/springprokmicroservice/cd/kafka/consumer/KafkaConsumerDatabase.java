package kafkacode.springprokmicroservice.cd.kafka.consumer;

import kafkacode.springprokmicroservice.cd.entity.Wikimedia;
import kafkacode.springprokmicroservice.cd.repository.WikimediaEventDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerDatabase {


   private final WikimediaEventDataRepository wikimediaEventDataRepository;

   public KafkaConsumerDatabase(WikimediaEventDataRepository wikimediaEventDataRepository) {
       this.wikimediaEventDataRepository = wikimediaEventDataRepository;
   }

    @KafkaListener(topics = "wikimedia_info_updates", groupId = "myGroup")
    public void consume(String eventMessage) {
        log.info("Receive message from topic: {}", eventMessage);

        Wikimedia wikimedia = new Wikimedia();
        wikimedia.setWikiEventData(eventMessage);

        wikimediaEventDataRepository.save(wikimedia);
    }
}
