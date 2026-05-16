package kafkacode.springprokmicroservice.cd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="wikimedia_recent_info")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Wikimedia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Lob
    private String wikiEventData;
}
