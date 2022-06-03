import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


enum Regexes {

    CREATE_TABLE("Create_a_table_for (?<username>\\S+) with_deposit_of (?<money>\\d+)"),
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
    private int deposit;
    private int position;
    private int levelNumber;
    private int introducedPeopleCount;

    public User(User introducer, String username, int levelNumber, int deposit, int position) {
        this.introducer = introducer;
        this.username = username;
        this.levelNumber = levelNumber;
        this.deposit = deposit;
        this.position = position;
        this.introducedPeopleCount = 0;
    }

    public int getIntroducedPeopleCount() {
        return introducedPeopleCount;
    }

    public void setIntroducedPeopleCount(int introducedPeopleCount) {
        this.introducedPeopleCount = introducedPeopleCount;
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

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public boolean isFriend(User user, int peopleInLLevel) {
        return this.getPosition() == (user.getPosition() + 1) % peopleInLLevel || this.getPosition() == (user.getPosition() + peopleInLLevel - 1) % peopleInLLevel;
    }

    public void sendUserHigher(ArrayList<Level> levels) {
        Comparator<User> comparator = Comparator.comparing(User::getIntroducedPeopleCount).thenComparing(User::getDeposit);
        ArrayList<User> sortedUsers = new ArrayList<>(levels.get(levelNumber - 1).getUsers());
        sortedUsers.sort(comparator);
        User miserableUser = sortedUsers.get(0);

        this.introducedPeopleCount = 0;
        miserableUser.setIntroducedPeopleCount(0);

        int position = miserableUser.getPosition();
        miserableUser.setPosition(this.position);
        this.position = position;

        miserableUser.setLevelNumber(levelNumber);
        this.levelNumber--;

        levels.get(levelNumber - 1).getUsers().remove(miserableUser);
        levels.get(levelNumber).getUsers().remove(this);

        levels.get(levelNumber - 1).getUsers().add(this);
        levels.get(levelNumber).getUsers().add(miserableUser);

    }
}

class Level {
    private final ArrayList<User> users;
    private int number;
    private int capacity;

    public Level(int number) {
        this.users = new ArrayList<>();
        this.number = number;
        this.capacity = (int) Math.pow(number, 2);
    }


    public ArrayList<User> getUsers() {
        return users;
    }

