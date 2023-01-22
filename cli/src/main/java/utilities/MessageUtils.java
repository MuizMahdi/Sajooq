package utilities;

import picocli.CommandLine;

public class MessageUtils {
    /**
     *
     * @param message
     * @return
     */
    public static String getErrorMessage(String message) {
        return CommandLine.Help.Ansi.AUTO.string("@|red " + message + "|@");
    }
}
