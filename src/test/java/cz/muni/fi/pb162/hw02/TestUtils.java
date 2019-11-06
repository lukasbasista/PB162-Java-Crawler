package cz.muni.fi.pb162.hw02;

import java.util.Arrays;

/**
 *
 * @author Jakub Cechacek
 */
public class TestUtils {
    public static String[] prefix(String prefix, String... values) {
        return Arrays.stream(values).map(s -> prefix + s).toArray(String[]::new);
    }
}
