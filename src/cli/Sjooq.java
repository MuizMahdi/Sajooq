///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.5.0

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.*;
import java.util.concurrent.Callable;


@Command(
    name = "sjooq", 
    mixinStandardHelpOptions = true, 
    version = "SJOOQ Cli 0.1",
    description = "A CLI with a set of utilities for managing Spring applications using Hibernate, Liquibase, and JOOQ")
class Sjooq implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Changelog, Entity, or Database which Sjooq will operate on" 
    )
    private String operand;


    @Parameters(
        index = "1",
        description = "The process that will be executed on the operand, e.g. add or update"
    )
    private String operation;


    @Option(
        names = {"-c", "--changelog"},
        description = "Name of the changelog",
        required = false
    )
    private String changelog;


    @Option(
        names = {"-e", "--entity"},
        description = "Name of the entity",
        required = false
    )
    private String entity;


    public static void main(String... args) {
        int exitCode = new CommandLine(new Sjooq()).execute(args);
        System.exit(exitCode);
    }


    private boolean isChangelog, isEntity, isDatabase, isAdding, isUpdating;


    @Override
    public Integer call() throws Exception {

        // Initialize
        initializeProcesses();

        // Validate
        if (!areProcessesValid()) return 1;

        // Run
        runProccesses();

        // End
        return 0;

    }


    private void initializeProcesses() {
        isChangelog = operand.equals("changelog");
        isEntity = operand.equals("entity");
        isDatabase = operand.equals("database");
        isAdding = operation.equals("add");
        isUpdating = operation.equals("update");
    }


    private boolean areProcessesValid() {

        // Validate operands
        if ((!isChangelog) && (!isEntity) && (!isDatabase)) {
            printlnAnsi("@|red Invalid operand, only changelog, entity, and database operands allowed|@");
            return false;
        }

        // Validate operations
        if (!isAdding && !isUpdating) {
            printlnAnsi("@|red Invalid operation, only add, and update operations allowed|@");
            return false;
        }

        // Validate changelog operations
        if (isChangelog && !isAdding) {
            printlnAnsi("@|red Invalid operation, only add operation is supported for changelog|@");
            return false;
        }

        // Validate entity operations
        if (isEntity && !isUpdating) {
            printlnAnsi("@|red Invalid operation, only update operation is supported for entity|@");
            return false;
        }

        // Validate database operations
        if (isDatabase && !isUpdating) {
            printlnAnsi("@|red Invalid operation, only update operation is supported for database|@");
            return false;
        }

        return true;

    }


    private void runProccesses() throws Exception {

        // Check for changelog generation from database
        if (isChangelog && changelog != null && !changelog.isEmpty()) {
            generateChangelogFromDb();
        }

        // Check for changelog generation from entity
        if (isChangelog && entity != null && !entity.isEmpty()) {
            generateChangelogFromEntity();
        }

        // Check for entity generation/update
        if (isEntity && changelog != null && !changelog.isEmpty()) {
            // TODO: --
        }

        // Check for database update
        if (isDatabase && changelog != null && !changelog.isEmpty()) {
            // TODO: --
        }

        RunCommand("mvn clean install");

    }


    private void generateChangelogFromDb() {
        
    }

    
    private void generateChangelogFromEntity() {

    }


    private void RunCommand(String command) throws Exception {

        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        final Process p = Runtime.getRuntime().exec(command);

        new Thread(new Runnable(){
            public void run() {
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                try { while ((line = input.readLine()) != null) System.out.println(line); } 
                catch(IOException ex) { ex.printStackTrace(); }
            } 
        }).start();

        p.waitFor();

    }


    private void printlnAnsi(String msg) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string(msg));
    }

}
