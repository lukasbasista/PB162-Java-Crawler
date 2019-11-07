package cz.muni.fi.pb162.hw02;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

/**
 *
 * @author Jakub Cechacek
 */
public class TestUtils {
    public static String[] prefix(String prefix, String... values) {
        return Arrays.stream(values).map(s -> prefix + s).toArray(String[]::new);
    }

    public static Map<String, List<String>> buildIndex(String prefix, String suffix, String... entries) {
        Map<String, List<String>> index = new HashMap<>();

        for(String entry : entries) {
            String[] parts = entry.split(":", 2);
            String key = prefix + parts[0].strip() + suffix;

            List<String> value = Arrays
                    .stream(parts[1].split("\\s+"))
                    .filter(not(String::isEmpty))
                    .map(String::strip)
                    .map(s -> prefix + s + suffix)
                    .collect(Collectors.toList());

            index.put(key, value);
        }
        return index;
    }

}
