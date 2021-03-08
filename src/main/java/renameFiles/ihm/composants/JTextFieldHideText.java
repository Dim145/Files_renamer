package renameFiles.ihm.composants;

import renameFiles.ihm.IHMGUI;
import renameFiles.metier.FileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * The type J text field hide text.
 */
public class JTextFieldHideText extends JTextField implements FocusListener
{
    private final IHMGUI ihm;
    private final String textFocusLost;

    /**
     * Instantiates a new J text field hide text.
     *
     * @param ihm            the ihm
     * @param basePath       the base path
     * @param texteFocusLost the texte focus lost
     */
    public JTextFieldHideText(IHMGUI ihm, String basePath, String texteFocusLost )
    {
        super(basePath);

        this.ihm = ihm;

        this.textFocusLost = texteFocusLost;

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
        this(ihm, basePath, "Non Obligatoire");
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
            this.setText(this.textFocusLost);
        }
    }
}
