package com.cloudera.parserchains.core.serialization;

import com.cloudera.parserchains.core.MutableParser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Module used to construct ParserChain objects from their JSON representations
 * Parser types are considered class names of the following format
 * com.cloudera.parserchains.parsers.<PARSER_TYPE_NAME>Parser
 * If this fails then the parser name is considered to be the full classpath of the parser object.
 */
public class ParserFactory {
  /**
   * Parser types can be considered classnames under this package
   */
  private static final String DEFAULT_PARSER_PACKAGE = "com.cloudera.parserchains.parsers";

  /**
   * Converts JSON data into a configured parserchain object
   * @param is input stream containing JSON data representing a ParserChain object.
   * @return Instantiated ParserChain Object
   * @throws IOException Steam read error
   * @throws ClassNotFoundException If parser type cannot be mapped to a class type
   * @throws NoSuchMethodException Parser type does not have a no-arg constructor
   * @throws IllegalAccessException Parser type does not have a public no-arg constructor
   * @throws InvocationTargetException Parser type no-arg constructor throws an argument
   * @throws InstantiationException Parser type is not creatable by design
   */
  public static com.cloudera.parserchains.core.ParserChain readFromStream(InputStream is) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
    ParserChain parserChain = readRawFromStream(Objects.requireNonNull(is));
    return hydrateParserChain(parserChain);
  }

  /**
   * Converts a JSON file into a parserchain object
   * @param path Path pointing to JSON file containing a single ParserChain object represented in JSON
   * @return Instantiated ParserChain Object
   * @throws IOException Steam read error
   * @throws ClassNotFoundException If parser type cannot be mapped to a class type
   * @throws NoSuchMethodException Parser type does not have a no-arg constructor
   * @throws IllegalAccessException Parser type does not have a public no-arg constructor
   * @throws InvocationTargetException Parser type no-arg constructor throws an argument
   * @throws InstantiationException Parser type is not creatable by design
   */
  public static com.cloudera.parserchains.core.ParserChain readFromFile(Path path) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    try (InputStream is = Objects.requireNonNull(Files.newInputStream(path))) {
      return readFromStream(is);
    }
  }

  /**
   * Reads JSON into placeholder objects ready to initialise into a placeholder parser chain object
   * @param is Stream to readin
   * @return placeholder ParserChain object
   * @throws IOException JSON read error
   */
  private static ParserChain readRawFromStream(InputStream is) throws IOException {
    return JSONUtils.INSTANCE.load(Objects.requireNonNull(is), ParserChain.class);
  }

  /**
   * Reads raw JSON
   * @param path location of file to read in
   * @return placeholder ParserChain object
   * @throws IOException JSON read error
   */
  private static ParserChain readRawFromFile(final Path path) throws IOException {
    try (InputStream is = Files.newInputStream(path)) {
      return readRawFromStream(is);
    }
  }

  /**
   * Creates a parser object based on the Classname
   * Will attempt to treat the parser type (prepended with "Parser" as a class in the DEFAULT_PARSER_PACKAGE package
   * If this fails, treat the parser type as a full classpath
   * @param parserType name of the parser to map intoa class
   * @return Constructed parser object
   * @throws ClassNotFoundException if parserType cannot be mapped to a classname
   */
  private static Class<?> getParserClass(final String parserType) throws ClassNotFoundException {
    Class<?> aClass;
    try {
      aClass = Class.forName(DEFAULT_PARSER_PACKAGE + "." + parserType + "Parser");
    } catch (ClassNotFoundException e) {
      aClass = Class.forName(parserType);
    }
    return aClass;
  }

  /**
   * Constructs a new Parser Object
   * @param parserType name of the type of parser to create
   * @return Constructed parser object
   * @throws ClassNotFoundException If parser type cannot be mapped to a class type
   * @throws NoSuchMethodException Parser type does not have a no-arg constructor
   * @throws IllegalAccessException Parser type does not have a public no-arg constructor
   * @throws InvocationTargetException Parser type no-arg constructor throws an argument
   * @throws InstantiationException Parser type is not creatable by design
   */
  private static com.cloudera.parserchains.core.MutableParser constructNewParser(final String parserType) throws
          ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    final Class<?> parserClass = Objects.requireNonNull(getParserClass(parserType));
    final Constructor<?> constructor = Objects.requireNonNull(parserClass.getConstructor());
    return (com.cloudera.parserchains.core.MutableParser) constructor.newInstance();
  }

  /**
   * Converts a placeholder Parser Object into an initialised parser object
   * @param parser placeholder object direct from json
   * @return an initialised Parser Object
   * @throws ClassNotFoundException If parser type cannot be mapped to a class type
   * @throws NoSuchMethodException Parser type does not have a no-arg constructor
   * @throws IllegalAccessException Parser type does not have a public no-arg constructor
   * @throws InvocationTargetException Parser type no-arg constructor throws an argument
   * @throws InstantiationException Parser type is not creatable by design
   */
  private static com.cloudera.parserchains.core.Parser hydrateParser(final Parser parser) throws NoSuchMethodException,
          ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
    final MutableParser mutableParser = Objects.requireNonNull(constructNewParser(parser.getType()));
    mutableParser.setId( parser.getId() );
    mutableParser.setInput( parser.getInput());
    mutableParser.setName( parser.getName() );
    mutableParser.setOutputs( parser.getOutputs() );
    mutableParser.setType( parser.getType() );
    mutableParser.setConfig( parser.getConfig() );
    mutableParser.configure(parser.getConfig(), parser.getInput(), parser.getOutputs());
    return mutableParser.getParser();
  }

  /**
   * Converts a placeholder Parser Chain Object into an initialised parser chain object
   * @param parserChain placeholder object direct from json
   * @return an initialised parserChain Object
   * @throws ClassNotFoundException If parser type cannot be mapped to a class type
   * @throws NoSuchMethodException Parser type does not have a no-arg constructor
   * @throws IllegalAccessException Parser type does not have a public no-arg constructor
   * @throws InvocationTargetException Parser type no-arg constructor throws an argument
   * @throws InstantiationException Parser type is not creatable by design
   */
  private static com.cloudera.parserchains.core.ParserChain hydrateParserChain(final ParserChain parserChain) throws
          ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    final com.cloudera.parserchains.core.ParserChain parserChainImpl = new com.cloudera.parserchains.core.ParserChain();
    parserChainImpl.setId( parserChain.getId() );
    parserChainImpl.setName( parserChain.getName() );

    List<com.cloudera.parserchains.core.Parser> parserImplList = new ArrayList<>();
    for(Parser parser: parserChain.getParsers()) {
      // could throw exceptions
      parserImplList.add( hydrateParser(parser) );
    }
    parserChainImpl.setParsers(parserImplList);
    return parserChainImpl;
  }
}
