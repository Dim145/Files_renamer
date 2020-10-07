package renameFiles.ihm;

import javax.swing.*;

public class Picker extends JFileChooser
{

    public Picker()
    {
        this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.setMultiSelectionEnabled(true);
    }

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
