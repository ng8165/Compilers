package scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * The ScannerTester class tests the Scanner class by running it through ScannerTest.txt
 * or scannerTestAdvanced.txt.
 * @author Nelson Gou
 * @version 8/31/23
 */
public class ScannerTester
{
    /**
     * Loads a text file into the Scanner and prints tokens until there are no more.
     * @param args arguments from the command line
     * @throws FileNotFoundException in case the testing file cannot be found
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        String name = "./ScannerTest.txt";
        // String name = "./scannerTestAdvanced.txt";
        Scanner sc = new Scanner(new FileInputStream(name));

        while (sc.hasNext())
        {
            try
            {
                System.out.println(sc.nextToken());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                // System.exit(69);
            }
        }
    }
}
