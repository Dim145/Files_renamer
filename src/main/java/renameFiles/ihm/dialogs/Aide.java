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

    private final Font   font;
    private final Color  currentColor;
    private final JPanel contentPane;
    private final DefaultListModel<String> categorieModel;

    /**
     * Instantiates a new Aide.
     *
     * @param theme the theme
     * @param font  the font
     */
    public Aide(Color theme, Font font)
    {
        this.setTitle("Aide");

        this.font = font;

        this.contentPane = new JPanel(new CardLayout());

        JScrollPane scrollPane = new JScrollPane(contentPane);
        JSplitPane splitPane = new JSplitPane();

        this.add(splitPane);

        splitPane.add(scrollPane, JSplitPane.RIGHT);

        this.currentColor = theme;

        this.categorieModel = new DefaultListModel<>();

        this.setTree(splitPane, contentPane);
        this.setCardAndModelText();

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
        JList<String> listCategorie = new JList<>(categorieModel);

        listCategorie.addListSelectionListener(e ->
        {
            int index = listCategorie.getSelectedIndex();

            if( index > -1 )
                ((CardLayout) contentPane.getLayout()).show(content, categorieModel.get(index));
        });

        splitPane.add(listCategorie, JSplitPane.LEFT);
    }

    private void setCardAndModelText()
    {
        this.categorieModel.clear();
        this.contentPane.removeAll();

        categorieModel.addElement(MANAGER.getString(Resources.APP_NAME)); // categorie 1
        categorieModel.addElement(MANAGER.getHelpString(3)); // categorie 2
        categorieModel.addElement(MANAGER.getString(Resources.SERIES)); // categorie 3
        categorieModel.addElement(MANAGER.getString(Resources.ALEANAME)); // categorie 4
        categorieModel.addElement(MANAGER.getString(Resources.MENU_OPTION)); // categorie 5
        categorieModel.addElement("web");

        for (int i = 0; i < categorieModel.getSize(); i++)
            this.contentPane.add(getJLabelForCardNumber(i), categorieModel.get(i));

        this.setRecursiveColor(this);
    }

    private void setRecursiveColor(Component component)
    {
        component.setBackground(this.currentColor);
        component.setForeground(Color.WHITE == this.currentColor ? Color.BLACK : Color.WHITE);

        if (component instanceof Container)
        {
            for (int i = 0; i < ((Container) component).getComponentCount(); i++)
                setRecursiveColor(((Container) component).getComponent(i));
        }
    }

    private Component getJLabelForCardNumber(int number)
    {
        JLabel label = new JLabel();

        if (this.font != null) label.setFont(font);

        StringBuilder textBuilder = new StringBuilder();

        switch (number)
        {
            case 0:
            {
                textBuilder.append("<h1>").append(MANAGER.getString(Resources.APP_NAME)).append("</h1>").append(
                        "<p>").append(MANAGER.getHelpString(1)).append("</p>").append("<p>").append(
                        MANAGER.getHelpString(2)).append("</p>");

                label.setHorizontalAlignment(JLabel.CENTER);
            }
            break;

            case 1:
            {
                textBuilder.append("<h2>").append(MANAGER.getHelpString(3)).append("</h2>").append(
                        "<div class=\"marginLeft\">").append("<h3 id=\"autre-extensions\">").append(
                        MANAGER.getString(Resources.EXTENSIONS)).append("</h3>").append("<p>").append(
                        MANAGER.getHelpString(4)).append("</p>").append("<h3>").append(
                        MANAGER.getString(Resources.NAME_PATERN)).append("</h3>").append("<p>").append(
                        MANAGER.getHelpString(5)).append("</p>").append("<p>").append(MANAGER.getHelpString(6)).append(
                        "</p>").append("<p>").append(MANAGER.getHelpString(7)).append("</p>").append("<ul>").append(
                        "<li>").append(MANAGER.getHelpString(8)).append("</li>").append("<li>").append(
                        MANAGER.getHelpString(9)).append("</li>").append("<li>").append(
                        MANAGER.getHelpString(10)).append("</li>").append("</ul>").append("<p>").append(
                        MANAGER.getHelpString(11)).append("</p>").append("</div>");
            }
            break;

            case 2:
            {
                textBuilder.append("<h2>").append(MANAGER.getString(Resources.SERIES)).append("</h2>").append(
                        "<div class=\"marginLeft\">").append("<p>").append(MANAGER.getHelpString(12)).append(
                        "</p>").append("<p>").append(MANAGER.getHelpString(13)).append("</p>").append("<p>").append(
                        MANAGER.getHelpString(14)).append("</p>").append("<h3>").append(
                        MANAGER.getString(Resources.NAME_PATERN)).append("</h3>").append("<p>").append(
                        MANAGER.getHelpString(15)).append("</p>").append("<p>").append(
                        MANAGER.getHelpString(16)).append("</p>").append("<p>").append(
                        MANAGER.getHelpString(17)).append("</p>").append("</div>");
            }
            break;

            case 3:
            {
                textBuilder.append("<h2>").append(MANAGER.getString(Resources.ALEANAME)).append("</h2>").append(
                        "<div class=\"marginLeft\">").append("<p>").append(MANAGER.getHelpString(18)).append(
                        "</p>").append("<p>").append(MANAGER.getHelpString(19)).append("</p>").append("<p>").append(
                        MANAGER.getHelpString(20)).append("</p>").append("<p>").append(
                        MANAGER.getHelpString(21)).append("</p>").append("</div>");
            }
            break;

            case 4:
            {
                textBuilder.append("<h2>").append(MANAGER.getString(Resources.MENU_OPTION)).append("</h2>").append(
                        "<p>").append(MANAGER.getHelpString(22)).append("</p>").append("<ul>").append("<li>").append(
                        MANAGER.getHelpString(23)).append("</li>").append("<li>").append(
                        MANAGER.getHelpString(24)).append("</li>").append("<li>").append(
                        MANAGER.getHelpString(25)).append("</li>").append("<li>").append(
                        MANAGER.getHelpString(26)).append("</li>").append("<li>").append(
                        MANAGER.getHelpString(32)).append("<ul>").append("<li>").append(
                        MANAGER.getHelpString(33)).append("</li>").append("<li>").append(
                        MANAGER.getHelpString(34)).append("</li>").append("</li>").append("<li>").append(
                        MANAGER.getHelpString(27)).append(" ").append("<ul>").append("<li>").append(
                        MANAGER.getHelpString(28)).append("</li>").append("<li>").append(
                        MANAGER.getHelpString(29)).append("</li>").append("<li>").append(
                        MANAGER.getHelpString(30)).append("</li>").append("<li>").append(
                        MANAGER.getHelpString(31)).append("</li>").append("</ul>").append("</ul>");
            }
            break;

            case 5:
            {
                textBuilder.append("<h1>").append(MANAGER.getHelpString(35)).append("</h1>")
                           .append("<div class=\"marginLeft\">")
                           .append("<p>").append(MANAGER.getHelpString(36)).append("</p>")
                           .append("<p>").append(MANAGER.getHelpString(37)).append("</p>")
                           .append("<ul>")
                           .append("<li>").append(MANAGER.getHelpString(38)).append("</li>")
                           .append("<li>").append(MANAGER.getHelpString(39)).append("</li>")
                           .append("<li>").append(MANAGER.getHelpString(40)).append("</li>")
                           .append("</ul>")
                           .append("<p>").append(MANAGER.getHelpString(41)).append("</p>");

                JLabel refSerie = new JLabel(transformStringToHTMLString("<a href=\"http://tvmaze.com\">TVMaze</a>"));
                JLabel refFilms = new JLabel(
                        transformStringToHTMLString("<a href=\"https://themoviedb.org\">TheMovieDB</a>"));

                refSerie.setToolTipText("http://tvmaze.com");
                refFilms.setToolTipText("https://themoviedb.org");

                refSerie.addMouseListener(new MouseAdapter()
                {
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

                JLabel endSerie = new JLabel(transformStringToHTMLString(MANAGER.getHelpString(42)));
                JLabel endFilms = new JLabel(transformStringToHTMLString(MANAGER.getHelpString(43)));

                GridBagLayout layout = new GridBagLayout();
                JPanel pnl = new JPanel(new BorderLayout());
                JPanel grid = new JPanel(layout);
                JPanel box = new JPanel(new FlowLayout(FlowLayout.LEFT));

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
        body = "<html>" + "<head>" + "<style>" + "body{ " + "background-color: " + (this.currentColor == Color.WHITE ? "while" : "rgb(" + this.currentColor.getRed() + "," + this.currentColor.getGreen() + "," + this.currentColor.getBlue() + ")") + ";" + "}" + "h2{" + "color:orange;" + "}" + "h1{" + "color:orange; " + "text-align:center;" + "}" + "h3{" + "color:orange; " + "}" + "p{" + "text-align:justify;" + "margin-left:10px; " + "margin-right:10px;" + "}" + ".marginLeft{" + "margin-left: 25px;" + "}" + "</style>" + "</head>" + "<body>" + body + "</body>" + "</html>";

        return body;
    }

    @Override
    public void setNewText()
    {
        this.setCardAndModelText();
    }
}
