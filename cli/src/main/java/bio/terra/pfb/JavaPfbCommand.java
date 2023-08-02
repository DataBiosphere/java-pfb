package bio.terra.pfb;

import static picocli.CommandLine.Command;

import picocli.CommandLine;

@Command(
    name = "pfb",
    mixinStandardHelpOptions = true,
    description = "A java implementation of pyPFB",
    versionProvider = PfbVersion.class)
public class JavaPfbCommand implements Runnable {
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
  }

  // Example "hello world" command
  @Command(name = "hello")
  public void helloCommand() {
    System.out.println("Hello world!");
  }

  // POC usage of java-pfb-library
  @Command(name = "getNumber5")
  public void getNumber5() {
    int val = Library.getNumber5();
    System.out.println("Test pulling from pfb library - The number is " + val);
  }
}
