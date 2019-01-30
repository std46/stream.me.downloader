package stream.me;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DownloadTable extends JTable {
    private final List<Download> downloads;


    public DownloadTable(List<Download> downloads) {
        super(new DownloadTableModel(downloads));
        Downloader.getInstance().setUpdateListener(this::downloadsUpdated);
        this.downloads = downloads;
        this.setPreferredSize(new Dimension(640, 480));
    }

    public void downloadsUpdated() {
        this.setModel(new DownloadTableModel(downloads));
    }
}
