package bio.terra.pfb;

import static bio.terra.pfb.models.PfbCommand.SHOW;
import static picocli.CommandLine.*;

import bio.terra.pfb.models.PfbCommand;
import bio.terra.pfb.models.PfbCommandOption;
import bio.terra.pfb.models.commands.*;
import java.io.IOException;
import picocli.CommandLine;

// Sonar complains about the use of System.out.println, but it seems right the right logging choice
// for a CLI
@SuppressWarnings("java:S106")
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
    if (command.equals(SHOW)) {
      switch (option) {
        case SCHEMA:
          callPfbLibraryCommand(new ShowSchema(), filePath);
          break;
        case TABLE_ROWS:
          callPfbLibraryCommand(new TableRows(), filePath, limit);
          break;
        case METADATA:
          callPfbLibraryCommand(new ShowMetadata(), filePath);
          break;
        case NODES:
          callPfbLibraryCommand(new ShowNodes(), filePath);
          break;
      }
    } else {
      System.err.println("Unknown command: " + command);
    }
  }

  public void callPfbLibraryCommand(PfbLibraryCommandInterface command, String filePath) {
    callPfbLibraryCommand(command, filePath, -1);
  }

  public void callPfbLibraryCommand(
      PfbLibraryCommandInterface command, String filePath, int limit) {
    if (limit >= 0) {
      String result;
      try {
        result = command.commandWithLimit(filePath, limit);
        System.out.println(result);
      } catch (IOException e) {
        throw new PicocliException(e.getMessage(), e);
      }
    } else {
      String result;
      try {
        result = command.command(filePath);
        System.out.println(result);
      } catch (IOException e) {
        throw new PicocliException(e.getMessage(), e);
      }
    }
  }
}
