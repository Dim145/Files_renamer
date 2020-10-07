package renameFiles.ihm;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JMenuBar
{
    private final IHMGUI ihm;
    private final JMenuItem itemBlockIfNotMatchNumber;
    private final JMenuItem aide;

    private boolean darkTheme;
    private Color currentColor;

    public MenuBar( IHMGUI ihm )
    {
        this.ihm       = ihm;
        this.darkTheme = false;

        JMenu optionMenu = new JMenu("option");
        JMenu aideMenu   = new JMenu("aide");
        //Todo faire la page d'aide

        this.itemBlockIfNotMatchNumber = new JMenuItem("✓ Block if not match");

        this.aide      = new JMenuItem("aide");
        JMenuItem aPropos   = new JMenuItem("a propos");
        JMenuItem darkTheme = new JMenuItem("X dark thème");

        optionMenu.add(itemBlockIfNotMatchNumber);
        optionMenu.add(darkTheme);

        aideMenu.add(aide);
        aideMenu.add(aPropos);

        this.itemBlockIfNotMatchNumber.addActionListener(e -> changeBlockParam());

        aPropos.addActionListener(e -> new APropos(this.currentColor));
        darkTheme.addActionListener(e ->
        {
            this.changeTheme();

            char first = darkTheme.getText().charAt(0);

            darkTheme.setText( (first == '✓' ? 'X' : '✓') + darkTheme.getText().substring(1) );
        });

        this.add(optionMenu);
        this.add(aideMenu);

        this.setOpaque(true);

        this.currentColor = Color.WHITE;
    }

    private void changeTheme()
    {
        this.darkTheme = !this.darkTheme;

        Color baseColor = this.darkTheme ? new Color(50, 50, 50) : Color.WHITE;

        this.currentColor = baseColor;

        this.ihm.setColorForIHMAndChildren(baseColor);
        this.setBackground(baseColor);

        for (int i = 0; i < this.getComponentCount(); i++)
            this.setRecursiveColor(baseColor, this.getComponent(i));
    }

    private void setRecursiveColor( Color color, Component component)
    {
        component.setBackground(color);
        component.setForeground(this.darkTheme ? Color.WHITE : Color.BLACK);

        if( component instanceof Container)
        {
            for (int i = 0; i < ((Container) component).getComponentCount(); i++)
                setRecursiveColor(color, ((Container) component).getComponent(i));
        }
    }

    private void changeBlockParam()
    {
        char first = this.itemBlockIfNotMatchNumber.getText().charAt(0);

        if( '✓' == first )
        {
            this.itemBlockIfNotMatchNumber.setBackground(Color.RED);
            this.itemBlockIfNotMatchNumber.setText('X' + this.itemBlockIfNotMatchNumber.getText().substring(1));
            this.ihm.setBlockIfNotMathPatern(false);
        }
        else if( 'X' == first )
        {
            this.itemBlockIfNotMatchNumber.setBackground(Color.GREEN);
            this.itemBlockIfNotMatchNumber.setText('✓' + this.itemBlockIfNotMatchNumber.getText().substring(1));
            this.ihm.setBlockIfNotMathPatern(true);
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(this.currentColor);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    public Color getCurrentColor()
    {
        return this.currentColor;
    }
}
