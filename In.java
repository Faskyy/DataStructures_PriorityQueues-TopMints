import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *  In. This class provides methods for reading strings
 *  and numbers from standard input and file inputs.
 *
 *  The Locale used is: language = English, country = US.
 *
 *  Like Scanner, reading a token also consumes preceding Java
 *  whitespace, reading a full line consumes
 *  the following end-of-line delimiter, while reading a character consumes
 *  nothing extra.
 *
 * @author david
 * Useful as a helper class for CIS-2168 demos and assignments.
 */
public final class In {

    // assume Unicode UTF-8 encoding
    private static final String CHARSET_NAME = "UTF-8";

    // assume language = English, country = US for consistency with System.out.
    private static final Locale LOCALE = Locale.US;

    // the default token separator; we maintain the invariant that this value
    // is held by the scanner's delimiter between calls
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+");

    // makes whitespace characters significant
    private static final Pattern EMPTY_PATTERN = Pattern.compile("");

    // used to read the entire input. source:
    // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
    private static final Pattern EVERYTHING_PATTERN = Pattern.compile("\\A");


    private Scanner scanner;

    // Initializes an input stream from standard input.
    public In() {
        scanner = new Scanner(new BufferedInputStream(System.in), CHARSET_NAME);
        scanner.useLocale(LOCALE);
    }

    // Initializes an input stream from a filename.
    public In(String name) {
        if (name == null) throw new IllegalArgumentException("argument is null");
        try {
            // first try to read file from local file system
            File file = new File(name);
            if (file.exists()) {
                // Wrap with BufferedInputStream instead of using
                // file as argument to Scanner
                FileInputStream fis = new FileInputStream(file);
                scanner = new Scanner(new BufferedInputStream(fis), CHARSET_NAME);
                scanner.useLocale(LOCALE);
                return;
            }

            // resource on the web
            URL url = new URL(name);
            URLConnection site = url.openConnection();
            InputStream is     = site.getInputStream();
            scanner            = new Scanner(new BufferedInputStream(is));
        }

        catch (IOException ioe) {
            throw new IllegalArgumentException("Could not open " + name, ioe);
        }
    }

    // Initializes an input stream from a given Scanner source.
    public In(Scanner scanner) {
        if (scanner == null) throw new IllegalArgumentException("scanner argument is null");
        this.scanner = scanner;
    }

    // Returns true if this input stream exists.
    public boolean exists()  { return scanner != null; }

    // Returns true if input stream is empty (except possibly whitespace).
    public boolean isEmpty() { return !scanner.hasNext(); }

    // Returns true if this input stream has a next line.
    public boolean hasNextLine() { return scanner.hasNextLine(); }

    // Returns true if this input stream has more input (including whitespace).
    public boolean hasNextChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        boolean result = scanner.hasNext();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result;
    }

