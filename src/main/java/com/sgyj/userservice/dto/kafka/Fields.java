package com.sgyj.userservice.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Fields {

    private String type;
    private boolean optional;
    private String field;

}
