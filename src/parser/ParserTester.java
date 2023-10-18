package parser;

import environment.Environment;
import scanner.*;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The ParserTester class tests the Parser class.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class ParserTester
{
    /**
     * The main method creates a Scanner and a Parser and parses statements
     * until there are no more.
     * @param args arguments from the command line
     * @throws ScanErrorException if there is an error in scanning
     * @throws IOException if there is an error in reading user input
     */
    public static void main(String[] args) throws ScanErrorException, IOException
    {
        String file = "parserTestAST.txt";
        Scanner sc = new Scanner(new FileInputStream("./src/parser/" + file));
        Parser ps = new Parser(sc);
        Environment env = new Environment();

        ps.parseProgram().exec(env);
    }
}
