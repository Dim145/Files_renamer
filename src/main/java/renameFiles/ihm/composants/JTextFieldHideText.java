package renameFiles.ihm.composants;

import renameFiles.ihm.IHMGUI;
import renameFiles.metier.enums.FileType;
import renameFiles.metier.resources.Languages;
import renameFiles.metier.resources.ResourceManager;
import renameFiles.metier.resources.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * The type J text field hide text.
 */
public class JTextFieldHideText extends JTextField implements FocusListener, Languages
{
    private final IHMGUI ihm;
    private final Resources resLostFocusText;

    /**
     * Instantiates a new J text field hide text.
     *
     * @param ihm            the ihm
     * @param basePath       the base path
     */
    public JTextFieldHideText(IHMGUI ihm, String basePath, Resources resLostFocusText )
    {
        super(basePath);

        this.ihm = ihm;

        this.resLostFocusText = resLostFocusText;

        this.addFocusListener(this);
    }

    /**
     * Instantiates a new J text field hide text.
     *
     * @param ihm      the ihm
     * @param basePath the base path
     */
    public JTextFieldHideText(IHMGUI ihm, String basePath )
    {
        this(ihm, basePath, Resources.NO_REQUIRED);
    }

    /**
     * Instantiates a new J text field hide text.
     *
     * @param ihm the ihm
     */
    public JTextFieldHideText(IHMGUI ihm )
    {
        this(ihm, "");
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        if( this.getForeground().equals(Color.GRAY) && this.isEditable() )
        {
            this.setText("");
            Color baseColor = this.getBackground().equals(Color.WHITE) ? new Color(50, 50, 50) : Color.WHITE;

            this.setForeground(IHMGUI.couleurPlusClair(baseColor, baseColor.equals(Color.WHITE)));
        }
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        if( this.getText().isEmpty() && this.ihm.getCurrentType() == FileType.SERIES )
        {
            this.setForeground(Color.GRAY);
            this.setText(ResourceManager.getInstance().getString(this.resLostFocusText));
        }
    }

    @Override
    public void setNewText()
    {
        if( this.getForeground() == Color.GRAY )
            this.setText(ResourceManager.getInstance().getString(this.resLostFocusText));
    }
}
