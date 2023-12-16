import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class Main {

    static String[] dataFormat = new String[] {"Second name", "First name", "Patronymic", "Date", "Phone", "Gender"};

    public static void main(String[] args) {
        try {
            saveInFile(parseInput(inputData()));
        } catch (NotEnoughData | ManyData | NoCorrectFormat e) {
            System.out.println(e.getMessage());
        }
    }


    public static String inputData() throws NotEnoughData {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().replaceAll("\\s+", " ").trim();
        if (input.equals("") || input.equals(" ")) {
            throw new NotEnoughData();
        }
        return input;
    }

    public static String parseInput(String input) throws ManyData, NotEnoughData, NoCorrectFormat{
        String[] dataArr = input.split(" ");
        //check count data
        if (dataArr.length < dataFormat.length) {
            throw new NotEnoughData();
        } else if (dataArr.length > dataFormat.length) {
            throw new ManyData();
        }
        //sort data in format
        String[] parsedArr = new String[dataFormat.length];
        int i = 0;
        for (String data : dataArr) {
            if (isPhone(data)) {
                parsedArr[4] = data;
            } else if (data.equals("f") || data.equals("m")) {
                parsedArr[5] = data;
            } else if (isDate(data)) {
                parsedArr[3] = data;
            } else {
                if (i <= 2) {
                    parsedArr[i] = data;
                    i++;
                }
            }
        }
        //check any format data, gen err message
        StringBuilder errData = new StringBuilder();
        for (int j = 0; j < dataFormat.length; j++) {
            if (parsedArr[j] == null) {
                errData.append(dataFormat[j]).append(" ");
            }
        }
        if (!errData.isEmpty())
            throw new NoCorrectFormat(errData.toString());

        return "<" + String.join("><", parsedArr) + ">";
    }

    public static boolean isPhone(String data) {
        try {
            Long.parseLong(data);
//            return data.length() == 11;             //if phone format
            return true;                            //if phone not format
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDate(String data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        simpleDateFormat.setLenient(false);
        try {
            simpleDateFormat.parse(data);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }


    public static void saveInFile(String note) {
        String fileName = note.split("><")[0].substring(1) + ".txt";
        try (FileWriter writer = new FileWriter(fileName, true);
                BufferedWriter bufferWriter = new BufferedWriter(writer)) {
            bufferWriter.write(note + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}


class NotEnoughData extends Exception {
    public NotEnoughData() {
        super("Недостаточно введённых данных.");
    }
}

class ManyData extends Exception {
    public ManyData() {
        super("Изботок данных.");
    }
}

class NoCorrectFormat extends Exception {
    public NoCorrectFormat(String data) {
        super("Неверный формат ввода: " + data);
    }
}