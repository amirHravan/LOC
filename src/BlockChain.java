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
    NUMBER_OF_USERS_IN_LEVEL("Number_of_users_in_level (?<level>\\d+)"),
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
    private final String username;
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

    public void promoteUser(ArrayList<Level> levels) {

        this.introducedPeopleCount = 0;
        if (this.levelNumber < 2) {
            return;
        }

        if (levels.get(this.levelNumber-1).hasCapacity()){
            this.levelNumber--;
            this.position = levels.get(levelNumber).getPeopleCount();
            levels.get(levelNumber).getUsers().add(this);
            levels.get(levelNumber+1).getUsers().remove(this);
            return;

        }

        Comparator<User> comparator = Comparator.comparing(User::getIntroducedPeopleCount).thenComparing(User::getDeposit);
        ArrayList<User> sortedUsers = new ArrayList<>(levels.get(levelNumber - 1).getUsers());
        sortedUsers.sort(comparator);

        User miserableUser = sortedUsers.get(sortedUsers.size()-1);

        this.introducedPeopleCount = 0;
        miserableUser.setIntroducedPeopleCount(0);
        
        levels.get(levelNumber - 1).getUsers().add(this);
        levels.get(levelNumber).getUsers().add(miserableUser);

        levels.get(levelNumber - 1).getUsers().remove(miserableUser);
        levels.get(levelNumber).getUsers().remove(this);

        levels.get(levelNumber - 1).updatePositions();
        levels.get(levelNumber).updatePositions();
        
        miserableUser.setLevelNumber(levelNumber);
        this.levelNumber--;
        
    }

    @Override
    public String toString() {
        return this.username;
    }
}

