package com.cloudera.parserchains.core.serialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Used for reading in Parser JSON data
 */
public class Parser {
  private String id;
  private String name;
  private String type;
  private Map<String, Object> config;
  private String input;
  private List<String> outputs;

  public Parser() {
    config = new HashMap<>();
    outputs = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, Object> getConfig() {
    return config;
  }

  public void setConfig(Map<String, Object> config) {
    this.config = config;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public List<String> getOutputs() {
    return outputs;
  }

  public void setOutputs(List<String> outputs) {
    this.outputs = outputs;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Parser parser = (Parser) o;
    return Objects.equals(getId(), parser.getId()) &&
            Objects.equals(getName(), parser.getName()) &&
            Objects.equals(getType(), parser.getType()) &&
            Objects.equals(getConfig(), parser.getConfig()) &&
            Objects.equals(getInput(), parser.getInput()) &&
            Objects.equals(getOutputs(), parser.getOutputs());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getType(), getConfig(), getInput(), getOutputs());
  }

  @Override
  public String toString() {
    return "Parser{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", type='" + type + '\'' +
            ", config=" + config +
            ", input=" + input +
            ", outputs=" + outputs +
            '}';
  }
}
