package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.muni.fi.pb162.hw02.TestUtils.buildIndex;
import static cz.muni.fi.pb162.hw02.TestUtils.prefix;

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
        String[] expected = prefix("http://localhost:8080/t01/", "trpaplaneta.html", "web1.html");
        Assertions.assertThat(index).containsExactlyInAnyOrder(expected);
    }

    @Test
    public void shouldGetCompleteIndexWithoutCycle() {

    }

    @Test
    public void shouldGetCompleteIndexWithCycle() {
        Map<String, List<String>> index = crawler.crawlAll("http://localhost:8080/t01/web2.html");
        String[] expected = prefix(
                "http://localhost:8080/t01/",
                "aloe.html", "bert.html", "cirok.html", "opium.html", "trpaplaneta.html", "web1.html", "web2.html"
        );
        Assertions.assertThat(index.keySet()).containsExactlyInAnyOrder(expected);
    }

    @Test
    public void shouldGetListWithSelfReference() {
        List<String> index = crawler.crawl("http://localhost:8080/t01/trpaplaneta.html");
        String[] expected = prefix("http://localhost:8080/t01/", "trpaplaneta.html", "aloe.html");
        Assertions.assertThat(index).containsExactlyInAnyOrder(expected);
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
        Map<String, List<String>> index = buildIndex(
                "http://localhost:8080/t01/", ".html",
                "aloe: cirok",
                "bert: opium trpaplaneta",
                "cirok: aloe web1 bert",
                "opium: bert cirok aloe opium"

        );
        Map<String, List<String>> expected = buildIndex(
                "http://localhost:8080/t01/", ".html",
                "aloe:  cirok opium",
                "bert:  cirok opium",
                "cirok: aloe opium",
                "opium: bert opium"
        );
        Map<String, List<String>> reverseIndex = crawler.reverseIndex(index);

        expected.forEach( (k, v) -> {
            Assertions.assertThat(reverseIndex.keySet()).contains(k);
            Assertions.assertThat(reverseIndex.get(k)).containsExactlyInAnyOrderElementsOf(v);
        });

    }

    @Test
    public void shouldBuildReverseIndexByCrawl() {
        Map<String, List<String>> expected = buildIndex(
                "http://localhost:8080/t01/", ".html",
                "aloe:  cirok opium trpaplaneta",
                "bert:  cirok opium",
                "cirok: aloe opium",
                "opium: bert opium",
                "web1:  cirok web2",
                "web2: web1",
                "trpaplaneta: bert trpaplaneta web2"
        );
        Map<String, List<String>> reverseIndex = crawler.crawlReverse("http://localhost:8080/t01/web2.html");

        expected.forEach( (k, v) -> {
            Assertions.assertThat(reverseIndex.keySet()).contains(k);
            Assertions.assertThat(reverseIndex.get(k)).containsExactlyInAnyOrderElementsOf(v);
        });
    }

}
