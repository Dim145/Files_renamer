package renameFiles.ihm.dialogs;

import renameFiles.metier.resources.ResourceManager;
import renameFiles.metier.resources.Resources;
import renameFiles.metier.resources.Traduisible;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The type Aide.
 */
public class Aide extends JDialog implements Traduisible
{
    private final static ResourceManager MANAGER = ResourceManager.getInstance();

    private final Color currentColor;

    private Font font;
    /**
     * Instantiates a new Aide.
     *
     * @param theme the theme
     * @param font  the font
     */
    public Aide( Color theme, Font font )
    {
        this.setTitle("Aide");

        this.font = font;

        JPanel panel = new JPanel(new CardLayout());

        JScrollPane pane      = new JScrollPane(panel);
        JSplitPane  splitPane = new JSplitPane();

        this.add(splitPane);

        splitPane.add(pane, JSplitPane.RIGHT);

        this.currentColor = theme ;

        this.setTree(splitPane, panel);

        this.setRecursiveColor(theme, this);

        this.pack();
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

    private void setTree(JSplitPane splitPane, JPanel content)
    {
        CardLayout c = (CardLayout) content.getLayout();

        DefaultListModel<String> categorieModel = new DefaultListModel<>();

        categorieModel.addElement(MANAGER.getString(Resources.APP_NAME)); // categorie 1
        categorieModel.addElement(MANAGER.getHelpString(3)); // categorie 2
        categorieModel.addElement(MANAGER.getString(Resources.SERIES)); // categorie 3
        categorieModel.addElement(MANAGER.getString(Resources.ALEANAME)); // categorie 4
        categorieModel.addElement(MANAGER.getString(Resources.MENU_OPTION)); // categorie 5
        categorieModel.addElement("web");

        for (int i = 0; i < categorieModel.getSize(); i++)
            content.add(getJLabelForCardNumber(i), categorieModel.get(i));

        JList<String> listCategorie = new JList<>(categorieModel);

        listCategorie.addListSelectionListener(e ->
        {
            int index = listCategorie.getSelectedIndex();
            System.out.println(index);

            c.show(content, categorieModel.get(index));
        });

        splitPane.add(listCategorie, JSplitPane.LEFT);
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

    private Component getJLabelForCardNumber(int number)
    {
        JLabel label = new JLabel();

        if (this.font != null)
            label.setFont(font);

        StringBuilder textBuilder = new StringBuilder();

        switch (number)
        {
            case 0:
            {
                textBuilder.append("<h1>").append(MANAGER.getString(Resources.APP_NAME)).append("</h1>")
                           .append("<p>").append(MANAGER.getHelpString(1)).append("</p>")
                           .append("<p>").append(MANAGER.getHelpString(2)).append("</p>");

                label.setHorizontalAlignment(JLabel.CENTER);
            }break;

            case 1:
            {
                textBuilder.append("<h2>").append(MANAGER.getHelpString(3)).append("</h2>")
                           .append("<div class=\"marginLeft\">")
                               .append("<h3 id=\"autre-extensions\">").append(MANAGER.getString(Resources.EXTENSIONS)).append("</h3>")
                               .append("<p>").append(MANAGER.getHelpString(4)).append("</p>")
                               .append("<h3>").append(MANAGER.getString(Resources.NAME_PATERN)).append("</h3>")
                               .append("<p>").append(MANAGER.getHelpString(5)).append("</p>")
                               .append("<p>").append(MANAGER.getHelpString(6)).append("</p>")
                               .append("<p>").append(MANAGER.getHelpString(7)).append("</p>")
                               .append("<ul>")
                                   .append("<li>").append(MANAGER.getHelpString(8)).append("</li>")
                                   .append("<li>").append(MANAGER.getHelpString(9)).append("</li>")
                                   .append("<li>").append(MANAGER.getHelpString(10)).append("</li>")
                               .append("</ul>")
                               .append("<p>").append(MANAGER.getHelpString(11)).append("</p>")
                           .append("</div>");
            }break;

            case 2:
            {
                textBuilder.append("<h2>").append(MANAGER.getString(Resources.SERIES)).append("</h2>")
                           .append("<div class=\"marginLeft\">")
                               .append("<p>").append(MANAGER.getHelpString(12)).append("</p>")
                               .append("<p>").append(MANAGER.getHelpString(13)).append("</p>")
                               .append("<p>").append(MANAGER.getHelpString(14)).append("</p>")
                               .append("<h3>").append(MANAGER.getString(Resources.NAME_PATERN)).append("</h3>")
                               .append("<p>").append(MANAGER.getHelpString(15)).append("</p>")
                               .append("<p>").append(MANAGER.getHelpString(16)).append("</p>")
                               .append("<p>").append(MANAGER.getHelpString(17)).append("</p>")
                           .append("</div>");
            }break;

            case 3:
            {
                textBuilder.append("<h2>").append(MANAGER.getString(Resources.ALEANAME)).append("</h2>")
                           .append("<div class=\"marginLeft\">")
                               .append("<p>").append(MANAGER.getHelpString(18)).append("</p>")
                               .append("<p>").append(MANAGER.getHelpString(19)).append("</p>")
                               .append("<p>").append(MANAGER.getHelpString(20)).append("</p>")
                               .append("<p>").append(MANAGER.getHelpString(21)).append("</p>")
                           .append("</div>");
            }break;

            case 4:
            {
                textBuilder.append("<h2>").append(MANAGER.getString(Resources.MENU_OPTION)).append("</h2>")
                           .append("<p>").append(MANAGER.getHelpString(22)).append("</p>")
                           .append("<ul>")
                               .append("<li>").append(MANAGER.getHelpString(23)).append("</li>")
                               .append("<li>").append(MANAGER.getHelpString(24)).append("</li>")
                               .append("<li>").append(MANAGER.getHelpString(25)).append("</li>")
                               .append("<li>").append(MANAGER.getHelpString(26)).append("</li>")
                               .append("<li>").append(MANAGER.getHelpString(32))
                               .append("<ul>")
                                   .append("<li>").append(MANAGER.getHelpString(33)).append("</li>")
                                   .append("<li>").append(MANAGER.getHelpString(34)).append("</li>").append("</li>")
                                   .append("<li>").append(MANAGER.getHelpString(27)).append(" ")
                               .append("<ul>")
                                   .append("<li>").append(MANAGER.getHelpString(28)).append("</li>")
                                   .append("<li>").append(MANAGER.getHelpString(29)).append("</li>")
                                   .append("<li>").append(MANAGER.getHelpString(30)).append("</li>")
                                   .append("<li>").append(MANAGER.getHelpString(31)).append("</li>")
                               .append("</ul>")
                           .append("</ul>");
            }break;

            case 5:
            {
                textBuilder.append("<h1>Fonctionnalité WEB</h1>")
                           .append("<div class=\"marginLeft\">")
                                .append("<p>L'application peut tenter de se connecter a internet pour chercher les différente vidéos.</p>")
                           .append("<p>Cela lui permet:</p><ul><li>")
                            .append("de corrigé le nom de la serie si celui-ci n'est pas trop déformé</li>")
                           .append("<li>d'ajouter ou de retiré le titre d'un episode selon la checkbox dans web.</li>")
                           .append("<li>de traduire si cela est possible, les nom de series. si la langue choisie est Japonnais,<br/> cela traduira en caractere japonnnais seulement si la langue actuelle est japonnais.</li>")
                           .append("</ul>")
                           .append("<p>les deux sites ou sont récupéré les données sont: </p>");

               JLabel refSerie = new JLabel(transformStringToHTMLString("<a href=\"http://tvmaze.com\">TVMaze</a>"));
               JLabel refFilms = new JLabel(transformStringToHTMLString("<a href=\"https://themoviedb.org\">TheMovieDB</a>"));

               refSerie.setToolTipText("http://tvmaze.com");
               refFilms.setToolTipText("https://themoviedb.org");

               refSerie.addMouseListener(new MouseAdapter() {
                   @Override
                   public void mouseClicked(MouseEvent e)
                   {
                       String text = ((JLabel) e.getSource()).getToolTipText();

                       try
                       {
                           Desktop.getDesktop().browse(new URI(text));
                       }
                       catch (IOException | URISyntaxException ioException)
                       {
                           ioException.printStackTrace();
                       }
                   }

                   @Override
                   public void mouseEntered(MouseEvent e)
                   {
                       ((JLabel) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                   }

                   @Override
                   public void mouseExited(MouseEvent e)
                   {
                       ((JLabel) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                   }
               });

                for (MouseListener m : refSerie.getMouseListeners())
                    refFilms.addMouseListener(m);

               JLabel endSerie = new JLabel(transformStringToHTMLString("pour les séries"));
               JLabel endFilms = new JLabel(transformStringToHTMLString("pour les films"));

               GridBagLayout layout = new GridBagLayout();
               JPanel pnl = new JPanel(new BorderLayout());
               JPanel grid = new JPanel(layout);
               JPanel box  = new JPanel(new FlowLayout(FlowLayout.LEFT));

               label.setText(transformStringToHTMLString(textBuilder.toString()));

                GridBagConstraints c = new GridBagConstraints();
                c.gridy = 1;
                layout.setConstraints(box, c);

               grid.add(label);
               box.add(refSerie);
               box.add(endSerie);

               grid.add(box);

               box = new JPanel(new FlowLayout(FlowLayout.LEFT));

               box.add(refFilms);
               box.add(endFilms);

                c = new GridBagConstraints();
                c.gridy = 2;
                layout.setConstraints(box, c);

               grid.add(box);
               pnl.add(grid, BorderLayout.NORTH);

               return pnl;
            }
        }

        label.setText(transformStringToHTMLString(textBuilder.toString()));

        return label;
    }

    private String transformStringToHTMLString(String body)
    {
        body =
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
                        body +
                    "</body>" +
                "</html>";

        return body;
    }

    @Override
    public void setNewText()
    {

    }
}
