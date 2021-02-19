package renameFiles.ihm;

import javax.swing.*;
import java.awt.*;

public class Aide extends JDialog
{
    private final JLabel label;
    private final Color currentColor;

    public Aide( Color theme, Font font )
    {
        this.setTitle("Aide");

        this.label = new JLabel();

        this.add(this.label);

        this.currentColor = theme ;
        this.setHTMLText();

        this.label.setForeground(theme == Color.WHITE ? Color.BLACK : Color.WHITE);

        if( font != null ) this.label.setFont(font);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

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
                            "p{" +
                                "text-align:justify;" +
                                "margin-left:10px; " +
                                "margin-right:10px;" +
                            "}" +
                        "</style>" +
                    "</head>" +
                    "<body>" +
                        "<h1>File renamer</h1>" +
                        "<p>Cette application permet de renomer tous un ensemble de fichiers de la même façon.</p>" +
                        "<p>Pour cela il faut juste choisir le repertoire ou sont les fichiers à renomer. </p>" +
                        "<h2>Les extensions</h2>" +
                        "<p>Les fichiers pris en compte peuvent etre filtré selon leurs extensions. Plusieurs " +
                        "peuvent être choisi, il suffit de les séparé par une \",\"</p>" +
                        "<h2>Le nom</h2>" +
                        "<p>Le nom seras appliqué a tous les fichiers du dossiers, plus ceux des sous-dossiers. " +
                        "Les eventuels chiffres ou nombres devrons etre remplacer par les deux caractères \"%%\".</p>" +
                        "<p>Par exemple: fichier n°%%.txt</p>" +
                        "<p> Si nous avons 3 fichiers avec un chiffre allant de 1 à 3 dans le dossier courrant, les noms seront donc:</p>" +
                        "<ul><li>fichier n°1.txt</li><li>fichier n°2.txt</li><li>fichier n°3.txt</li></ul>" +
                        "<p>P.S: Le programme prend en compte les nombres a virgule. (\",\" ou \".\")</p>" +
                        "<h2>Options</h2>" +
                        "<p>Il y as 2 options supplémentaires:</p>" +
                        "<ul>" +
                            "<li>Un thème sombre pour ceux qui ont des yeux sensibles.</li>" +
                            "<li>Une option de sécurité qui permet de ne pas renomer un fichier si la quantité de nombre ne correspond pas a celle de \"%%\"</li>" +
                        "</ul>" +
                    "</body>" +
                "</html>";

        this.label.setText(html);
    }

    private String construireHTML()
    {
        return "<html>" + "<head>" + "<style>" + "body{ " + "background-color:white; margin-bottom:20px;" + "}" + "h2{"
                + "color:orange;" + "}" + "h1{" + "color:orange; text-align:center;" + "}" + "p{" + "text-align:justify;" + "margin-left:10px; " + "margin-right:10px;"
                + "}" + "</style>" + "</head>" +

                "<body>" + "<h1>Les Bouboules</h1>"
                + "<h2>Bienvenue !</h2>"
                + "<p>Ce jeu a ete realiser par Dimitri Dubois, Ladislas Morcamp, Thomas Leray et Tracy Joo.</p>"
                + "<h2>Principe du jeu</h2>"
                + "<p>Il s'agit d'un jeu multijoueurs (au moins 2) en reseau dont l'objectif est la collecte de <i>n</i> objets au cours de la partie. Le joueur qui gagne est celui qui a collecte le plus d'items.</p>"
                + "</body>" + "</html>";
    }
}
