package renameFiles.ihm.dialogs;

import renameFiles.metier.resources.ResourceManager;
import renameFiles.metier.resources.Resources;
import renameFiles.metier.resources.Traduisible;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The type Aide.
 */
public class Aide extends JDialog implements Traduisible
{
    private final static ResourceManager MANAGER = ResourceManager.getInstance();

    private final JLabel label;
    private final Color currentColor;

    /**
     * Instantiates a new Aide.
     *
     * @param theme the theme
     * @param font  the font
     */
    public Aide( Color theme, Font font )
    {
        this.setTitle("Aide");

        this.label = new JLabel();

        JScrollPane pane = new JScrollPane(this.label);

        this.add(pane);

        this.currentColor = theme ;
        this.setHTMLText();

        this.setRecursiveColor(theme, this);

        if( font != null ) this.label.setFont(font);

        this.pack();
        this.setSize(this.getWidth(), 750);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        ResourceManager.getInstance().addObjectToTranslate(this);

        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                super.windowClosed(e);

                ResourceManager.getInstance().removeObjectToTranlate(Aide.this);
            }
        });
    }

    private void setRecursiveColor( Color color, Component component)
    {
        component.setBackground(color);
        component.setForeground(Color.WHITE == color ? Color.BLACK : Color.WHITE);

        if( component instanceof Container)
        {
            for (int i = 0; i < ((Container) component).getComponentCount(); i++)
                setRecursiveColor(color, ((Container) component).getComponent(i));
        }
    }

    //Todo utiliser les fichiers de ressources
    private void setHTMLText()
    {
        String html =
                "<html>" +
                    "<head>" +
                        "<style>" +
                            "body{ " +
                                "background-color: " + (this.currentColor == Color.WHITE ? "while" : "rgb(" + this.currentColor.getRed() + "," + this.currentColor.getGreen() + "," + this.currentColor.getBlue() + ")") + ";" +
                            "}" +
                            "h2{" +
                                "color:orange;" +
                            "}" +
                            "h1{" +
                                "color:orange; " +
                                "text-align:center;" +
                            "}" +
                            "h3{" +
                                "color:orange; " +
                            "}" +
                            "p{" +
                                "text-align:justify;" +
                                "margin-left:10px; " +
                                "margin-right:10px;" +
                            "}" +
                            ".marginLeft{" +
                                "margin-left: 25px;" +
                            "}" +
                        "</style>" +
                    "</head>" +
                    "<body>" +
                        "<h1>" + MANAGER.getString(Resources.APP_NAME) + "</h1>" +
                        "<p>"  + MANAGER.getHelpString(1) + "</p>" +
                        "<p>"  + MANAGER.getHelpString(2) + "</p>" +
                        "<h2>" + MANAGER.getHelpString(3) + "</h2>" +
                        "<div class=\"marginLeft\">" +
                        "<h3 id=\"autre-extensions\">" + MANAGER.getString(Resources.EXTENSIONS) + "</h3>" +
                        "<p>"  + MANAGER.getHelpString(4) + "</p>" +
                        "<h3>" + MANAGER.getString(Resources.NAME_PATERN) + "</h3>" +
                        "<p>"  + MANAGER.getHelpString(5) + "</p>" +
                        "<p>"  + MANAGER.getHelpString(6) + "</p>" +
                        "<p>"  + MANAGER.getHelpString(7) + "</p>" +
                        "<ul>" +
                            "<li>"+ MANAGER.getHelpString(8)  +"</li>" +
                            "<li>"+ MANAGER.getHelpString(9)  +"</li>" +
                            "<li>"+ MANAGER.getHelpString(10) +"</li>" +
                        "</ul>" +
                        "<p>"+ MANAGER.getHelpString(11) +"</p>" +
                        "</div>" +
                        "<h2>" + MANAGER.getString(Resources.SERIES) + "</h2>" +
                        "<div class=\"marginLeft\">" +
                        "<p>"+ MANAGER.getHelpString(12) +"</p>" +
                        "<p>"+ MANAGER.getHelpString(13) +"</p>" +
                        "<p>"+ MANAGER.getHelpString(14) +"</p>" +
                        "<h3>" + MANAGER.getString(Resources.NAME_PATERN) + "</h3>" +
                        "<p>"+ MANAGER.getHelpString(15) +"</p>" +
                        "<p>"+ MANAGER.getHelpString(16) +"</p>" +
                        "<p>"+ MANAGER.getHelpString(17) +"</p>" +
                        "</div>" +
                        "<h2>" + MANAGER.getString(Resources.ALEANAME) + "</h2>" +
                        "<div class=\"marginLeft\">" +
                        "<p>"+ MANAGER.getHelpString(18) +"</p>" +
                        "<p>"+ MANAGER.getHelpString(19) +"</p>" +
                        "<p>"+ MANAGER.getHelpString(20) +"</p>" +
                        "<p>"+ MANAGER.getHelpString(21) +"</p>" +
                        "</div>" +
                        "<h2>" + MANAGER.getString(Resources.MENU_OPTION) + "</h2>" +
                        "<p>"+ MANAGER.getHelpString(22) +"</p>" +
                        "<ul>" +
                            "<li>" + MANAGER.getHelpString(23) + "</li>" +
                            "<li>" + MANAGER.getHelpString(24) + "</li>" +
                            "<li>" + MANAGER.getHelpString(25) + "</li>" +
                            "<li>" + MANAGER.getHelpString(26) + "</li>" +
                            "<li>" + MANAGER.getHelpString(27) + " " +
                                "<ul>" +
                                    "<li>" + MANAGER.getHelpString(28) + "</li>" +
                                    "<li>" + MANAGER.getHelpString(29) + "</li>" +
                                    "<li>" + MANAGER.getHelpString(30) + "</li>" +
                                    "<li>" + MANAGER.getHelpString(31) + "</li>" +
                        "</ul>" +
                    "</body>" +
                "</html>";

        this.label.setText(html);
    }

    @Override
    public void setNewText()
    {
        this.setHTMLText();
    }
}
