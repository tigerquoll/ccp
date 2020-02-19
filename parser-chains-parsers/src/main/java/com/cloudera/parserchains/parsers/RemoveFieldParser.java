package com.cloudera.parserchains.parsers;

import com.cloudera.parserchains.core.AbstractParser;
import com.cloudera.parserchains.core.FieldName;
import com.cloudera.parserchains.core.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A parser which can remove fields from a message.
 */
public class RemoveFieldParser extends AbstractParser {
    private List<FieldName> fieldsToRemove;

    public RemoveFieldParser() {
        fieldsToRemove = new ArrayList<>();
    }

    public RemoveFieldParser removeField(FieldName fieldName) {
        fieldsToRemove.add(fieldName);
        return this;
    }

    @Override
    public void configure(Map<String, Object> config, String input, List<String> outputs) {

    }

    @Override
    public Message parse(Message message) {
        return Message.builder()
                .withFields(message)
                .removeFields(fieldsToRemove)
                .build();
    }

    @Override
    public List<FieldName> outputFields() {
        return Collections.emptyList();
    }

    List<FieldName> getFieldsToRemove() {
        return fieldsToRemove;
    }
}
