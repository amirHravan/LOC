import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


enum Regexes {

    CREATE_TABLE("Create_a_table_for (?<username>\\S+) with_deposit_of (<money>\\d+)"),
    JOIN_WITH_INVITATION("Invitation_request_from (?<oldUsername>\\S+) for (?<newUsername>\\S+) with_deposit_of (?<money>\\d+)"),
    JOIN_INDEPENDENT("Join_request_for (?<username>\\S+) with_deposit_of (?<money>\\d+)"),
    NUMBER_OF_LEVELS("Number_of_levels"),
    NUMBER_OF_USERS("Number_of_users"),
    NUMBER_OF_USERS_IN_LEVEL("Number_of_users_level (?<level>\\d+)"),
    USER_INTRODUCER("Introducer_of (?<username>\\S+)"),
    USER_FRIENDS("Friends_of (?<username>\\S+)"),
    USER_CREDITS("Credit_of (?<username>\\S+)"),
    SAME_LEVEL_USERS("Users_on_the_same_level_with (?<username>\\S+)"),
    PROFIT_UNTIL_NOW("How_much_have_we_made_yet"),
    END("End");
    private final String pattern;

    Regexes(String pattern) {
        this.pattern = pattern;
    }

    public static Matcher getMatcher(String input, Regexes regex) {
        return Pattern.compile(regex.pattern).matcher(input);
    }
}

class User {
    private final User introducer;
    private String username;
    private Level level;
    private int deposit;
    private int position;

    public User(User introducer, String username, Level floor, int deposit, int position) {
        this.introducer = introducer;
        this.username = username;
        this.level = floor;
        this.deposit = deposit;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public User getIntroducer() {
        return introducer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public boolean isFriend(User user,int peopleInLLevel){
        return this.getPosition() == (user.getPosition() + 1) % peopleInLLevel || this.getPosition() == (user.getPosition() + peopleInLLevel - 1) % peopleInLLevel;
    }
}

class Level {
    private int number;
    private int capacity;

    public Level(int number) {
        this.number = number;
        this.capacity = (int) Math.pow(number, 2);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

class Mahdiz {
    private int deposit;

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
}

class FinancialCircle {
    private final ArrayList<User> users;
    private final ArrayList<Level> levels;
    private Mahdiz mahdiz;

    public FinancialCircle() {
        this.users = new ArrayList<>();
        this.levels = new ArrayList<>();

    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public Mahdiz getMahdiz() {
        return mahdiz;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setMahdiz(Mahdiz mahdiz) {
        this.mahdiz = mahdiz;
    }
}

class Controller {
    private final FinancialCircle financialCircle = new FinancialCircle();

    private User getUserByName(String username){
        for (User user : financialCircle.getUsers()) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;

    }


    public String createTable(String username, int deposit) {

    }

    public String joinIndependent(String username, int deposit) {

    }


    public String joinWithInvitation(String oldUsername, String newUsername, int deposit) {

    }

    public int getNumberOfUsers() {
        return financialCircle.getUsers().size();
    }

    public int getNumberOfLevels() {
        return financialCircle.getLevels().size();
    }

    public ArrayList<User> getUsersInSameLevel(int levelNumber) {
        ArrayList<User> users = new ArrayList<>();

        for (User user : financialCircle.getUsers()) {
            if (user.getLevel().getNumber() == levelNumber)
                users.add(user);
        }

        return users;
    }

    public int getMahdizDeposit() {
        return financialCircle.getMahdiz().getDeposit();
    }
    public ArrayList<User> getUserFriends(String username) {
        User targetUser = getUserByName(username);
        ArrayList<User> userFriends = new ArrayList<>();

        for (User user : financialCircle.getUsers()) {
            if (user.getLevel().getNumber() == targetUser.getLevel().getNumber()){
                if (targetUser.isFriend(user,financialCircle.getUsers().size()))
                    userFriends.add(user);
            }
        }
        return userFriends;
    }

    public String getUserIntroducer(String username) {
        User user = getUserByName(username);
        if (user == null)
            return "1";
        if (user.getIntroducer() == null)
            return "2";

        return "3";
    }

    public String getUserDeposit(String username) {
        User user = getUserByName(username);
        if (user == null)
            return "1";

        return String.valueOf(user.getDeposit());
    }
}

class Menu {
    public void run() {
        Controller controller = new Controller();

        String input;
        Scanner scanner = new Scanner(System.in);
        Matcher matcher;

        while (true) {
            input = scanner.nextLine();

            if ((matcher = Regexes.getMatcher(input, Regexes.CREATE_TABLE)).matches()) {
                System.out.println(controller.createTable(matcher.group(1), Integer.parseInt(matcher.group(2))));

            } else if ((matcher = Regexes.getMatcher(input, Regexes.JOIN_INDEPENDENT)).matches()) {
                System.out.println(controller.joinIndependent(matcher.group(1), Integer.parseInt(matcher.group(2))));

            } else if ((matcher = Regexes.getMatcher(input, Regexes.JOIN_WITH_INVITATION)).matches()) {
                System.out.println(controller.joinWithInvitation(matcher.group(1), matcher.group(2), Integer.parseInt(matcher.group(3))));

            } else if ((matcher = Regexes.getMatcher(input, Regexes.NUMBER_OF_USERS)).matches()) {
                System.out.println(controller.getNumberOfUsers());

            } else if ((matcher = Regexes.getMatcher(input, Regexes.NUMBER_OF_LEVELS)).matches()) {
                System.out.println(controller.getNumberOfLevels());

            } else if ((matcher = Regexes.getMatcher(input, Regexes.USER_CREDITS)).matches()) {
                System.out.println(controller.getUserDeposit(matcher.group(1)));

            } else if ((matcher = Regexes.getMatcher(input, Regexes.NUMBER_OF_USERS_IN_LEVEL)).matches()) {
                System.out.println(controller.getUsersInSameLevel(Integer.parseInt(matcher.group(1))).size());

            } else if ((matcher = Regexes.getMatcher(input, Regexes.PROFIT_UNTIL_NOW)).matches()) {
                System.out.println(controller.getMahdizDeposit());

            } else if ((matcher = Regexes.getMatcher(input, Regexes.SAME_LEVEL_USERS)).matches()) {
                if (controller.getUsersInSameLevel(Integer.parseInt(matcher.group(1))).size() == 0)
                    System.out.println("");

                for (User user : controller.getUsersInSameLevel(Integer.parseInt(matcher.group(1)))) {
                    System.out.println(user);
                }

            } else if ((matcher = Regexes.getMatcher(input, Regexes.USER_FRIENDS)).matches()) {
                if (controller.getUserFriends(matcher.group(1)).size() == 0)
                    System.out.println("");

                for (User userFriend : controller.getUserFriends(matcher.group(1))) {
                    System.out.println(userFriend);
                }

            } else if ((matcher = Regexes.getMatcher(input, Regexes.USER_INTRODUCER)).matches()) {
                System.out.println(controller.getUserIntroducer(matcher.group(1)));

            } else if (Regexes.getMatcher(input, Regexes.END).matches()) {
                break;

            } else {
                System.out.println("invalid command!");

            }
        }
    }
}

public class BlockChain {

    public static void main(String[] args) {
        Menu controller = new Menu();
        controller.run();
    }
}
