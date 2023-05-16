package com.sgyj.userservice.dto.kafka;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaAccountDto implements Serializable {

    private Schema schema;
    private Payload payload;

    public static KafkaAccountDto of(Schema schema, Payload payload) {
        return new KafkaAccountDto(schema, payload);
    }

}
