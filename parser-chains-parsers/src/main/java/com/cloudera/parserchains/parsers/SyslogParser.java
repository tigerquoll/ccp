package com.cloudera.parserchains.parsers;

import com.cloudera.parserchains.core.AbstractParser;
import com.cloudera.parserchains.core.FieldName;
import com.cloudera.parserchains.core.FieldValue;
import com.cloudera.parserchains.core.Message;
import com.github.palindromicity.syslog.SyslogParserBuilder;
import com.github.palindromicity.syslog.SyslogSpecification;
import com.github.palindromicity.syslog.dsl.ParseException;
import com.github.palindromicity.syslog.dsl.SyslogFieldKeys;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

public class SyslogParser extends AbstractParser {

    private FieldName inputField;
    private SyslogSpecification specification;

    public SyslogParser() {
        this.specification = SyslogSpecification.RFC_5424;
    }

    public SyslogParser withSpecification(SyslogSpecification specification) {
        this.specification = Objects.requireNonNull(specification);
        return this;
    }

    public SyslogParser withInputField(FieldName inputField) {
        this.inputField = Objects.requireNonNull(inputField);
        return this;
    }

    @Override
    public void configure(Map<String, Object> config, String input, List<String> outputs) {

    }

    @Override
    public Message parse(Message input) {
        if(inputField == null) {
            throw new IllegalStateException("Input field has not been defined.");
        }
        Message.Builder output = Message.builder().withFields(input);
        Optional<FieldValue> value = input.getField(inputField);
        if(value.isPresent()) {
            doParse(output, value.get().toString());

        } else {
            output.withError(format("Message does not contain input field '%s'", inputField.toString()));
        }

        return output.build();
    }

    private void doParse(Message.Builder output, String value) {
        try {
            new SyslogParserBuilder()
                    .forSpecification(specification)
                    .build()
                    .parseLine(value)
                    .forEach((k, v) -> output.addField(FieldName.of(k), FieldValue.of(v.toString())));

        } catch(ParseException e) {
            output.withError(e);
        }
    }

    @Override
    public List<FieldName> outputFields() {
        if(SyslogSpecification.RFC_3164.equals(specification)) {
            return Arrays.asList(
                    FieldName.of(SyslogFieldKeys.MESSAGE.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_HOSTNAME.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_PRI.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_PRI_SEVERITY.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_PRI_FACILITY.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_TIMESTAMP.getField()));

        } else if (SyslogSpecification.RFC_5424.equals(specification)) {
            return Arrays.asList(
                    FieldName.of(SyslogFieldKeys.MESSAGE.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_APPNAME.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_HOSTNAME.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_PRI.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_PRI_SEVERITY.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_PRI_FACILITY.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_PROCID.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_TIMESTAMP.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_MSGID.getField()),
                    FieldName.of(SyslogFieldKeys.HEADER_VERSION.getField()));
        } else {
            throw new IllegalArgumentException("Unexpected specification: " + specification);
        }
    }
}
