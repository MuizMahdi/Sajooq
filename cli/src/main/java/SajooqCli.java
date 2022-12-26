///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.5.0

import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.*;
import java.util.concurrent.Callable;


@Command(
    name = "sajooq", 
    mixinStandardHelpOptions = true, 
    version = "SAJOOQ Cli 0.1",
    description = "A CLI with a set of commands for managing entities lifecycle using Hibernate, Liquibase, and JOOQ")
class SajooqCli implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Changelog, Entity, or Database which Sajooq will operate on" 
    )
    private String operand;

    @Parameters(
        index = "1",
        description = "The process that will be executed on the operand, e.g. generate or update"
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

    @Option(
        names = {"-n", "--db-name"},
        description = "Database name",
        required = false
    )
    private String dbName;

    @Option(
        names = {"-u", "--db-user"},
        description = "Database user",
        required = false
    )
    private String dbUser;

    @Option(
        names = {"-p", "--db-password"},
        description = "Database user's password",
        required = false
    )
    private String dbPassword;

    private boolean isChangelog, isEntity, isDatabase, isGenerating, isUpdating;

    /** 
     * Initializes CLI
     * @param args
     */
    public static void main(String... args) {
        System.exit(new CommandLine(new SajooqCli()).execute(args));
    }

    /** 
     * Runs CLI command
     * @return Integer
     * @throws Exception
     */
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
        isGenerating = operation.equals("generate");
        isUpdating = operation.equals("update");
    }

    /** 
     * Checks if all CLI operands and their operations are valid for execution
     * @return boolean
     */
    private boolean areProcessesValid() {
        // Validate operands
        if ((!isChangelog) && (!isEntity) && (!isDatabase)) {
            printError("Invalid operand, only 'changelog', 'entity', and 'database' operands allowed");
            return false;
        }

        // Validate operations
        if (!isGenerating && !isUpdating) {
            printError("Invalid operation, only 'generate', and 'update' operations allowed");
            return false;
        }

        // Validate entity operations
        if (isEntity && !isGenerating) {
            printError("Invalid operation, only 'generate' operation is supported for entity");
            return false;
        }

        // Validate database operations
        if (isDatabase && !isUpdating) {
            printError("Invalid operation, only 'update' operation is supported for database");
            return false;
        }

        return true;
    }

    /**
     * Checks if Database CLI options are entered and valid for execution
     * @return
     */
    private boolean areDbCredentialsValid() {
        if (StringUtils.isAnyEmpty(dbName, dbUser, dbPassword)) {
            printError("Invalid database credentials, please enter database name, user, and password");
            return false;
        }

        return true;
    }

    /** 
     * Runs a process according to the entered operands and operations
     * @throws Exception
     */
    private void runProccesses() throws Exception {
        // Check for changelog generation from database
        if (isChangelog && isGenerating && areDbCredentialsValid()) {
            generateChangelogFromDb();
        }

        // Check for changelog generation from entity
        if (isChangelog && entity != null && !entity.isEmpty()) {
            generateChangelogFromEntity();
        }

        // Check for entity generation/update
        if (isEntity && changelog != null && !changelog.isEmpty()) {
            generateEntityfromChangelog();
        }

        // Check for database update
        if (isDatabase && changelog != null && !changelog.isEmpty()) {
            updateDatabaseFromChangelog();
        }

        RunCommand("mvn clean install");
    }

    /**
     * Generates changelog from main database
     * @throws Exception
     */
    private void generateChangelogFromDb() throws Exception {
        RunCommand("mvn liquibase:generateChangeLog");
    }

    /**
     * Generates and updates entities from changelog
     * @throws Exception
     */
    private void generateEntityfromChangelog() throws Exception {
        RunCommand("mvn org.jooq:jooq-codegen-maven:3.15.1:generate@generate-entity");
    }

    /**
     * Generates changelog from JPA entities, this is done
     * using JOOQ's JPADatabase code generator, which:
     * 1. Uses Spring to find all JPA annotated entities in classpath,
     * 2. Creates DB tables for these entities in a local H2 DB,
     * 3. Generates the JOOQ metamodel classes from the difference
     * between the local H2 DB and the production DB.
     * @throws Exception
     */
    private void generateChangelogFromEntity() throws Exception {
        RunCommand("");
    }

    /**
     * Updates the main DB using liquibase changelogs
     * @throws Exception
     */
    private void updateDatabaseFromChangelog() throws Exception {
        RunCommand("mvn liquibase:update");
    }

    /**
     * @param command
     * @throws Exception
     */
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

    /**
     * Prints error message
     * @param message message to be printed
     */
    private void printError(String message) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|red " + message + "|@"));
    }
}
