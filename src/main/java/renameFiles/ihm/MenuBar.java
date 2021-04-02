package renameFiles.ihm;

import renameFiles.ihm.dialogs.APropos;
import renameFiles.ihm.dialogs.Aide;
import renameFiles.metier.Metier;
import renameFiles.metier.resources.ResourceManager;
import renameFiles.metier.resources.Resources;
import renameFiles.metier.resources.Traduisible;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;

/**
 * The type Menu bar.
 */
public class MenuBar extends JMenuBar implements Traduisible
{
    private static final ResourceManager MANAGER = ResourceManager.getInstance();
    private static final Locale          SPANISH = new Locale("spa", "");

    private final IHMGUI ihm;

    private final JMenuItem aide;
    private final JMenuItem aPropos;

    private final JCheckBoxMenuItem itemBlockIfNotMatchNumber;
    private final JCheckBoxMenuItem darkTheme;
    private final JCheckBoxMenuItem qualiterTextuelle;
    private final JCheckBoxMenuItem activeWeb;
    private final JCheckBoxMenuItem webTitle;
    private final JCheckBoxMenuItem webName;

    private final JCheckBoxMenuItem french;
    private final JCheckBoxMenuItem english;
    private final JCheckBoxMenuItem japanese;
    private final JCheckBoxMenuItem spanish;

    private final JMenu optionMenu;
    private final JMenu aideMenu;
    private final JMenu languageMenu;
    private final JMenu web;

    private Color currentColor;

    /**
     * Instantiates a new Menu bar.
     *
     * @param ihm the ihm
     */
    public MenuBar( IHMGUI ihm )
    {
        this.ihm        = ihm;

        this.optionMenu   = new JMenu(MANAGER.getString(Resources.MENU_OPTION));
        this.aideMenu     = new JMenu(MANAGER.getString(Resources.HELP));
        this.languageMenu = new JMenu(MANAGER.getString(Resources.LANGUAGES));
        this.web          = new JMenu(new ActionWEB());

        this.itemBlockIfNotMatchNumber = new JCheckBoxMenuItem(new ActionBlock());
        this.darkTheme                 = new JCheckBoxMenuItem(new ActionDarkTheme());
        this.qualiterTextuelle         = new JCheckBoxMenuItem(new ActionQualiter());
        this.activeWeb                 = new JCheckBoxMenuItem(new ActionActiveWeb());
        this.webTitle                  = new JCheckBoxMenuItem("web Title");
        this.webName                   = new JCheckBoxMenuItem("web name");
        
        this.french                    = new JCheckBoxMenuItem(MANAGER.getString(Resources.FRENCH));
        this.english                   = new JCheckBoxMenuItem(MANAGER.getString(Resources.ENGLISH));
        this.japanese                  = new JCheckBoxMenuItem(MANAGER.getString(Resources.JAPANESE));
        this.spanish                   = new JCheckBoxMenuItem(MANAGER.getString(Resources.SPANISH));

        this.aide    = new JMenuItem(new ActionAide());
        this.aPropos = new JMenuItem(MANAGER.getString(Resources.ABOUT));

        optionMenu.add(itemBlockIfNotMatchNumber);
        optionMenu.add(darkTheme);
        optionMenu.add(qualiterTextuelle);
        optionMenu.add(web);

        aideMenu.add(aide);
        aideMenu.add(aPropos);

        languageMenu.add(this.french);
        languageMenu.add(this.english);
        languageMenu.add(this.japanese);
        languageMenu.add(this.spanish);

        web.add(activeWeb);
        web.addSeparator();
        web.add(webName);
        web.add(webTitle);

        ButtonGroup group = new ButtonGroup();
        group.add(this.french);
        group.add(this.english);
        group.add(this.japanese);
        group.add(this.spanish);

        aPropos  .addActionListener(e -> new APropos(this.currentColor));

        this.french  .addActionListener(e -> this.setLanguage(Locale.FRENCH));
        this.english .addActionListener(e -> this.setLanguage(Locale.ENGLISH));
        this.japanese.addActionListener(e -> this.setLanguage(Locale.JAPANESE));
        this.spanish .addActionListener(e -> this.setLanguage(MenuBar.SPANISH));

        this.add(optionMenu);
        this.add(aideMenu);
        this.add(languageMenu);

        this.setOpaque(true);

        this.currentColor = Color.WHITE;

        switch (ResourceManager.getInstance().getLocale().getLanguage().toLowerCase())
        {
            case "fr" : this.french .setSelected(true); break;
            case "en" : this.english.setSelected(true); break;
            case "spa": this.spanish.setSelected(true); break;
            case "ja" :
            case "jpn": this.japanese.setSelected(true); break;
        }

        ResourceManager.getInstance().addObjectToTranslate(this);
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

        this.ihm.savePreferences(Metier.tabPreferences[1], String.valueOf(this.darkTheme.isSelected()));
    }

