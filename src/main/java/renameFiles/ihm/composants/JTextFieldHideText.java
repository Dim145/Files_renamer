package renameFiles.ihm.composants;

import renameFiles.ihm.IHMGUI;
import renameFiles.metier.FileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextFieldHideText extends JTextField implements FocusListener
{
    private final IHMGUI ihm;
    private final String textFocusLost;

    public JTextFieldHideText(IHMGUI ihm, String basePath, String texteFocusLost )
    {
        super(basePath);

        this.ihm = ihm;

        this.textFocusLost = texteFocusLost;

        this.addFocusListener(this);
    }

    public JTextFieldHideText(IHMGUI ihm, String basePath )
    {
        this(ihm, basePath, "Non Obligatoire");
    }

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
