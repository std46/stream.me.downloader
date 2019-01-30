package stream.me;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.LinkedBlockingQueue;

public class Downloader {

    private static Downloader instance = new Downloader();

    final private LinkedBlockingQueue<Download> queue = new LinkedBlockingQueue<>(100);

    private Runnable updateListener = null;

    public static Downloader getInstance() {
        return instance;
    }

    //https://www.stream.me/archive/andthaman/carrying-the-bald-genius-fort-later-or-andthaman-on-socials/aqJzv7E6qd
    private Downloader() {
        new Thread(() -> {
            while (true) {
                final Download download;
                final String title;
                try {
                    download = queue.take();
                    changeStatus(Status.DOWNLOADING_META, download);
                    try (WebClient client = new WebClient()) {
                        client.getOptions().setThrowExceptionOnScriptError(false);
                        final HtmlPage page = client.getPage(download.getUrl());
                        final ScriptResult scriptResult = page.executeJavaScript("__context");
                        final NativeObject obj = (NativeObject) scriptResult.getJavaScriptResult();
                        NativeObject archive = (NativeObject) obj.get("archive");
                        NativeObject embedded = (NativeObject) archive.get("_embedded");
                        final NativeArray vod = (NativeArray) embedded.get("vod");
                        title = (String) ((NativeObject) vod.get(0)).get("title");
                        SwingUtilities.invokeLater(() -> {
                            download.setTitle(title);
                            updateListener.run();
                        });
                        changeStatus(Status.DOWNLOADING_VIDEO, download);
                        NativeObject links = (NativeObject) ((NativeObject) vod.get(0)).get("_links");
                        String mp4Source = (String) ((NativeObject) links.get("source")).get("href");

                        FileUtils.copyURLToFile(new URL(mp4Source),
                                new File(download.getDestination(),
                                        download.getTitle()
                                                .replace('\\', '_')
                                                .replace('/', '_') + ".mp4"));
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                        changeStatus(Status.FAILURE, download);
                    }
                    changeStatus(Status.FINISHED, download);
                } catch (InterruptedException e) {
                    throw new RuntimeException("¯\\_(ツ)_/¯");
                }
            }
        }).start();
    }

    private void changeStatus(Status status, Download download) {
        SwingUtilities.invokeLater(() -> {
            //only modify download from within invokeLater to ensure no race conditions
            download.setStatus(status);
            if (updateListener != null) {
                updateListener.run();
            }
        });
    }

    public Runnable getUpdateListener() {
        return updateListener;
    }

    public void setUpdateListener(Runnable updateListener) {
        this.updateListener = updateListener;
    }

    public LinkedBlockingQueue<Download> getQueue() {
        return queue;
    }
}