    private void setLanguage( Locale locale )
    {
        ResourceManager.getInstance().setLocale(locale);

        this.ihm.savePreferences(Metier.tabPreferences[3], locale.getLanguage());
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

        this.ihm.savePreferences(Metier.tabPreferences[0], String.valueOf(this.itemBlockIfNotMatchNumber.isSelected()));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(this.currentColor);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    @Override
    public void setNewText()
    {
        this.optionMenu  .setText(MANAGER.getString(Resources.MENU_OPTION));
        this.aideMenu    .setText(MANAGER.getString(Resources.HELP));
        this.languageMenu.setText(MANAGER.getString(Resources.LANGUAGES));

        this.itemBlockIfNotMatchNumber.setText(MANAGER.getString(Resources.BLOCK_NOT_MATCH));
        this.darkTheme.setText(MANAGER.getString(Resources.DARK_THEME));
        this.qualiterTextuelle.setText(MANAGER.getString(Resources.QUALITERTEXTUEL));

        this.french   .setText(MANAGER.getString(Resources.FRENCH));
        this.english  .setText(MANAGER.getString(Resources.ENGLISH));
        this.japanese .setText(MANAGER.getString(Resources.JAPANESE));
        this.spanish  .setText(MANAGER.getString(Resources.SPANISH));

        this.aide   .setText(MANAGER.getString(Resources.HELP));
        this.aPropos.setText(MANAGER.getString(Resources.ABOUT));
    }

    public void setQualiterTextuel(boolean b)
    {
        this.qualiterTextuelle.setSelected(b);

        this.ihm.savePreferences(Metier.tabPreferences[4], String.valueOf(this.qualiterTextuelle.isSelected()));
    }

    public boolean isQualiterTextuel()
    {
        return this.qualiterTextuelle.isSelected();
    }

    private class ActionDarkTheme extends AbstractAction
    {
        public ActionDarkTheme()
        {
            super(MANAGER.getString(Resources.DARK_THEME));

            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
            this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            MenuBar.this.changeTheme(MenuBar.this.darkTheme.isSelected());
        }
    }

    private class ActionBlock extends AbstractAction
    {
        public ActionBlock()
        {
            super(MANAGER.getString(Resources.BLOCK_NOT_MATCH));

            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_B);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            MenuBar.this.changeBlockParam(MenuBar.this.itemBlockIfNotMatchNumber.isSelected());
        }
    }

    private class ActionAide extends AbstractAction
    {
        public ActionAide()
        {
            super(MANAGER.getString(Resources.HELP));

            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_H);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            new Aide(MenuBar.this.currentColor, MenuBar.this.getFont());
        }
    }

    private class ActionQualiter extends AbstractAction
    {
        public ActionQualiter()
        {
            super(MANAGER.getString(Resources.QUALITERTEXTUEL));

            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            setQualiterTextuel(qualiterTextuelle.isSelected());
        }
    }

    private class ActionActiveWeb extends AbstractAction
    {
        public ActionActiveWeb()
        {
            super("Activer récupéartion WEB");

            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        }


        @Override
        public void actionPerformed(ActionEvent e)
        {
            webName .setEnabled(activeWeb.isSelected());
            webTitle.setEnabled(activeWeb.isSelected());

            web.doClick();
        }
    }

    private static class ActionWEB extends AbstractAction
    {
        public ActionWEB()
        {
            super("WEB");

            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);
        }


        @Override
        public void actionPerformed(ActionEvent e)
        {

        }
    }
}
