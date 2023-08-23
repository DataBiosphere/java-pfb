package bio.terra.pfb;

import static bio.terra.pfb.JavaPfbCommand.PfbCommand.SHOW;
import static bio.terra.pfb.JavaPfbCommand.PfbCommandOption.TABLE_ROWS;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;
import static picocli.CommandLine.Parameters;

import picocli.CommandLine;

@Command(
    name = "pfb",
    mixinStandardHelpOptions = true,
    description = "A java implementation of pyPFB",
    versionProvider = PfbVersion.class)
public class JavaPfbCommand implements Runnable {

  @Parameters(index = "0", description = "command to run (e.g. Show)")
  private PfbCommand command;

  @Parameters(
      index = "1",
      description = "option to run for given command (e.g. schema)",
      defaultValue = "tableRows")
  private PfbCommandOption option = TABLE_ROWS;

  @Option(
      names = {"-i", "--input"},
      description = "Input file value")
  private String filePath;

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
    System.out.println("PFB RUN");
    if (command != null && option != null) {
      if (command.equals(SHOW)) {
        switch (option) {
          case SCHEMA:
            System.out.println("Show schema for file path: " + filePath);
            System.out.println(library.showSchema(filePath));
            break;
          case TABLE_ROWS:
            System.out.println("show table rows for file path: " + filePath);
            System.out.println(library.showTableRows(filePath));
            break;
          case METADATA:
            System.out.println("show metadata for file path: " + filePath);
            System.out.println(library.showMetadata(filePath));
            break;
          case NODES:
            System.out.println("show nodes for file path: " + filePath);
            System.out.println(library.showNodes(filePath));
            break;
        }
      } else {
        System.out.println("Unknown command: " + command);
      }
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
