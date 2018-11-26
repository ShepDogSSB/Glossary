import java.util.Comparator;

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

    private static class StringLT implements Comparator<String> {

        @Override
        public int compare(String arg0, String arg1) {
            return arg0.compareTo(arg1);
        }
    }

    /**
     * Creates the header for the base index file.
     *
     * @param index
     *            The html to output the header to
     */
    public static void createHomePageHeader(SimpleWriter index) {

        /*
         * Makes the document a valid html.
         */

        index.println("<!DOCTYPE html>");

        /*
         * prints out the leading information for the "home page".
         */

        index.println("<html>\n<head>\n<title>Glossary</title></head>\n<body>"
                + "<h2>Glossary</h2>" + "<hr />" + "<h3>Word List</h3>");
    }

    /**
     * Creates an html page for each word.
     *
     * @param definition
     *            The definition of the word
     * @param word
     *            The word for which the page is being made for.
     * @param out
     *            The input file stream.
     */
    public static void createWordPage(String word, String definition,
            SimpleWriter out, Queue<String> words) {
        out.println("<html>\n" + "<head>\n" + "<title>" + word + "</title>");
        out.println("</head>\n" + "<body>\n" + "<h2><b><i><font color=\"red\">"
                + word + "</font></i></b></h2>");
        String tempDef = replaceWithLinks(words, definition);
        out.println("<blockquote>" + tempDef + "</blockquote>");
        out.println("<hr />");
        out.println("<p>Return to <a href=\"index.html\">index</a>.</p>\n"
                + "</body>\n" + "</html>");
    }

    /**
     * Gets the words from the input and puts them in a Queue.
     *
     * @param words
     *            the queue for the definitions to be stored
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
     * Gets the definitions for words from the input and puts them in a Queue.
     *
     * @param definitions
     *            the queue for the definitions to be stored
     * @param in
     *            The input file stream.
     */
    public static void getDefinitions(Queue<String> definitions,
            SimpleReader in) {
        /*
         * While we can still parse through the input stream
         */
        while (!in.atEOS()) {
            /*
             * Initialize strings and string buffer
             */
            StringBuffer buffer = new StringBuffer();
            String def = "";
            String testVal = in.nextLine();
            /*
             * While the next line in the input isn't empty.
             */
            while (!testVal.equals("")) {
                /*
                 * We check to see if the next line has a space in it (if it
                 * doesn't, that means its a word.) and if it does we append
                 * that string to a buffer (SpotBugs recommended using String
                 * Buffer).
                 */
                if (testVal.contains(" ")) {
                    //def += n + " ";
                    buffer.append(testVal + " ");

                }
                /*
                 * We then check to see if we are at the end of the file, and if
                 * we aren't then we go to the next line, and if we are we stop
                 * trying to parse through the input stream.
                 */
                if (in.atEOS()) {
                    testVal = "";
                } else {
                    testVal = in.nextLine();
                }
            }
            /*
             * We convert the buffer created into a string and then enqueue it
             * to definitions.
             */
            def = buffer.toString();
            definitions.enqueue(def);
        }
    }

    /**
     *
     */
    public static void createList(SimpleWriter index, Queue<String> words) {
        int count = 0;
        index.println("<ul>");
        while (count < words.length()) {
            String word = words.front();
            index.println(
                    "<li><a href=" + word + ".html>" + word + "</a></li>");
            words.rotate(1);
            count++;
        }
    }

    /**
     *
     */
    public static String replaceWithLinks(Queue<String> words,
            String definition) {
        String result = "";
        for (String word : words) {
            for (String wordTwo : definition.split(" ")) {
                if (definition.indexOf(word) != -1) {
                    result = definition.replace(word, "<a href=\"" + word
                            + ".html" + "\">" + word + "</a>");
                } else {
                    result = definition;
                }
            }
        }
        return result;
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
         * Reads in the text file to use as the input.
         */

        out.println("Enter The Location & Name of Input File: ");
        String name = in.nextLine();
        SimpleReader fileIn = new SimpleReader1L(name);

        /*
         * Reads in the name of the output folder
         */

        out.println("Enter The Name of the Output Folder: ");
        String folder = in.nextLine();

        /*
         * Creates home page and makes it into a valid html page.
         */

        SimpleWriter index = new SimpleWriter1L(folder + "/index.html");
        createHomePageHeader(index);

        /*
         * Gets the words and definitions and puts them into their respective
         * Queue.
         */

        getWords(words, fileIn);
        SimpleReader fileIn2 = new SimpleReader1L(name);
        fileIn.close();
        getDefinitions(definitions, fileIn2);

        /*
         * Creates the word pages for each glossary term.
         */
        int count = 0;
        Queue<String> wordList = new Queue1L<>();
        while (count < words.length()) {
            String tempWord = words.front();
            String tempDefinition = definitions.dequeue();
            SimpleWriter tempOut = new SimpleWriter1L(
                    folder + "/" + tempWord + ".html");
            createWordPage(tempWord, tempDefinition, tempOut, words);
            words.rotate(1);
            count++;
            tempOut.close();
        }

        /*
         * Creates the list for the home page.
         */
        Comparator<String> order = new StringLT();
        words.sort(order);
        createList(index, words);

        /*
         * Puts links in places where appropriate in the word pages.
         */

        /*
         * TODO: go into the word pages and add links where appropriate, then
         * make sure everything is closed properly. Also add comments to
         * createWordPage
         */

        /*
         * Close I/O Streams.
         */

        in.close();
        fileIn2.close();
        out.close();
        index.close();

    }
}
