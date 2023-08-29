package bio.terra.pfb;

import static bio.terra.pfb.JavaPfbCommand.PfbCommand.SHOW;
import static picocli.CommandLine.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@Command(
    name = "pfb",
    mixinStandardHelpOptions = true,
    description = "A java implementation of pyPFB",
    versionProvider = PfbVersion.class)
public class JavaPfbCommand implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(JavaPfbCommand.class);

  @Parameters(arity = "1", index = "0", description = "Command to run (Options include: show)")
  private PfbCommand command;

  // Goal is to follow the same command and option structure as pyPFB
  // In pyPFB, when there isn't a second argument for the "show" command, it defaults to display the
  // table row data. S
  @Parameters(
      arity = "0..1",
      index = "1",
      description =
          "[OPTIONAL] Option to run for given command (Options include: schema, metadata, nodes. If not defined for the show command, the row data will be returned.)",
      defaultValue = "tableRows")
  private PfbCommandOption option;

  @Option(
      names = {"-i", "--input"},
      description = "Input file value")
  private String filePath;

  @Option(
      names = {"-n", "--limit"},
      description =
          "How many records to show, ignored for sub-commands.\n"
              + "                        [default: no limit]",
      defaultValue = "-1")
  private int limit;

  public static void main(String[] args) {
    int exitCode = executeCommand(args);
    System.exit(exitCode);
  }

  static int executeCommand(String[] args) {
    return new CommandLine(new JavaPfbCommand()).execute(args);
  }

  @Override
  public void run() {
    Library library = new Library(new PfbReader());
    logger.info("PFB RUN");
    if (command.equals(SHOW)) {
      switch (option) {
        case SCHEMA:
          logger.info("Show schema for file path: {}", filePath);
          logger.info(library.showSchema(filePath));
          break;
        case TABLE_ROWS:
          if (limit >= 0) {
            logger.info("Show table rows for file path: {}, Limit = {}", filePath, limit);
            logger.info(library.showTableRows(filePath, limit));
          } else {
            logger.info("show table rows for file path: {}", filePath);
            logger.info(library.showTableRows(filePath));
          }
          break;
        case METADATA:
          logger.info("show metadata for file path: {}", filePath);
          logger.info(library.showMetadata(filePath));
          break;
        case NODES:
          logger.info("show nodes for file path: {}", filePath);
          logger.info(library.showNodes(filePath));
          break;
      }
    } else {
      logger.info("Unknown command: {}", command);
    }
  }

  public enum PfbCommand {
    SHOW("show");

    private final String displayName;

    PfbCommand(String displayName) {
      this.displayName = displayName;
    }

    @Override
    public String toString() {
      return this.displayName;
    }
  }

  public enum PfbCommandOption {
    SCHEMA("schema"),
    METADATA("metadata"),
    NODES("nodes"),
    TABLE_ROWS("tableRows");

    private final String displayName;

    PfbCommandOption(String displayName) {
      this.displayName = displayName;
    }

    @Override
    public String toString() {
      return this.displayName;
    }
  }
}
