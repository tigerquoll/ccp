package com.cloudera.parserchains.core.serialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Used for reading in ParserChain JSON data
 */
public class ParserChain {
  private String id;
  private String name;
  private List<Parser> parsers;

  public ParserChain() {
    parsers = new ArrayList<Parser>();
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

  public List<Parser> getParsers() {
    return parsers;
  }

  public void setParsers(List<Parser> parsers) {
    this.parsers = parsers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ParserChain that = (ParserChain) o;
    return Objects.equals(getId(), that.getId()) &&
            Objects.equals(getName(), that.getName()) &&
            Objects.equals(getParsers(), that.getParsers());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getParsers());
  }

  @Override
  public String toString() {
    return "ParserChain{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", parsers=" + parsers +
            '}';
  }
}
