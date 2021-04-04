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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private final JCheckBoxMenuItem blockIfNotMathPatern;
    private final JCheckBoxMenuItem darkMode;
    private final JCheckBoxMenuItem qualiterTextuel;
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

        this.blockIfNotMathPatern = new JCheckBoxMenuItem(new ActionBlock());
        this.darkMode             = new JCheckBoxMenuItem(new ActionDarkTheme());
        this.qualiterTextuel      = new JCheckBoxMenuItem(new ActionQualiter());
        this.activeWeb            = new JCheckBoxMenuItem(new ActionActiveWeb());
        this.webTitle             = new JCheckBoxMenuItem(new ActionWebTitle());
        this.webName              = new JCheckBoxMenuItem(new ActionWebName());
        
        this.french                    = new JCheckBoxMenuItem(MANAGER.getString(Resources.FRENCH));
        this.english                   = new JCheckBoxMenuItem(MANAGER.getString(Resources.ENGLISH));
        this.japanese                  = new JCheckBoxMenuItem(MANAGER.getString(Resources.JAPANESE));
        this.spanish                   = new JCheckBoxMenuItem(MANAGER.getString(Resources.SPANISH));

        this.aide    = new JMenuItem(new ActionAide());
        this.aPropos = new JMenuItem(MANAGER.getString(Resources.ABOUT));

        optionMenu.add(blockIfNotMathPatern);
        optionMenu.add(darkMode);
        optionMenu.add(qualiterTextuel);
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
    public void setDarkMode(boolean bDarkTheme )
    {
        Color baseColor = bDarkTheme ? new Color(50, 50, 50) : Color.WHITE;

        this.currentColor = baseColor;

        this.ihm.setColorForIHMAndChildren(baseColor);
        this.setBackground(baseColor);

        for (int i = 0; i < this.getComponentCount(); i++)
            this.setRecursiveColor(baseColor, this.getComponent(i));

        if( bDarkTheme != this.darkMode.isSelected() )
            this.darkMode.setSelected(bDarkTheme);

        this.ihm.savePreferences(Metier.tabPreferences[1], String.valueOf(this.darkMode.isSelected()));
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
    public void setBlockIfNotMathPatern(boolean blockIfNotMatch )
    {
        if( this.blockIfNotMathPatern.isSelected() )
        {
            this.blockIfNotMathPatern.setBackground(Color.RED);
            this.ihm.setBlockIfNotMathPatern(false);
        }
        else
        {
            this.blockIfNotMathPatern.setBackground(Color.GREEN);
            this.ihm.setBlockIfNotMathPatern(true);
        }

        if( this.blockIfNotMathPatern.isSelected() != blockIfNotMatch )
            this.blockIfNotMathPatern.setSelected(blockIfNotMatch);

        this.ihm.savePreferences(Metier.tabPreferences[0], String.valueOf(this.blockIfNotMathPatern.isSelected()));
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

        this.blockIfNotMathPatern.setText(MANAGER.getString(Resources.BLOCK_NOT_MATCH));
        this.qualiterTextuel.setText(MANAGER.getString(Resources.QUALITERTEXTUEL));

        this.darkMode.setText(MANAGER.getString(Resources.DARK_THEME));
        this.webName  .setText(MANAGER.getString(Resources.STANDARD_NAME));
        this.webTitle .setText(MANAGER.getString(Resources.STANDARD_TITLE));
        this.activeWeb.setText(MANAGER.getString(Resources.ACTIVE_WEB));

        this.french   .setText(MANAGER.getString(Resources.FRENCH));
        this.english  .setText(MANAGER.getString(Resources.ENGLISH));
        this.japanese .setText(MANAGER.getString(Resources.JAPANESE));
        this.spanish  .setText(MANAGER.getString(Resources.SPANISH));

        this.aide   .setText(MANAGER.getString(Resources.HELP));
        this.aPropos.setText(MANAGER.getString(Resources.ABOUT));
    }

    public void setQualiterTextuel(boolean b)
    {
        this.qualiterTextuel.setSelected(b);

        this.ihm.savePreferences(Metier.tabPreferences[4], String.valueOf(this.qualiterTextuel.isSelected()));
    }

    public boolean isQualiterTextuel()
    {
        return this.qualiterTextuel.isSelected();
    }

    public void setActiveWeb(boolean b)
    {
        if( this.activeWeb.isSelected() != b )
            this.activeWeb.setSelected(b);

        webName .setEnabled(b);
        webTitle.setEnabled(b);

        this.ihm.setWebValues(0, b);
        this.ihm.savePreferences(Metier.tabPreferences[5], String.valueOf(b));
    }

    public void setWebTitle( boolean b )
    {
        if( this.webTitle.isSelected() != b )
            this.webTitle.setSelected(b);

        this.ihm.setWebValues(1, b);

        this.ihm.savePreferences(Metier.tabPreferences[6], String.valueOf(b));
    }

    public void setWebName( boolean b )
    {
        if( this.webName.isSelected() != b )
            this.webName.setSelected(b);

        this.ihm.setWebValues(2, b);

        this.ihm.savePreferences(Metier.tabPreferences[7], String.valueOf(b));
    }

    public void setIHMValueFirstTime(String name, boolean parseBoolean)
    {
        Class<MenuBar> thisClass = MenuBar.class;

        try
        {
            Method m = thisClass.getMethod("set" + name, boolean.class);

            m.invoke(this, parseBoolean);
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored)
        {

        }
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
            MenuBar.this.setDarkMode(MenuBar.this.darkMode.isSelected());
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
            MenuBar.this.setBlockIfNotMathPatern(MenuBar.this.blockIfNotMathPatern.isSelected());
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
            setQualiterTextuel(qualiterTextuel.isSelected());
        }
    }

    private class ActionActiveWeb extends AbstractAction
    {
        public ActionActiveWeb()
        {
            super(MANAGER.getString(Resources.ACTIVE_WEB));

            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        }


        @Override
        public void actionPerformed(ActionEvent e)
        {
            setActiveWeb(activeWeb.isSelected());

            web.doClick();
        }
    }

    private class ActionWebTitle extends AbstractAction
    {
        public ActionWebTitle()
        {
            super(MANAGER.getString(Resources.STANDARD_TITLE));

            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            setWebTitle(webTitle.isSelected());
        }
    }

    private class ActionWebName extends AbstractAction
    {
        public ActionWebName()
        {
            super(MANAGER.getString(Resources.STANDARD_NAME));

            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            setWebName(webName.isSelected());
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
