import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class TextAnalyzerTest {
    private String testString = "This is very very cool";
    private String testHtmlStringStart = "<h1>The Raven</h1>";
    private String testHtmlStringStop = "</div><!--end chapter-->";
    private String testUrlString = "https://www.gutenberg.org/files/1065/1065-h/1065-h.htm";
    private int testTopN = 20;
    private Map<String, Integer> mapTest = new HashMap<>();
    {
        mapTest.put("this", 1);
        mapTest.put("is", 1);
        mapTest.put("very", 2);
        mapTest.put("cool",1);
    }


    @Test
    void fromUrlWithCorrectValues() throws IOException {
        TreeMap<String, Integer> map = (TreeMap<String, Integer>) TextAnalyzer.getListOfWordFrequencyUsingURL(testUrlString, testHtmlStringStart,testHtmlStringStop, 20);
        assertTrue(map.firstEntry().getKey() == "the" || map.firstEntry().getValue() == 56);
    }


    @Test
    void fromUrlWithNoValues() throws IOException {
        //TreeMap<String, Integer> map = (TreeMap<String, Integer>) TextAnalyzer.getListOfWordFrequencyUsingURL(null, null,null, null);
        assertTrue(TextAnalyzer.getListOfWordFrequencyUsingURL(null, null,null, null) == null);
    }

    @Test
    void fromStringWithNoValues() {
        assertTrue(TextAnalyzer.getListOfWordFrequencyUsingText(null,null) == null);
    }

    @Test
    void fromStringWithCorrectValues() throws IOException {
        TreeMap<String, Integer> map = (TreeMap<String, Integer>) TextAnalyzer.getListOfWordFrequencyUsingURL(testUrlString, testHtmlStringStart,testHtmlStringStop, 20);

        //assertTrue(TextAnalyzer.getListOfWordFrequencyUsingText(testString, 20).size() == mapTest.size());
        assertTrue(TextAnalyzer.getListOfWordFrequencyUsingText(testString, 20).size() == map.size());
    }

    @Test
    void fromStringWithNegativeInput() {
        assertTrue(TextAnalyzer.getListOfWordFrequencyUsingText(testString, -1).size() == 0);
    }

}