package bio.terra.pfb;

import static bio.terra.pfb.models.PfbCommand.SHOW;
import static picocli.CommandLine.*;

import bio.terra.pfb.models.PfbCommand;
import bio.terra.pfb.models.PfbCommandOption;
import bio.terra.pfb.models.commands.*;
import java.io.IOException;
import picocli.CommandLine;

@Command(
    name = "pfb",
    mixinStandardHelpOptions = true,
    description = "A java implementation of pyPFB",
    versionProvider = PfbVersion.class)
public class JavaPfbCommand implements Runnable {

  @Parameters(arity = "1", index = "0", description = "Command to run (Options include: show)")
  private PfbCommand command;

  // Goal is to follow the same command and option structure as pyPFB
  // In pyPFB, when there isn't a second argument for the "show" command, it defaults to display the
  // table row data.
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
    if (command.equals(SHOW)) {
      switch (option) {
        case SCHEMA:
          callPfbLibraryCommand(new ShowSchema(), library, filePath);
          break;
        case TABLE_ROWS:
          callPfbLibraryCommand(new TableRows(), library, filePath, limit);
          break;
        case METADATA:
          callPfbLibraryCommand(new ShowMetadata(), library, filePath);
          break;
        case NODES:
          callPfbLibraryCommand(new ShowNodes(), library, filePath);
          break;
      }
    } else {
      System.err.println("Unknown command: " + command);
    }
  }

  public void callPfbLibraryCommand(
      PfbLibraryCommandInterface command, Library library, String filePath) {
    callPfbLibraryCommand(command, library, filePath, -1);
  }

  public void callPfbLibraryCommand(
      PfbLibraryCommandInterface command, Library library, String filePath, int limit) {
    if (limit >= 0) {
      String result;
      try {
        result = command.commandWithLimit(library, filePath, limit);
        System.out.println(result);
      } catch (IOException e) {
        throw new PicocliException(e.getMessage(), e);
      }
    } else {
      String result;
      try {
        result = command.command(library, filePath);
        System.out.println(result);
      } catch (IOException e) {
        throw new PicocliException(e.getMessage(), e);
      }
    }
  }
}
