package renameFiles.ihm;

import renameFiles.metier.resources.Traduisible;
import renameFiles.metier.resources.ResourceManager;

import javax.swing.*;

/**
 * The type Picker.
 */
public class Picker extends JFileChooser implements Traduisible
{
    /**
     * Instantiates a new Picker.
     */
    public Picker()
    {
        this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.setMultiSelectionEnabled(false);
        this.setFileHidingEnabled(false);

        ResourceManager.getInstance().addObjectToTranslate(this);
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

    @Override
    public void setNewText()
    {
        this.setLocale(ResourceManager.getInstance().getLocale());
        this.updateUI();
    }
}
