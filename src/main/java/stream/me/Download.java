package stream.me;

import java.io.File;
import java.net.URL;

public class Download {


    private String title = null;
    private URL url;
    private File destination = null;
    private Status status = Status.WAITING;

    public Download(URL url, File destination) {
        this.url = url;
        this.destination = destination;
    }

    public String getTitle() {
        return title == null ? "No Title Yet" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public File getDestination() {
        return destination;
    }

    public void setDestination(File destination) {
        this.destination = destination;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
