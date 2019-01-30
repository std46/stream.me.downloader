package stream.me;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MainFrame extends JFrame {

    public static void main(String[] args) {
        //if using mac, we don't won't the menubar on the window
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        SwingUtilities.invokeLater(MainFrame::new);
    }

    public MainFrame() {
        LinkedBlockingQueue<Download> downloadQueue = Downloader.getInstance().getQueue();
        List<Download> downloadList = new ArrayList<>();

        final DownloadTable downloadTable = new DownloadTable(downloadList);

        this.add(new VideoInputForm(dl -> {
            try {
                downloadList.add(dl);
                downloadQueue.put(dl);
                downloadTable.downloadsUpdated();
            } catch (InterruptedException e) {
                throw new RuntimeException("¯\\_(ツ)_/¯");
            }
        }), BorderLayout.NORTH);

        this.add(downloadTable);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
}
