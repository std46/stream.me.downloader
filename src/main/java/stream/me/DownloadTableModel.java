package stream.me;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DownloadTableModel extends AbstractTableModel {
    private final List<Download> downloads;

    public DownloadTableModel(final List<Download> downloads) {

        this.downloads = downloads;
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    public int getRowCount() {
        return downloads.size();
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    public int getColumnCount() {
        return 3;
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {

        switch (columnIndex) {
            case 0:
                return downloads.get(rowIndex).getTitle();
            case 1:
                return downloads.get(rowIndex).getUrl();
            case 2:
                return downloads.get(rowIndex).getStatus();
            default:
                throw new RuntimeException("¯\\_(ツ)_/¯");
        }
    }
}
