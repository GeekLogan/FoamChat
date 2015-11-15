
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author Logan Walker <logan.walker@me.com>
 */
public class LogUtilities {

    public static void statLog(ChatLog in) {
        System.err.println("Size of Users: " + in.users.length);
        System.err.println("Size of Messages: " + in.messages.length);
    }
    
    public static void dumpLog(ChatLog in) {
        System.out.println("Users: ");
        for( User a : in.users ) {
            System.out.println( a.displayName );
        }
        System.out.println("Messages: ");
        for( Message a : in.messages ) {
            System.out.println( a.from + " : " + a.message );
        }
    }

    public static void sortFields(ChatLog in) {
        Arrays.sort(in.users, new UserComp());
        Arrays.sort(in.messages, new MessageComp());
    }

    private static class UserComp implements Comparator<User> {

        @Override
        public int compare(User e1, User e2) {
            return e1.id - e2.id;
        }
    }

    private static class MessageComp implements Comparator<Message> {

        @Override
        public int compare(Message e1, Message e2) {
            return (int) (e1.created.getTime() - e2.created.getTime());
        }
    }

    public static int rand() {
        Random rndGenerator = new Random();
        return rndGenerator.nextInt(2147483647);
    }
}
