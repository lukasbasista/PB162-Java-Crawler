package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public void shouldGetCompleteIndexWithSelfReference() {

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