    public int getPeopleCount() {
        return this.users.size();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean hasCapacity() {
        return this.capacity - this.users.size() > 0;
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
    private final ArrayList<Level> levels;
    private Mahdiz mahdiz;

    private User founder;

    public FinancialCircle() {
        this.levels = new ArrayList<>();
        this.founder = null;
        this.mahdiz = new Mahdiz();
    }

    public User getFounder() {
        return founder;
    }

    public void setFounder(User founder) {
        this.founder = founder;
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public Mahdiz getMahdiz() {
        return mahdiz;
    }

    public void setMahdiz(Mahdiz mahdiz) {
        this.mahdiz = mahdiz;
    }


    public void addNewLevel() {
        Level level = new Level(levels.size());
        levels.add(level);
    }

    public String joinRequest(String username, int deposit) {
        Level level = new Level(levels.size());
        level.getUsers().add(new User(null, username, level.getNumber(), (int) (deposit * 0.15), 0));
        founder.setDeposit((int) (founder.getDeposit() + deposit * 0.1));
        mahdiz.setDeposit((int) (mahdiz.getDeposit() + deposit * 0.25));

        for (int i = 1; i < level.getNumber(); i++) {
            for (User user : levels.get(i).getUsers()) {
                user.setDeposit((int) (user.getDeposit() + (deposit / ((level.getNumber() - 1) * (level.getPeopleCount())))));
            }
        }
        return "User added successfully in level " + level.getNumber();
    }

    public String invitePeople(String username, User oldUser, int deposit) {
        oldUser.setIntroducedPeopleCount(oldUser.getIntroducedPeopleCount() + 1);
        Level level = new Level(-1);
        for (int i = oldUser.getLevelNumber() + 1; i < levels.size(); i++) {
            if (levels.get(i).hasCapacity())
                level = levels.get(i);
        }
        if (level.getNumber() == -1){
            level = new Level(levels.size());
            levels.add(level);
        }

        User newUser = new User(oldUser, username, level.getNumber(), (int) (deposit * 0.2), level.getPeopleCount());

        oldUser.setDeposit((int) (oldUser.getDeposit() + deposit * 0.05));
        mahdiz.setDeposit((int) (mahdiz.getDeposit() + deposit * 0.15));
        founder.setDeposit((int) (founder.getDeposit() + deposit * 0.1));
        level.getUsers().add(newUser);

        for (int i = 1; i < oldUser.getLevelNumber(); i++) {
            for (User user : levels.get(i).getUsers()) {
                user.setDeposit((int) (user.getDeposit() + (deposit / ((level.getNumber() - 1) * (level.getPeopleCount())))));
            }
        }

        if (oldUser.getIntroducedPeopleCount() > 5)
            oldUser.sendUserHigher(levels);

        return "User added successfully in level " + level.getNumber();
    }
}

class Controller {
    private final FinancialCircle financialCircle = new FinancialCircle();

    private User getUserByName(String username) {
        for (Level level : financialCircle.getLevels()) {
            for (User user : level.getUsers()) {
                if (user.getUsername().equals(username))
                    return user;
            }
        }
        return null;
    }


    public String createTable(String username, int deposit) {
        if (financialCircle.getFounder() != null)
            return "We already have a founder";

        if (deposit < 5000)
            return "Money is not enough";

        User user = new User(null, username, 0, deposit - 5000, 0);
        financialCircle.addNewLevel();
        financialCircle.getLevels().get(0).getUsers().add(user);
        financialCircle.setFounder(user);

        return "You now own a table";
    }

    public String joinIndependent(String username, int deposit) {
        if (getUserByName(username) != null)
            return "Username already taken";


        return financialCircle.joinRequest(username, deposit);
    }


    public String joinWithInvitation(String oldUsername, String newUsername, int deposit) {
        User oldUser;

        if ((oldUser = getUserByName(oldUsername)) == null)
            return "";

        if (getUserByName(newUsername) != null)
            return "Username already taken";

        return (financialCircle.invitePeople(newUsername, oldUser, deposit));
    }

    public int getNumberOfUsers() {
        int count = 0;
        for (Level level : financialCircle.getLevels()) {
            count += level.getUsers().size();
        }

        return count;
    }

    public int getNumberOfLevels() {
        return financialCircle.getLevels().size();
    }

    public ArrayList<User> getUsersInSameLevel(int levelNumber) {
        return financialCircle.getLevels().get(levelNumber).getUsers();
    }

    public int getMahdizDeposit() {
        return financialCircle.getMahdiz().getDeposit();
    }

    public ArrayList<User> getUserFriends(String username) {
        User targetUser = getUserByName(username);

        ArrayList<User> userFriends = new ArrayList<>();

        if (targetUser == null)
            return null;

        for (User user : financialCircle.getLevels().get(targetUser.getLevelNumber()).getUsers()) {
            if (targetUser.isFriend(user, financialCircle.getLevels().get(targetUser.getLevelNumber()).getPeopleCount()))
                userFriends.add(user);
        }

        return userFriends;
    }

    public String getUserIntroducer(String username) {
        User user = getUserByName(username);

        if (user == null)
            return "No_such_user_found";
        if (user.getIntroducer() == null)
            return "No_introducer";

        return user.getIntroducer().getUsername();
    }

    public String getUserDeposit(String username) {
        User user = getUserByName(username);
        if (user == null)
            return "No_such_user_found";

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
                if (controller.getUsersInSameLevel(Integer.parseInt(matcher.group(1))) == null)
                    System.out.println("No_such_level_found");

                System.out.println(controller.getUsersInSameLevel(Integer.parseInt(matcher.group(1))).size());

            } else if ((matcher = Regexes.getMatcher(input, Regexes.PROFIT_UNTIL_NOW)).matches()) {
                System.out.println(controller.getMahdizDeposit());

            } else if ((matcher = Regexes.getMatcher(input, Regexes.SAME_LEVEL_USERS)).matches()) {
                int levelNumber = Integer.parseInt(matcher.group(1));

                if (controller.getUsersInSameLevel(levelNumber) == null) {
                    System.out.println("No_such_user_found");
                    continue;
                }

                if (controller.getUsersInSameLevel(levelNumber).size() == 1) {
                    System.out.println("He_is_all_by_himself");
                    continue;
                }

                for (User user : controller.getUsersInSameLevel(levelNumber)) {
                    //fixme string with spare " ";

                    System.out.print(user + " ");
                }
                System.out.println();


            } else if ((matcher = Regexes.getMatcher(input, Regexes.USER_FRIENDS)).matches()) {
                if (controller.getUserFriends(matcher.group(1)) == null){
                    System.out.println("No_such_user_found");
                    continue;
                }

                if (controller.getUserFriends(matcher.group(1)).size() == 1){
                    System.out.println("No_friend");
                    continue;
                }

                for (User userFriend : controller.getUserFriends(matcher.group(1))) {
                    //fixme string with spare " ";

                    System.out.print(userFriend + " ");
                }
                System.out.println();

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
