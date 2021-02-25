package renameFiles.ihm;

import javax.swing.*;

import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import java.awt.*;

public class APropos extends JOptionPane
{
    public APropos(Color background)
    {
        String sAff = "<HTML><BODY><FONT COLOR=\"RED\"> <FONT SIZE=\"+1\"> <B>File Renamer</B> </FONT> </FONT></BODY></HTML>\n\n\n\n" +
                "<HTML><BODY><FONT COLOR=\""+ ( background.equals(Color.WHITE) ? "black" : "white") + "\">Créer par:    Dimitri  Dubois</FONT> </BODY></HTML>\n\n"                   +
                "<HTML><BODY><FONT COLOR=\""+ ( background.equals(Color.WHITE) ? "black" : "white") + "\">Développé en Octobre 2020</FONT> </BODY></HTML></FONT> </BODY></HTML> \n" +
                "<HTML><BODY><FONT COLOR=\""+ ( background.equals(Color.WHITE) ? "black" : "white") + "\">Version 1.4</FONT> </BODY></HTML>";

        UIResource basePane  = (UIResource) UIManager.getColor( "OptionPane.background" );
        UIResource basePanel = (UIResource) UIManager.getColor( "Panel.background" );

        UIManager.put("OptionPane.background", background);
        UIManager.put("Panel.background"     , background);

        showMessageDialog( null, sAff, "A propos...", JOptionPane.INFORMATION_MESSAGE, new ImageIcon( APropos.class.getResource("/Images/Files_renamer.png") ) );

        UIManager.put("OptionPane.background", basePane );
        UIManager.put("Panel.background"     , basePanel);
    }
}

