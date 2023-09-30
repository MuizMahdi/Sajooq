package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class ExternalCommandUtils {
    /**
     * Executes an external command
     * @param command
     *          The command to be executed
     *  @throws IOException when there's an error while reading the command
     */
    public static void execute(String command) throws IOException, InterruptedException {
        final Process p = Runtime.getRuntime().exec(command);

        new Thread(() -> {
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            try { while ((line = input.readLine()) != null) System.out.println(line); }
            catch(IOException e) { e.printStackTrace(); }
        }).start();

        p.waitFor();
    }

    private ExternalCommandUtils() {}
}
