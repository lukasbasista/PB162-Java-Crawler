package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.SimpleHttpClient;
import cz.muni.fi.pb162.hw02.crawler.SmartCrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lukáš Bašista
 */
public class Crawler implements SmartCrawler {


    private SimpleHttpClient client = new SimpleHttpClient();

    @Override
    public List<String> crawl(String url) {
        String html = "";
        try {
            html = client.get(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> tags = getTagValues(html);
        Pattern p = Pattern.compile("href=\"([^\"]*)\"", Pattern.DOTALL);
        List<String> urls = new ArrayList<>();
        for (String tag: tags) {
            Matcher m = p.matcher(tag);
            if (m.find()) {
                urls.add(m.group(1));
            }
        }
        return urls;
    }


    @Override
    public Map<String, List<String>> crawlAll(String url) {
        Map<String, List<String>> result = new HashMap<>();
        List<String> urls = crawl(url);
        Boolean x = false;
        Set<String> checked = new HashSet<>();
        for (String link : urls) {
            Set<String> unchecked = new HashSet<>(crawl(link));
            x = unchecked.isEmpty();
            while (!x) {
                Set<String> k = new HashSet<>();
                for (String a : unchecked) {
                    k.addAll(crawl(a));
                    checked.add(a);
                }
                unchecked.addAll(k);
                unchecked.removeAll(checked);
                x = unchecked.isEmpty();
            }
        }
        for (String s : checked) {
            result.put(s, new ArrayList<>(crawl(s)));
        }
        return result;
    }

    @Override
    public Map<String, List<String>> crawlReverse(String url) {
        return reverseIndex(crawlAll(url));
    }

    @Override
    public Map<String, List<String>> reverseIndex(Map<String, List<String>> index) {
        Map<String, List<String>> result = new HashMap<>();
        Set<String> urls = new HashSet<>();
        for (String key : index.keySet()) {
            urls.addAll(index.get(key));
            urls.add(key);
        }
        for (String url : urls) {
            List<String> rv = new ArrayList<>();
            for (String key : index.keySet()) {
                if (index.get(key).contains(url) && key != url) {
                    rv.add(key);
                }
            }
            result.put(url, rv);

        }
        return result;
    }

    private static final Pattern A_TAG = Pattern.compile("<a(.+?)</a>", Pattern.DOTALL);

    /**
     *
     * @param str asd
     * @return asd
     */
    private static List<String> getTagValues(final String str) {
        final List<String> tagValues = new ArrayList<>();
        final Matcher matcher = A_TAG.matcher(str);
        while (matcher.find()) {
            tagValues.add(matcher.group());
        }
        return tagValues;
    }
}