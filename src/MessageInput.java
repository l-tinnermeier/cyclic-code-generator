public class MessageInput {

    public static String message = "";

    public static boolean validMessage(String input) {

        if (input.matches("[01]{11}")) {

            message = input;
            return true;
        }

        return false;
    }
}