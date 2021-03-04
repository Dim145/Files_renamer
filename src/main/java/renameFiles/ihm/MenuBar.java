package renameFiles.ihm;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JMenuBar
{
    private final IHMGUI ihm;
    private final JMenuItem aide;

    private final JCheckBoxMenuItem itemBlockIfNotMatchNumber;
    private final JCheckBoxMenuItem darkTheme;
    private final JCheckBoxMenuItem replacePByS;

    private Color currentColor;

    public MenuBar( IHMGUI ihm )
    {
        this.ihm        = ihm;

        JMenu optionMenu = new JMenu("option");
        JMenu aideMenu   = new JMenu("aide");
        //Todo modifier la page d'aide

        this.itemBlockIfNotMatchNumber = new JCheckBoxMenuItem("Block if not match");
        this.darkTheme                 = new JCheckBoxMenuItem("Dark thème");
        this.replacePByS               = new JCheckBoxMenuItem("Replace \".\" by \" \"");

        this.aide         = new JMenuItem("aide");
        JMenuItem aPropos = new JMenuItem("a propos");

        optionMenu.add(itemBlockIfNotMatchNumber);
        optionMenu.add(darkTheme);
        optionMenu.add(replacePByS);

        aideMenu.add(aide);
        aideMenu.add(aPropos);

        this.itemBlockIfNotMatchNumber.addActionListener(e -> changeBlockParam(this.itemBlockIfNotMatchNumber.isSelected()));
        this.replacePByS              .addActionListener(e -> this.changeReplacePbyS(this.replacePByS.isSelected()));

        aPropos  .addActionListener(e -> new APropos(this.currentColor));
        darkTheme.addActionListener(e -> this.changeTheme(darkTheme.isSelected()));
        this.aide.addActionListener(e -> new Aide(this.currentColor, this.getFont()));

        this.add(optionMenu);
        this.add(aideMenu);

        this.setOpaque(true);

        this.currentColor = Color.WHITE;
    }

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

    public void changeReplacePbyS( boolean replacePbyS )
    {
        this.ihm.changeReplacePbyS(replacePbyS);

        if( this.replacePByS.isSelected() != replacePbyS )
            this.replacePByS.setSelected(replacePbyS);

        this.reWritePrefParam();
    }

    private void reWritePrefParam()
    {
        this.ihm.saveBooleanPreferences("ignoreRenameProtection", !this.itemBlockIfNotMatchNumber.isSelected(), true);
        this.ihm.saveBooleanPreferences("darkMode", this.darkTheme.isSelected(), false);
        this.ihm.saveBooleanPreferences("replace \".\" by \" \"", this.replacePByS.isSelected(), false);
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
