package renameFiles.ihm;

import renameFiles.ihm.dialogs.APropos;
import renameFiles.ihm.dialogs.Aide;
import renameFiles.metier.Metier;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * The type Menu bar.
 */
public class MenuBar extends JMenuBar
{
    private final IHMGUI ihm;
    private final JMenuItem aide;

    private final JCheckBoxMenuItem itemBlockIfNotMatchNumber;
    private final JCheckBoxMenuItem darkTheme;

    private Color currentColor;

    /**
     * Instantiates a new Menu bar.
     *
     * @param ihm the ihm
     */
    public MenuBar( IHMGUI ihm )
    {
        this.ihm        = ihm;

        JMenu optionMenu = new JMenu("option");
        JMenu aideMenu   = new JMenu("aide");
        //Todo modifier la page d'aide

        this.itemBlockIfNotMatchNumber = new JCheckBoxMenuItem("Block if not match");
        this.darkTheme                 = new JCheckBoxMenuItem("Dark thème");

        this.aide         = new JMenuItem("aide");
        JMenuItem aPropos = new JMenuItem("a propos");

        optionMenu.add(itemBlockIfNotMatchNumber);
        optionMenu.add(darkTheme);

        aideMenu.add(aide);
        aideMenu.add(aPropos);

        this.itemBlockIfNotMatchNumber.addActionListener(e -> changeBlockParam(this.itemBlockIfNotMatchNumber.isSelected()));

        aPropos  .addActionListener(e -> new APropos(this.currentColor));
        darkTheme.addActionListener(e -> this.changeTheme(darkTheme.isSelected()));
        this.aide.addActionListener(e -> new Aide(this.currentColor, this.getFont()));

        this.add(optionMenu);
        this.add(aideMenu);

        this.setOpaque(true);

        this.currentColor = Color.WHITE;
    }

    /**
     * Change theme.
     *
     * @param bDarkTheme the b dark theme
     */
    public void changeTheme( boolean bDarkTheme )
    {
        Color baseColor = bDarkTheme ? new Color(50, 50, 50) : Color.WHITE;

        this.currentColor = baseColor;

        this.ihm.setColorForIHMAndChildren(baseColor);
        this.setBackground(baseColor);

        for (int i = 0; i < this.getComponentCount(); i++)
            this.setRecursiveColor(baseColor, this.getComponent(i));

        if( bDarkTheme != this.darkTheme.isSelected() )
            this.darkTheme.setSelected(bDarkTheme);

        this.reWritePrefParam();
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

    /**
     * Change block param.
     *
     * @param blockIfNotMatch the block if not match
     */
    public void changeBlockParam( boolean blockIfNotMatch )
    {
        System.out.println("Param block changed");

        if( this.itemBlockIfNotMatchNumber.isSelected() )
        {
            this.itemBlockIfNotMatchNumber.setBackground(Color.RED);
            this.ihm.setBlockIfNotMathPatern(false);
        }
        else
        {
            this.itemBlockIfNotMatchNumber.setBackground(Color.GREEN);
            this.ihm.setBlockIfNotMathPatern(true);
        }

        if( this.itemBlockIfNotMatchNumber.isSelected() != blockIfNotMatch )
            this.itemBlockIfNotMatchNumber.setSelected(blockIfNotMatch);

        this.reWritePrefParam();
    }

    /**
     * Re write pref param.
     */
    public void reWritePrefParam()
    {
        HashMap<String, Object> prefs = new HashMap<>();

        prefs.put(Metier.tabPreferences[0], this.itemBlockIfNotMatchNumber.isSelected());
        prefs.put(Metier.tabPreferences[1], this.darkTheme.isSelected());

        this.ihm.savePreferences(prefs, true);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(this.currentColor);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

}
