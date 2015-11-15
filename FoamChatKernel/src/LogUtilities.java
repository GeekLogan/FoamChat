
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Logan Walker <logan.walker@me.com>
 */
public class LogUtilities {

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
}
