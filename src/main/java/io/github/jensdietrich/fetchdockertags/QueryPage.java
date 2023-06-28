package io.github.jensdietrich.fetchdockertags;

import java.util.List;
import java.util.Objects;

/**
 * Query result, representing the json response returned by a docker API query.
 * @author jens dietrich
 */
public class QueryPage {
    private int count = 0;
    private String next = null;
    private String previous = null;

    private List<Tag> results = null;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Tag> getResults() {
        return results;
    }

    public void setResults(List<Tag> results) {
        this.results = results;
    }
}
