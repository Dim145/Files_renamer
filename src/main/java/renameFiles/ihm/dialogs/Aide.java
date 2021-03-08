package renameFiles.ihm.dialogs;

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
                        "<h1>File renamer</h1>" +
                        "<p>Cette application permet de renomer tous un ensemble de fichiers de la même façon.</p>" +
                        "<p>Pour cela il faut juste choisir le repertoire ou sont les fichiers à renomer. </p>" +
                        "<h2>Les fichiers non répertoriés:</h2>" +
                        "<div class=\"marginLeft\">" +
                        "<h3 id=\"autre-extensions\">Les extensions</h3>" +
                        "<p>Les fichiers pris en compte peuvent etre filtré selon leurs extensions. Plusieurs " +
                        "peuvent être choisi, il suffit de les séparé par une \",\"</p>" +
                        "<h3>Le nom</h3>" +
                        "<p>Le nom seras appliqué a tous les fichiers du dossiers, plus ceux des sous-dossiers. " +
                        "Les eventuels chiffres ou nombres devrons etre remplacer par les deux caractères \"%%\".</p>" +
                        "<p>Par exemple: fichier n°%%.txt</p>" +
                        "<p> Si nous avons 3 fichiers avec un chiffre allant de 1 à 3 dans le dossier courrant, les noms seront donc:</p>" +
                        "<ul><li>fichier n°1.txt</li><li>fichier n°2.txt</li><li>fichier n°3.txt</li></ul>" +
                        "<p>P.S: Le programme prend en compte les nombres a virgule. (\",\" ou \".\")</p>" +
                        "</div>" +
                        "<h2>Les séries</h2>" +
                        "<div class=\"marginLeft\">" +
                        "<p>Les vidéos ont un fonctionnement totalement automatique. Le renommage se feras en suivant le précepte suivant:" +
                        "<p> NomDeLaSerie Saison Episode Qualité Compression.extension </p>" +
                        "<p> ce qui donne par exemple: Test S1 Ep01 720p x265.mkv</p><br/>" +
                        "<p>P.S: Si cette norme ne vous plait pas, il est possible de renommer les vidéos en selectionnant la catégorie \"Autres\" et en mettant les extensions correct.</p>" +
                        "<h3>Le nom</h3>" +
                        "<p>Le nom n'est pas obligatoire. Il seras automatique récupé et adapter selon le nom de fichier.</p>" +
                        "<p>par exemple: [Une Team] Test (TV) S1 episode 5 1080p donneras -> Test S1 Ep 5 1080p</p>" +
                        "<p>Si vous choisissez de mettre un nom dans le champs \"paterne\", se seras celui-ci que seras mis au détriment de l'ancien.</p>" +
                        "</div>" +
                        "<h2>Nom aléatoires</h2>" +
                        "<div class=\"marginLeft\">" +
                        "<p>Cette sections est plus situationnel. Elle permet de renommée un fichier avec un numéro aléatoire devant le nom du fichier.</p>" +
                        "<p>Le numéro et le nom sont séparé par un \"-\", si un numéro est deja présent, il seras remplacé par un nouveau si la case a chocher \"save nb if exist\" est décoché</>" +
                        "<p>La zones de saisie d'extension fonctionne de la même manière que pour \"Autres\", et aucun paterne ne peut etre saisie.</p>" +
                        "<p>Cette section peut etre utile pour de vieux lecteur radio ou de vieux logiciels qui ne savent pas lire de façon aléatoire...</p>" +
                        "</div>" +
                        "<h2>Options</h2>" +
                        "<p>Il y as 5 options supplémentaires:</p>" +
                        "<ul>" +
                            "<li>Un thème sombre pour ceux qui ont des yeux sensibles.</li>" +
                            "<li>Une option de sécurité qui permet de ne pas renomer un fichier si la quantité de nombre ne correspond pas a celle de \"%%\"</li>" +
                            "<li>Une option pour remplacer tous les points par des espaces (extension non incluse) [Pour séries].</li>" +
                            "<li>La possibilité de sauvegarder l'eventuel nombre deja présent dans le nom [Pour Nom aléatoire].</li>" +
                            "<li>La modification du nombre de sous-repertoire parcouru: " +
                                "<ul>" +
                                    "<li>1 = dossier choisi</li>" +
                                    "<li>2 = 1 + premier sous-répertoire</li>" +
                                    "<li>3 = 2 + second sous-repertoire pour chaque premiers sous-répertoires.</li>" +
                                    "<li>...</li>" +
                        "</ul>" +
                    "</body>" +
                "</html>";

        this.label.setText(html);
    }
}
