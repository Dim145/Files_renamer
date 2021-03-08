package renameFiles.ihm;

import javax.swing.*;

/**
 * The type Picker.
 */
public class Picker extends JFileChooser
{
    /**
     * Instantiates a new Picker.
     */
    public Picker()
    {
        this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.setMultiSelectionEnabled(false);
        this.setFileHidingEnabled(false);
    }

    /**
     * Pick a directory string.
     *
     * @return the string
     */
    public String pickADirectory()
    {
        int action = this.showOpenDialog(null);

        if( action == APPROVE_OPTION )
        {
            return this.getSelectedFile().getPath();
        }
        else return null;
    }
}
