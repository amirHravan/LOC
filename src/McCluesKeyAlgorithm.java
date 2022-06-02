import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Pair<key, value> {
    private final key key;
    private final value value;

    public Pair(key key, value value) {
        this.key = key;
        this.value = value;
    }

    public key getKey() {
        return key;
    }

    public value getValue() {
        return value;
    }

}


class MinTerm {
    private final int number;
    private final String binary;

    public MinTerm(int number, String binary) {
        this.number = number;
        this.binary = binary;
    }

    public static String toBinaryForm(int number, int digitCount) {
        String binaryMinTerm = Integer.toBinaryString(number);

        if (binaryMinTerm.length() != digitCount) {
            StringBuilder temp = new StringBuilder();
            for (int i = 0; i < (digitCount - binaryMinTerm.length()); i++) {
                temp.append("0");
            }
            temp.append(binaryMinTerm);
            binaryMinTerm = temp.toString();
        }
        return binaryMinTerm;
    }

    public static ArrayList<String> turnDontCareToBinary(ArrayList<String> allMinTerms, String DontCareBinary) {
        boolean flag = false;

        for (int i = 0; i < DontCareBinary.length(); i++) {
            if (DontCareBinary.charAt(i) == '-') {
                StringBuilder temp = new StringBuilder(DontCareBinary);
                temp.setCharAt(i, '0');
                for (String string : turnDontCareToBinary(allMinTerms, temp.toString())) {
                    if (!allMinTerms.contains(string))
                        allMinTerms.add(string);
                }

                temp.setCharAt(i, '1');

                for (String string : turnDontCareToBinary(allMinTerms, temp.toString())) {
                    if (!allMinTerms.contains(string))
                        allMinTerms.add(string);
                }

                flag = true;
            }
        }
        if (!flag)
            allMinTerms.add(DontCareBinary);

        for (int j = 0; j < allMinTerms.size(); j++) {
            String term1 = allMinTerms.get(j);
            for (int i = j + 1; i < allMinTerms.size(); i++) {
                String term2 = allMinTerms.get(i);
                if (term1.equals(term2)) {
                    allMinTerms.remove(term2);
                    i--;
                }
            }
        }
        return allMinTerms;
    }

    public static int binaryToDecimal(String binaryForm) {
        return Integer.parseInt(binaryForm, 2);
    }

    public int getNumber() {
        return number;
    }

    public String getBinary() {
        return binary;
    }

    public int countGroupNumber() {
        int count = 0;
        for (int i = 0; i < this.binary.length(); i++) {
            if (binary.charAt(i) == '1')
                count++;
        }
        return count;
    }

    public int getDifferentBitsCount(MinTerm minTerm) {
        int count = 0;
        for (int i = 0; i < this.binary.length(); i++) {
            if (this.binary.charAt(i) != minTerm.binary.charAt(i))
                count++;
        }
        return count;
    }

    @Override
    public String toString() {
        return "(" + number + " : " + binary + ")";
    }

    public Pair<MinTerm, MinTerm> getNewMinTerm(MinTerm minTerm) {
        int index = 0;

        for (int i = 0; i < this.binary.length(); i++) {
            if (this.binary.charAt(i) != minTerm.binary.charAt(i)) {
                index = i;
                break;
            }
        }

        StringBuilder newBinaryForm = new StringBuilder(minTerm.binary);
        newBinaryForm.setCharAt(index, '-');
        return new Pair<>(new MinTerm(this.number, newBinaryForm.toString()), new MinTerm(minTerm.number, newBinaryForm.toString()));
    }

}

class Group {
    private final ArrayList<MinTerm> minTerms = new ArrayList<>();

    public ArrayList<MinTerm> getMinTerms() {
        return minTerms;
    }

    @Override
    public String toString() {
        return "Group{" +
                "minTerms=" + minTerms +
                '}';
    }

