package renameFiles.ihm.dialogs;

import renameFiles.metier.resources.ResourceManager;
import renameFiles.metier.resources.Resources;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * The type A propos.
 */
public class APropos extends JOptionPane
{
    /**
     * Instantiates a new A propos.
     *
     * @param background the background
     */
    public APropos(Color background)
    {
        ResourceManager manager = ResourceManager.getInstance();

        String sAff = "<HTML><BODY><FONT COLOR=\"RED\"> <FONT SIZE=\"+1\"> <B>"+manager.getString(Resources.APP_NAME)+"</B> </FONT> </FONT></BODY></HTML>\n\n\n\n" +
                "<HTML><BODY><FONT COLOR=\""+ ( background.equals(Color.WHITE) ? "black" : "white") + "\">"+manager.getString(Resources.CREATEDBY)+":    Dimitri  Dubois</FONT> </BODY></HTML>\n\n"                   +
                "<HTML><BODY><FONT COLOR=\""+ ( background.equals(Color.WHITE) ? "black" : "white") + "\">"+manager.getString(Resources.DEVELOPEDIN)+" 2020-2021</FONT> </BODY></HTML></FONT> </BODY></HTML> \n" +
                "<HTML><BODY><FONT COLOR=\""+ ( background.equals(Color.WHITE) ? "black" : "white") + "\">"+manager.getString(Resources.VERSION)+" 4.0</FONT> </BODY></HTML>";

        UIResource basePane  = (UIResource) UIManager.getColor( "OptionPane.background" );
        UIResource basePanel = (UIResource) UIManager.getColor( "Panel.background" );

        UIManager.put("OptionPane.background", background);
        UIManager.put("Panel.background"     , background);

        showMessageDialog( null, sAff, manager.getString(Resources.ABOUT_TITLE), JOptionPane.INFORMATION_MESSAGE, new ImageIcon( APropos.class.getResource(
                "/images/Files_renamer.png") ) );

        UIManager.put("OptionPane.background", basePane );
        UIManager.put("Panel.background"     , basePanel);
    }
}

