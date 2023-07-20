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
    int exitCode = new CommandLine(new JavaPfbCommand()).execute(args);
    System.exit(exitCode);
  }

  @Override
  public void run() {
    System.out.println("PFB RUN");
  }

  @Command(name = "hello")
  public void helloCommand() {
    System.out.println("Hello world!");
  }
}
