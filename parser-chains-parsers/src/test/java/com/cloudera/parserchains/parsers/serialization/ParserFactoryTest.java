package com.cloudera.parserchains.parsers.serialization;

import com.cloudera.parserchains.core.ParserChain;
import com.cloudera.parserchains.core.serialization.ParserFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserFactoryTest {
  @NotNull
  private ParserChain getParserChain(String testFileClassPath) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    Objects.requireNonNull(testFileClassPath);
    try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(testFileClassPath)) {
      return ParserFactory.readFromStream(is);
    }
  }

  @Test
  public void testOuterChain() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
    ParserChain parserChain = getParserChain("testParser.json");
    assertEquals("58485407",parserChain.getId());
    assertEquals( "Dummy Chain A", parserChain.getName() );
    assertEquals( 3, parserChain.getParsers().size());
  }

  @Test
  public void testSimpleParser() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
    ParserChain parserChain = getParserChain("testParser.json");
    assertEquals( "123", parserChain.getParsers().get(0).getId());
    assertEquals( "eventLog", parserChain.getParsers().get(0).getName());
    assertEquals( "DelimitedText", parserChain.getParsers().get(0).getType());
    assertEquals("", parserChain.getParsers().get(0).getInput());
    assertEquals(Collections.emptyList(), parserChain.getParsers().get(0).getOutputs() );
  }

  @Test
  public void testOutputFields() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
    ParserChain parserChain = getParserChain("testParser.json");
    assertEquals( "456", parserChain.getParsers().get(1).getId());
    assertEquals( "Asa", parserChain.getParsers().get(1).getName());
    assertEquals( "Syslog", parserChain.getParsers().get(1).getType());
    assertEquals("", parserChain.getParsers().get(1).getInput());
    assertEquals(Arrays.asList("asa_tag", "asa_bar", "asa_foo"), parserChain.getParsers().get(1).getOutputs() );
  }

  @Test
  public void testParserConfigs() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
    ParserChain parserChain = getParserChain("testParser.json");
    assertEquals( "678", parserChain.getParsers().get(2).getId());
    assertEquals( "My first Router", parserChain.getParsers().get(2).getName());
    assertEquals( "RenameField", parserChain.getParsers().get(2).getType());
    assertEquals("", parserChain.getParsers().get(2).getInput());
    assertEquals(Collections.emptyList(), parserChain.getParsers().get(2).getOutputs() );
    assertEquals("asa_tag", parserChain.getParsers().get(2).getConfig().get("matchingField").toString());
    assertEquals("%ASA-7-609001", ((Map<String,Object>)((List<Object>) parserChain.getParsers().get(2).getConfig().get("routes")).get(0)).get("matchingValue").toString() );
  }


}
