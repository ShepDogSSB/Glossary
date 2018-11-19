import components.queue.Queue;
import components.queue.Queue1L;
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

    /**
     * Creates the header for the base index file.
     *
     * @param index
     *            The html to output the header to
     */
    public static void createHomePageHeader(SimpleWriter index) {
        index.println("<!DOCTYPE html>");
        index.println("<html>\n<head>\n<title>Glossary</title></head>\n<body>"
                + "<h2>Glossary</h2>" + "<hr />" + "<h3>Word List</h3>");
    }

    public static void createWordPage(String word, String definition,
            SimpleWriter out) {
        out.println("<html>\n" + "<head>\n" + "<title>" + word + "</title>");
        out.println("</head>\n" + "<body>\n" + "<h2><b><i><font color=\"red\">"
                + word + "</font></i></b></h2>");
        out.println("<blockquote>" + definition + "</blockquote>");

    }

    /**
     * Gets the words from the input and puts them in a Set.
     *
     * @param words
     *            the set for the words to be stored
     * @param in
     *            The input file stream.
     */
    public static void getWords(Queue<String> words, SimpleReader in) {
        while (!in.atEOS()) {
            String testVal = in.nextLine();
            if (!testVal.contains(" ") && !testVal.isEmpty()) {
                words.enqueue(testVal);
            }
        }
    }

    /**
     * Gets the definitions from the input and puts them in a Set.
     *
     * @param definitions
     *            the set for the definitions to be stored
     * @param in
     *            The input file stream.
     */
    public static void getDefinitions(Queue<String> definitions,
            SimpleReader in) {
        while (!in.atEOS()) {
            String testVal = in.nextLine();
            if (testVal.contains(" ") && !testVal.isEmpty()) {
                definitions.enqueue(testVal);
            }
        }
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
         * Queues for storing words and definitions instantiation.
         */

        Queue<String> words = new Queue1L<>();
        Queue<String> definitions = new Queue1L<>();

        /*
         * Creates home page and makes it into a valid html page.
         */

        SimpleWriter index = new SimpleWriter1L("data/index.html");
        createHomePageHeader(index);

        /*
         * Reads in the text file to use as the input.
         */

        out.println("Enter Location & Name of Input File: ");
        String name = in.nextLine();
        SimpleReader fileIn = new SimpleReader1L(name);

        /*
         * Gets the words and definitions and puts them into their respective
         * Queue.
         */

        getWords(words, fileIn);

        fileIn.close();
        SimpleReader fileInTwo = new SimpleReader1L(name);

        getDefinitions(definitions, fileInTwo);

        /*
         * Creates the word pages for each glossary term.
         */
        while (words.length() > 0 && definitions.length() > 0) {
            String tempWord = words.dequeue();
            String tempDefinition = definitions.dequeue();
            SimpleWriter tempOut = new SimpleWriter1L(
                    "data/" + tempWord + ".html");

            createWordPage(tempWord, tempDefinition, tempOut);
        }

        /*
         * Close I/O Streams.
         */
        in.close();
        fileInTwo.close();
        out.close();
        index.close();
    }
}