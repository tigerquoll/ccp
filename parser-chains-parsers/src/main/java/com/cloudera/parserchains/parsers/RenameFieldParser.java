package com.cloudera.parserchains.parsers;

import com.cloudera.parserchains.core.AbstractParser;
import com.cloudera.parserchains.core.FieldName;
import com.cloudera.parserchains.core.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A parser that can rename message fields.
 */
public class RenameFieldParser extends AbstractParser {
    private Map<FieldName, FieldName> fieldsToRename;

    public RenameFieldParser() {
        this.fieldsToRename = new HashMap<>();
    }

    /**
     * Configure the parser to rename a field.
     * @param from The original field name.
     * @param to The new field name.
     */
    public RenameFieldParser renameField(FieldName from, FieldName to) {
        fieldsToRename.put(from, to);
        return this;
    }

    Map<FieldName, FieldName> getFieldsToRename() {
        return Collections.unmodifiableMap(fieldsToRename);
    }

    @Override
    public void configure(Map<String, Object> config, String input, List<String> outputs) {

    }

    @Override
    public Message parse(Message input) {
        Message.Builder output = Message.builder()
                .withFields(input);
        fieldsToRename.forEach((from, to) -> output.renameField(from, to));
        return output.build();
    }

    @Override
    public List<FieldName> outputFields() {
        return new ArrayList<>(fieldsToRename.keySet());
    }
}
