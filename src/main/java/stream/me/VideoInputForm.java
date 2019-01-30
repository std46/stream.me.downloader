package stream.me;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class VideoInputForm extends JPanel {
    final static String LAST_DL_LOCATION = "LAST_DL_LOCATION";
    final Preferences preferences = Preferences.userRoot().node(this.getClass().getName());
    File destination = null;
    final JTextArea inputArea;
    final JButton addButton;
    final JButton setDownloadLocationButton;
    final JLabel downloadLocationLabel;


    public VideoInputForm(Consumer<Download> downloadConsumer) {

        this.setLayout(new BorderLayout());

        final JLabel infoLabel = new JLabel("Put each URL on a separate line");

        inputArea = new JTextArea();
        inputArea.setEnabled(false);

        final JScrollPane inputAreaScrollPane = new JScrollPane(inputArea);

        addButton = new JButton("Add");
        addButton.setEnabled(false);
        addButton.addActionListener(e -> {
            //check if empty or whitespace (String:isBlank() JDK 11)
            if (!inputArea.getText().trim().isEmpty()) {
                final String[] urlStrs = inputArea.getText().split(System.lineSeparator());
                final List<URL> urls = Arrays.stream(urlStrs).map(s -> {
                    try {
                        return new URL(s);
                    } catch (MalformedURLException e1) {
                        return null;
                    }
                }).collect(Collectors.toList());
                //ensure that all urls are valid first before we add any an reset the input
                if (urls.stream().allMatch(Objects::nonNull)) {
                    urls.forEach(u -> downloadConsumer.accept(new Download(u, destination)));
                    inputArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Found malformed URL(s)", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No URLs to add...");
            }
        });

        downloadLocationLabel = new JLabel("No Location set");


        setDownloadLocationButton = new JButton("Set Download Location");
        setDownloadLocationButton.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (jfc.showDialog(this, "Select Directory") == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = jfc.getSelectedFile();
                if (selectedFile.canWrite()) {
                    setDestination(selectedFile);
                    preferences.put(LAST_DL_LOCATION, selectedFile.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No write permissions on that directory",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        checkSetLastDownloadLocation();


        final JPanel controlPanel = new JPanel();

        final BoxLayout controlLayout = new BoxLayout(controlPanel, BoxLayout.X_AXIS);
        controlPanel.setLayout(controlLayout);

        controlPanel.add(addButton);
        controlPanel.add(setDownloadLocationButton);
        controlPanel.add(downloadLocationLabel);

        this.add(infoLabel, BorderLayout.NORTH);
        this.add(inputAreaScrollPane, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.SOUTH);


        this.setPreferredSize(new Dimension(0, 100));

    }

    private void setDestination(File file) {
        setDownloadLocation(file, null);
    }

    private void setDownloadLocation(File file, String extInfo) {
        destination = file;
        inputArea.setEnabled(true);
        addButton.setEnabled(true);
        setDownloadLocationButton.setText("Change Download Location");
        final String downloadLocationInfo = (extInfo == null ? "" : extInfo) + file.getAbsolutePath();
        downloadLocationLabel.setText(downloadLocationInfo);
    }

    private void checkSetLastDownloadLocation() {
        final String lastDlLocation = preferences.get(LAST_DL_LOCATION, null);
        if (lastDlLocation != null) {
            final File file = new File(lastDlLocation);
            setDownloadLocation(file, "(From Last Run)");
        }
    }
}
