import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
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

            var prg = Parser.parse(prgStr.toString());
//            var res = prg.eval(new HashMap<String, Val>(), new HashMap<RefVal, Val>());
//            System.out.println(res);
            myReader.close();
        } catch (FileNotFoundException | Parser.NoMatchFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
