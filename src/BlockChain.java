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
    private Level floor;
    private int deposit;
    private int position;

    public User(User introducer, String username, Level floor, int deposit, int position) {
        this.introducer = introducer;
        this.username = username;
        this.floor = floor;
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

    public Level getFloor() {
        return floor;
    }

    public void setFloor(Level floor) {
        this.floor = floor;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
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
    private Mahdiz mahdiz;

    public FinancialCircle() {
        this.users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}

class Controller {
    private final FinancialCircle financialCircle = new FinancialCircle();

    public void run() {
        String input;
        Scanner scanner = new Scanner(System.in);
        Matcher matcher;

        while (true) {
            input = scanner.nextLine();

            if (Regexes.getMatcher(input, Regexes.CREATE_TABLE).matches()) {

            } else if (Regexes.getMatcher(input, Regexes.JOIN_INDEPENDENT).matches()) {

            } else if (Regexes.getMatcher(input, Regexes.JOIN_WITH_INVITATION).matches()) {

            } else if (Regexes.getMatcher(input, Regexes.NUMBER_OF_USERS).matches()) {

            } else if (Regexes.getMatcher(input, Regexes.NUMBER_OF_LEVELS).matches()) {

            } else if (Regexes.getMatcher(input, Regexes.NUMBER_OF_USERS_IN_LEVEL).matches()) {

            } else if (Regexes.getMatcher(input, Regexes.PROFIT_UNTIL_NOW).matches()) {

            } else if (Regexes.getMatcher(input, Regexes.SAME_LEVEL_USERS).matches()) {

            } else if (Regexes.getMatcher(input, Regexes.USER_FRIENDS).matches()) {

            } else if (Regexes.getMatcher(input, Regexes.USER_INTRODUCER).matches()) {

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
        Controller controller = new Controller();
        controller.run();
    }
}
