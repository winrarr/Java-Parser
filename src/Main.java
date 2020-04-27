import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            File myObj = new File("C:\\Users\\KT\\IdeaProjects\\Parser\\tests\\test");
            Scanner myReader = new Scanner(myObj);
            StringBuilder prgStr = new StringBuilder();
            while (myReader.hasNextLine()) {
                prgStr.append(myReader.nextLine());
            }
            Parser parser = new Parser();
            var prg = parser.parse(prgStr.toString());
            System.out.println(prg);
            myReader.close();
        } catch (FileNotFoundException | Parser.NoMatchFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
