package bio.terra.pfb;

import static picocli.CommandLine.Command;

import picocli.CommandLine;

@Command(name = "pfb")
public class JavaPfbCommand implements Runnable {
  public static void main(String[] args) {
    CommandLine.run(new JavaPfbCommand(), args);
  }

  @Override
  public void run() {
    System.out.println("A java implementation of pyPFB");
  }

  @Command(name = "hello")
  public void helloCommand() {
    System.out.println("Hello world!");
  }

  @Command(name = "--help")
  public void helpCommand() {
    System.out.println("Help is on its way!! In the next PR...");
  }
}
