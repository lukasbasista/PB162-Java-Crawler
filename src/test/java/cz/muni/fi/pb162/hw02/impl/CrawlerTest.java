package cz.muni.fi.pb162.hw02.impl;

import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 *
 * @author Jakub Cechacek
 */
public class CrawlerTest {

    private Crawler crawler;
    private static TestServer server;


    @BeforeClass
    public static void startWebServer() {
        server = new TestServer();
        server.start();
    }

    @AfterClass
    public static void stopWebServer() {
        server.stop();
    }

    @Before
    public void setup() {
        this.crawler = new Crawler();
    }

    @Test
    public void shouldProcessEmptyPage() {
        List<String> index = crawler.crawl("http://localhost:8080/t00/web1.html");
        Assertions.assertThat(index).isEmpty();
    }

    @Test
    public void shouldHandleInvalidUrl() {
        List<String> index = crawler.crawl("http://localhost:8080/tXX/web1.html");
        Assertions.assertThat(index).isEmpty();
    }

    @Test
    public void shouldGetListOfLinks() {
        List<String> index = crawler.crawl("http://localhost:8080/t01/web2.html");
        Assertions.assertThat(index).containsExactlyInAnyOrder("http://localhost:8080/trpaplaneta.html", "http://localhost:8080/web1.html");
    }

    @Test
    public void shouldGetCompleteIndex() {
        Map<String, List<String>> index = crawler.crawlAll("http://localhost:8080/t01/web2.html");
        String[] expected = prefix(
                "http://localhost:8080/t01/",
                "aloe.html", "bert.html", "cirok.html", "opium.html", "trpaplaneta.html", "web1.html", "web2.html"
        );
        Assertions.assertThat(index.keySet()).containsExactlyInAnyOrder(expected);
    }

    private String[] prefix(String prefix, String... values) {
        return Arrays.stream(values).map(s -> prefix + s).toArray(String[]::new);
    }

    @Test
    public void shouldGetCompleteIndexWithSelfReference() {

    }

    @Test
    public void shouldGetCompleteIndexWithCycle() {

    }

    @Test
    public void shouldBuildEmptyReverseIndexFromEmptyIndex() {
        Map<String, List<String>> reverse = crawler.reverseIndex(Collections.emptyMap());
        Assertions.assertThat(reverse).isEmpty();
    }

    @Test
    public void shouldBuildEmptyReverseIndex() {

    }

    @Test
    public void shouldBuildReverseIndexFromFullyLinkedIndex() {

    }

    @Test
    public void shouldBuildReverseIndex() {

    }

    @Test
    public void shouldBuildReverseIndexByCrawl() {

    }

}