    public Group compareWith(Group group) {
        Group newGroup = new Group();
        for (MinTerm minTerm1 : this.minTerms) {
            for (MinTerm minTerm2 : group.minTerms) {
                if (minTerm1.getDifferentBitsCount(minTerm2) == 1) {
                    newGroup.addPair(minTerm1.getNewMinTerm(minTerm2));
                }
            }
        }
        return newGroup;

    }

    private void addPair(Pair<MinTerm, MinTerm> newMinTerm) {
        minTerms.add(newMinTerm.getKey());
        minTerms.add(newMinTerm.getValue());
    }
}

class EPI {
    private final ArrayList<Integer> termIndexes = new ArrayList<>();
    private final String binaryForm;

    public EPI(String binaryForm) {
        this.binaryForm = binaryForm;
    }

    public ArrayList<Integer> getTermIndexes() {
        return termIndexes;
    }

    public String getBinaryForm() {
        return binaryForm;
    }

    @Override
    public String toString() {
        return "(" +
                "termIndexes= " + termIndexes +
                ", binaryForm= " + binaryForm  +
                ')';
    }
}


class Algorithm {
    private final ArrayList<MinTerm> minTerms = new ArrayList<>();
    private int variableCount;
    private Group[] table;
    public void run() {
        makeMinTerms();
        makeTable();
        simplifyTable();
        printEPIs();
    }
    private ArrayList<ArrayList<EPI>> getAllSubArrays(ArrayList<EPI> source) {
        ArrayList<ArrayList<EPI>> allSubArrays = new ArrayList<>();

        for (int i = 1; i < Math.pow(2, source.size()); i++) {
            ArrayList<EPI> subArray = new ArrayList<>();
            for (int j = 0; j < source.size(); j++) {
                if ((i >> j) % 2 == 1) {
                    subArray.add(source.get(j));
                }
            }
            allSubArrays.add(subArray);
        }
        return allSubArrays;
    }

    private void printEPIs() {
        ArrayList<Integer> allIndexes = new ArrayList<>();
        ArrayList<String> termsToPrint = new ArrayList<>();
        ArrayList<EPI> EPIs = new ArrayList<>();

        for (int i = 0; i <= variableCount; i++) {
            if (table[i] == null)
                continue;
            for (MinTerm minTerm : table[i].getMinTerms()) {
                allIndexes.add(minTerm.getNumber());
                if (!termsToPrint.contains(minTerm.getBinary()))
                    termsToPrint.add(minTerm.getBinary());
            }
        }

        for (MinTerm minTerm : minTerms) {
            if (!allIndexes.contains(minTerm.getNumber())) {
                termsToPrint.add(minTerm.getBinary());
            }
        }


        for (String string : termsToPrint) {
            EPI epi = new EPI(string);
            for (String binary : MinTerm.turnDontCareToBinary(new ArrayList<>(), string)) {
                epi.getTermIndexes().add(MinTerm.binaryToDecimal(binary));
            }
            EPIs.add(epi);
        }

        ArrayList<ArrayList<EPI>> subEPIs = getAllSubArrays(EPIs);

        int minimumTerm = 1000000;
        ArrayList<EPI> necessaryEPI;
        ArrayList<ArrayList<EPI>> necessaryEPIs = new ArrayList<>();

        for (ArrayList<EPI> subEPIArray : subEPIs) {
            if (coversAllMinTerms(subEPIArray)) {
                if (subEPIArray.size() < minimumTerm) {
                    necessaryEPIs.clear();
                    minimumTerm = subEPIArray.size();
                    necessaryEPIs.add(subEPIArray);
                }
                if (subEPIArray.size() == minimumTerm) {
                    necessaryEPIs.add(subEPIArray);
                }
            }
        }


        if (termsToPrint.size() == 1)
            System.out.println(1);
        else {
            for (ArrayList<EPI> Nepi : necessaryEPIs) {
                StringBuilder temp = new StringBuilder();
                for (EPI epi : Nepi) {
                    temp.append(getStringForm(epi.getBinaryForm())).append(" + ");
                }
                System.out.println(temp.substring(0, temp.length() - 3));
            }
            necessaryEPI = necessaryEPIs.get(Math.min(9,necessaryEPIs.size()-1));
            StringBuilder temp = new StringBuilder();
            for (EPI epi : necessaryEPI) {
                temp.append(getStringForm(epi.getBinaryForm())).append(" + ");
            }
            System.out.println(temp.substring(0,temp.length()-3));
        }
    }

