package com.cloudera.parserchains.core;

import java.util.List;
import java.util.Map;

/**
 * Used when constructing a Parser Object
 */
public interface MutableParser {
  void configure(Map<String, Object> config, String input, List<String> outputs);

  public void setId(String id);
  public void setName(String name);
  public void setConfig(Map<String, Object> config);
  public void setInput(String input);
  public void setOutputs(List<String> outputs);
  public void setType(String type);

  public Parser getParser();
}
