
package in.mobileappdev.news.models;

import java.util.List;

public class SourcesResponce {

    private String status;
    private List<Source> sources = null;

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The sources
     */
    public List<Source> getSources() {
        return sources;
    }

    /**
     * 
     * @param sources
     *     The sources
     */
    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

}