class Level {
    private final ArrayList<User> users;
    private final int number;
    private final int capacity;

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
    public boolean hasCapacity() {
        return this.capacity - this.users.size() > 0;
    }
    public void updatePositions() {
        for (int i = 0; i < users.size(); i++) {
            users.get(i).setPosition(i);
        }
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
    private final Mahdiz mahdiz;
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

    public Level addNewLevel() {
        Level level = new Level(levels.size());
        levels.add(level);
        return level;
    }

    public String joinRequest(String username, int deposit) {
        Level level = addNewLevel();
        level.getUsers().add(new User(null, username, level.getNumber(), (int) (deposit * 0.15), 0));
        founder.setDeposit((int) (founder.getDeposit() + deposit * 0.1));
        mahdiz.setDeposit((int) (mahdiz.getDeposit() + deposit * 0.25));

        for (int i = 0; i < level.getNumber(); i++) {
            for (User user : levels.get(i).getUsers()) {
                user.setDeposit((int) (user.getDeposit() + ((deposit*0.5) / (level.getNumber() * level.getPeopleCount()))));
            }
        }
        return "User added successfully in level " + level.getNumber();
    }

    public String invitePeople(String username, User oldUser, int deposit) {
        oldUser.setIntroducedPeopleCount(oldUser.getIntroducedPeopleCount() + 1);

        Level level = new Level(-1);
        for (int i = oldUser.getLevelNumber() + 1; i < levels.size(); i++) {
            if (levels.get(i).hasCapacity()){
                level = levels.get(i);
                break;
            }
        }

        if (level.getNumber() == -1){
            level = addNewLevel();
        }

        User newUser = new User(oldUser, username, level.getNumber(), (int) (deposit * 0.2), level.getPeopleCount());

        oldUser.setDeposit((int) (oldUser.getDeposit() + deposit * 0.05));
        mahdiz.setDeposit((int) (mahdiz.getDeposit() + deposit * 0.15));
        founder.setDeposit((int) (founder.getDeposit() + deposit * 0.1));
        level.getUsers().add(newUser);

        for (int i = 0; i < level.getNumber(); i++) {
            for (User user : levels.get(i).getUsers()) {
                user.setDeposit((int) (user.getDeposit() + ((deposit*0.5) / (level.getNumber() * level.getPeopleCount()))));
            }
        }

        if (oldUser.getIntroducedPeopleCount() >= 5)
            oldUser.promoteUser(levels);

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
        financialCircle.getMahdiz().setDeposit(financialCircle.getMahdiz().getDeposit() + 5000);

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

    public ArrayList<User> getUsersInSameLevelList(User user){
        ArrayList<User> users = new ArrayList<>(financialCircle.getLevels().get(user.getLevelNumber()).getUsers());
        users.remove(user);

        return users;
    }

    public String getUsersInSameLevel(String username){
        User user = getUserByName(username);

        if (user == null) {
            return ("No_such_user_found");
        }
        ArrayList<User> userList = getUsersInSameLevelList(user);

        if (userList.size() == 0) {
            return ("He_is_all_by_himself");
        }
        StringBuilder output = new StringBuilder();

        for (User guy : userList) {
            output.append(guy).append(" ");
        }

        return output.substring(0,output.length()-1);

    }

    public String getUsersInLevel(int levelNumber) {
        if (levelNumber > financialCircle.getLevels().size()-1 || financialCircle.getLevels().get(levelNumber) == null)
            return "No_such_level_found";

        return String.valueOf(financialCircle.getLevels().get(levelNumber).getUsers().size());
    }

    public int getMahdizDeposit() {
        return financialCircle.getMahdiz().getDeposit();
    }

    public String getFriends(String username) {
        User targetUser = getUserByName(username);

        if (targetUser == null){
            return ("No_such_user_found");
        }
        ArrayList<User> userFriends = getUserFriends(targetUser);

        if (userFriends.size() == 0){
            return ("No_friend");
        }
        StringBuilder output = new StringBuilder();

        for (User userFriend : userFriends) {
            output.append(userFriend).append(" ");
        }

//        return output.substring(0,output.length()-1);
        return output.toString();
    }

    public ArrayList<User> getUserFriends(User targetUser){
        ArrayList<User> userFriends = new ArrayList<>();
        Level level = financialCircle.getLevels().get(targetUser.getLevelNumber());

        if (level.getUsers().size() == 1)
            return userFriends;

        userFriends.add(level.getUsers().get( (targetUser.getPosition() -1 + level.getUsers().size()) % level.getUsers().size()));

        if (level.getUsers().size() == 2) {
            return userFriends;
        }

        userFriends.add(level.getUsers().get( (targetUser.getPosition()+1) % level.getUsers().size()));
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

    public void printAllLevels() {
        for (Level level : financialCircle.getLevels()) {
            for (User user : level.getUsers()) {
                System.out.print(user + " ");
            }
            System.out.println();
        }
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

            } else if (Regexes.getMatcher(input, Regexes.NUMBER_OF_USERS).matches()) {
                System.out.println(controller.getNumberOfUsers());

            } else if (Regexes.getMatcher(input, Regexes.NUMBER_OF_LEVELS).matches()) {
                System.out.println(controller.getNumberOfLevels());

            } else if ((matcher = Regexes.getMatcher(input, Regexes.USER_CREDITS)).matches()) {
                System.out.println(controller.getUserDeposit(matcher.group(1)));

            } else if ((matcher = Regexes.getMatcher(input, Regexes.NUMBER_OF_USERS_IN_LEVEL)).matches()) {
                System.out.println(controller.getUsersInLevel(Integer.parseInt(matcher.group(1))));

            } else if (Regexes.getMatcher(input, Regexes.PROFIT_UNTIL_NOW).matches()) {
                System.out.println(controller.getMahdizDeposit());

            } else if ((matcher = Regexes.getMatcher(input, Regexes.SAME_LEVEL_USERS)).matches()) {
                System.out.println(controller.getUsersInSameLevel(matcher.group(1)));

            } else if ((matcher = Regexes.getMatcher(input, Regexes.USER_FRIENDS)).matches()) {
                System.out.println(controller.getFriends(matcher.group(1)));

            } else if ((matcher = Regexes.getMatcher(input, Regexes.USER_INTRODUCER)).matches()) {
                System.out.println(controller.getUserIntroducer(matcher.group(1)));

            } else if (Regexes.getMatcher(input, Regexes.END).matches()) {
                break;

            } else {
                controller.printAllLevels();
//                throw new RuntimeException();

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
