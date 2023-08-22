package bio.terra.pfb;

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
  private PfbCommandOption option = PfbCommandOption.tableRows;

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
    System.out.println("PFB RUN");
    if (command != null && option != null) {
      switch (command) {
        case show:
          switch (option) {
            case schema:
              System.out.println("show schema");
              System.out.println("File: " + filePath);
              System.out.println(Library.showSchema(filePath));
              break;
            case tableRows:
              System.out.println("show table rows");
              System.out.println("File: " + filePath);
              System.out.println(Library.showTableRows(filePath));
              break;
            case metadata:
              System.out.println("show metadata");
              System.out.println("File: " + filePath);
              System.out.println(Library.showMetadata(filePath));
              break;
            case nodes:
              System.out.println("show nodes");
              System.out.println("File: " + filePath);
              System.out.println(Library.showNodes(filePath));
              break;
          }
          break;
      }
    }
  }

  enum PfbCommand {
    show
  }

  enum PfbCommandOption {
    schema,
    metadata,
    nodes,
    tableRows,
  }
}
