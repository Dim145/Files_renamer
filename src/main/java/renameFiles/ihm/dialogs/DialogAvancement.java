package renameFiles.ihm.dialogs;

import renameFiles.ihm.IHMGUI;
import renameFiles.metier.resources.ResourceManager;
import renameFiles.metier.resources.Resources;

import javax.swing.*;
import java.awt.*;

/**
 * The type Dialog avancement.
 */
public class DialogAvancement extends JDialog
{
    private final int borneMin;

    private final JProgressBar bar;
    private final JLabel       affichageAvancement;
    private final JLabel       affichageFichierCourant;

    private int borneMax;

    /**
     * Instantiates a new Dialog avancement.
     *
     * @param borneMin the borne min
     * @param borneMax the borne max
     */
    public DialogAvancement( int borneMin, int borneMax )
    {
        this.borneMin = Math.min(borneMin, borneMax);
        this.borneMax = borneMin == borneMax ? borneMax +1 : Math.max(borneMin, borneMax);

        this.bar                     = new JProgressBar(JProgressBar.HORIZONTAL, borneMin, borneMax);
        this.affichageAvancement     = new JLabel(" 0.0%");
        this.affichageFichierCourant = new JLabel("");

        this.setPreferredSize(new Dimension(500, 75));

        this.add(this.bar                    , BorderLayout.CENTER);
        this.add(this.affichageAvancement    , BorderLayout.EAST  );
        this.add(this.affichageFichierCourant, BorderLayout.SOUTH );

        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setSize(500, 100);
        this.setLocationRelativeTo(null);

        Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int)dimension.getHeight();

        Font newFontSize = this.affichageAvancement.getFont().deriveFont(this.affichageAvancement.getFont().getSize() + (1f * height / IHMGUI.PAS_FONT_SIZE));

        this.affichageAvancement    .setFont(newFontSize);
        this.affichageFichierCourant.setFont(newFontSize);
    }

    /**
     * Sets borne max.
     *
     * @param newBorneMax the new borne max
     */
    public void setBorneMax( int newBorneMax )
    {
        if( this.borneMin > newBorneMax ) return;

        this.borneMax = this.borneMin == newBorneMax ? newBorneMax +1 : newBorneMax;
    }

    /**
     * Instantiates a new Dialog avancement.
     *
     * @param title the title
     */
    public DialogAvancement(String title)
    {
        this(title,0, 100);
    }

    /**
     * Instantiates a new Dialog avancement.
     *
     * @param title    the title
     * @param borneMin the borne min
     * @param borneMax the borne max
     */
    public DialogAvancement( String title, int borneMin, int borneMax )
    {
        this(borneMin, borneMax);

        this.setTitle(title);
    }

    /**
     * Reset.
     */
    public void reset()
    {
        this.bar.setValue(this.borneMin);
        this.affichageFichierCourant.setText("");
        this.affichageAvancement.setText(" 0.0%");
    }

    /**
     * Sets avancement.
     *
     * @param avancement the avancement
     */
    public void setAvancement( int avancement )
    {
        if( avancement > this.borneMax ) avancement = this.borneMax;
        if( avancement < this.borneMin ) avancement = this.borneMin;

        this.bar.setValue(avancement);
        this.affichageAvancement.setText( " " + String.format("%02.2f", (this.bar.getValue()/(this.borneMax*1.0)) * 100) + "%" );
    }

    /**
     * Avancer une fois.
     */
    public void avancerUneFois()
    {
        this.setAvancement(this.bar.getValue()+1);
    }

    /**
     * Sets fichier courant.
     *
     * @param nomFichier the nom fichier
     */
    public void setFichierCourant( String nomFichier )
    {
        if( !this.affichageFichierCourant.isVisible() )
            this.setVisible(true);

        this.affichageFichierCourant.setText(ResourceManager.getInstance().getString(Resources.TREATMENT) + " " + nomFichier);
    }
}
