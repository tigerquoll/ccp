package com.cloudera.parserchains.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A Parser that does nothing.
 */
public class NoopParser extends AbstractParser {

    @Override
    public void configure(Map<String, Object> config, String input, List<String> outputs) {

    }

    @Override
    public Message parse(Message message) {
        // do nothing
        return message;
    }

    @Override
    public List<FieldName> outputFields() {
        // do nothing
        return Collections.emptyList();
    }
}
