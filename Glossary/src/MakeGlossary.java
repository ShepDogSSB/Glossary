import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * This program creates a glossary from an input file.
 *
 * @author Nicholas Shepard
 *
 */
public final class MakeGlossary {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private MakeGlossary() {
    }

    public static void createHomePage(SimpleWriter index) {
        index.print("<!DOCTYPE html>");
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        /*
         * Basic I/O Variable Instantiation
         */
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        /*
         * Creates home page and makes it into a valid html page.
         */

        SimpleWriter index = new SimpleWriter1L("data/index.html");
        createHomePage(index);

        /*
         * Reads in the text file to use as the input.
         */

//        out.println("Enter Location & Name of Input File: ");
//        SimpleReader fileIn = new SimpleReader1L(in.nextLine());

        /*
         * Close I/O Streams.
         */
        in.close();
        out.close();

    }

}