    private boolean coversAllMinTerms(ArrayList<EPI> subEPIArray) {
        ArrayList<Integer> allCoveredIndexes = new ArrayList<>();
        for (EPI epi : subEPIArray) {
            for (Integer termIndex : epi.getTermIndexes()) {
                if (!allCoveredIndexes.contains(termIndex))
                    allCoveredIndexes.add(termIndex);
            }
        }
        ArrayList<Integer> allNeededIndexes = new ArrayList<>();

        for (MinTerm minTerm : minTerms) {
            allNeededIndexes.add(minTerm.getNumber());
        }
        return allCoveredIndexes.containsAll(allNeededIndexes);
    }

    private String getStringForm(String binaryString) {
        StringBuilder term = new StringBuilder();
        char letter = 'A';

        for (int i = 0; i < binaryString.length(); i++) {
            if (binaryString.charAt(i) == '0')
                term.append(letter).append('\'');
            if (binaryString.charAt(i) == '1')
                term.append(letter);
            letter += 1;
        }
        return term.toString();
    }

    private void simplifyTable() {
        Group[] newTable = new Group[variableCount + 1];
        for (int i = 0; i <= variableCount - 1; i++) {
            if (table[i] == null || table[i + 1] == null) {
                continue;
            }
            newTable[i] = table[i].compareWith(table[i + 1]);
        }

        if (!isTableEmpty(variableCount, newTable)) {
            table = newTable;
//            System.out.println(Arrays.toString(table));
            simplifyTable();
        } else {
            cleanTable(table);
//            System.out.println(Arrays.toString(table));
        }
    }

    private void cleanTable(Group[] table) {
        for (int i = 0; i < variableCount; i++) {
            if (table[i] == null)
                continue;

            ArrayList<MinTerm> tableMinTerms = table[i].getMinTerms();
            for (int j = 0; j < tableMinTerms.size(); j++) {
                MinTerm minTerm1 = tableMinTerms.get(j);
                for (int k = j + 1; k < tableMinTerms.size(); k++) {
                    MinTerm minTerm2 = tableMinTerms.get(k);
                    if (minTerm1.getNumber() == minTerm2.getNumber()) {
                        tableMinTerms.remove(minTerm2);
                        k--;
                    }
                }
            }
        }
    }

    private boolean isTableEmpty(int variableCount, Group[] newTable) {
        for (int i = 0; i <= variableCount; i++) {
            if (newTable[i] != null && newTable[i].getMinTerms().size() != 0)
                return false;
        }
        return true;
    }

    private void makeTable() {
        for (MinTerm minTerm : minTerms) {
            if (table[minTerm.countGroupNumber()] == null)
                table[minTerm.countGroupNumber()] = new Group();
            table[minTerm.countGroupNumber()].getMinTerms().add(minTerm);
        }

//        System.out.println(Arrays.toString(table));
    }

    private void makeMinTerms() {
        Scanner scanner = new Scanner(System.in);
        variableCount = scanner.nextInt();

        table = new Group[variableCount + 1];
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(scanner.next().split(",")));
        for (String token : tokens) {
            minTerms.add(new MinTerm(Integer.parseInt(token), MinTerm.toBinaryForm(Integer.parseInt(token), variableCount)));
        }
    }
}

public class McCluesKeyAlgorithm {
    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm();
        algorithm.run();
    }
}
