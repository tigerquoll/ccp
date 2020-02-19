package com.cloudera.parserchains.core;

import java.util.List;
import java.util.Map;

/**
 * Parses a {@link Message}.
 *
 */
public interface Parser {
    public String getId();
    public String getName();
    public Map<String, Object> getConfig();
    public String getInput();
    public List<String> getOutputs();
    public String getType();

    /**
     * Parse a {@link Message}.
     * @param message The message to parse.
     * @return A parsed message.
     */
    Message parse(Message message);

    /**
     * Returns the known output fields added to all parsed messages.  Not all parsers
     * are able to declare their known output fields.
     */
    List<FieldName> outputFields();

    
}
