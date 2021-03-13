package renameFiles.ihm;

import renameFiles.ihm.dialogs.APropos;
import renameFiles.ihm.dialogs.Aide;
import renameFiles.metier.Metier;
import renameFiles.metier.resources.Languages;
import renameFiles.metier.resources.ResourceManager;
import renameFiles.metier.resources.Resources;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Locale;

/**
 * The type Menu bar.
 */
public class MenuBar extends JMenuBar implements Languages
{
    private static final ResourceManager MANAGER = ResourceManager.getInstance();

    private final IHMGUI ihm;

    private final JMenuItem aide;
    private final JMenuItem aPropos;

    private final JCheckBoxMenuItem itemBlockIfNotMatchNumber;
    private final JCheckBoxMenuItem darkTheme;

    private final JCheckBoxMenuItem french;
    private final JCheckBoxMenuItem english;
    private final JCheckBoxMenuItem japanese;

    private final JMenu optionMenu;
    private final JMenu aideMenu;
    private final JMenu languageMenu;

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

        this.itemBlockIfNotMatchNumber = new JCheckBoxMenuItem(MANAGER.getString(Resources.BLOCK_NOT_MATCH));
        this.darkTheme                 = new JCheckBoxMenuItem(MANAGER.getString(Resources.DARK_THEME));
        
        this.french                    = new JCheckBoxMenuItem(MANAGER.getString(Resources.FRENCH));
        this.english                   = new JCheckBoxMenuItem(MANAGER.getString(Resources.ENGLISH));
        this.japanese                  = new JCheckBoxMenuItem(MANAGER.getString(Resources.JAPANESE));

        this.aide    = new JMenuItem(MANAGER.getString(Resources.HELP));
        this.aPropos = new JMenuItem(MANAGER.getString(Resources.ABOUT));

        optionMenu.add(itemBlockIfNotMatchNumber);
        optionMenu.add(darkTheme);

        aideMenu.add(aide);
        aideMenu.add(aPropos);

        languageMenu.add(this.french);
        languageMenu.add(this.english);
        languageMenu.add(this.japanese);

        ButtonGroup group = new ButtonGroup();
        group.add(this.french);
        group.add(this.english);
        group.add(this.japanese);

        this.itemBlockIfNotMatchNumber.addActionListener(e -> changeBlockParam(this.itemBlockIfNotMatchNumber.isSelected()));

        aPropos  .addActionListener(e -> new APropos(this.currentColor));
        darkTheme.addActionListener(e -> this.changeTheme(darkTheme.isSelected()));
        this.aide.addActionListener(e -> new Aide(this.currentColor, this.getFont()));

        this.french  .addActionListener(e -> this.setLanguage(Locale.FRENCH));
        this.english .addActionListener(e -> this.setLanguage(Locale.ENGLISH));
        this.japanese.addActionListener(e -> this.setLanguage(Locale.JAPANESE));

        this.add(optionMenu);
        this.add(aideMenu);
        this.add(languageMenu);

        this.setOpaque(true);

        this.currentColor = Color.WHITE;

        if( ResourceManager.getInstance().getLocale().getLanguage().equals(Locale.FRENCH.getLanguage())      ) this.french .setSelected(true);
        else if( ResourceManager.getInstance().getLocale().getLanguage().equals(Locale.ENGLISH.getLanguage()) ) this.english.setSelected(true);

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

        this.reWritePrefParam();
    }

    private void setLanguage( Locale locale )
    {
        ResourceManager.getInstance().setLocale(locale);

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

    @Override
    public void setNewText()
    {
        this.optionMenu  .setText(MANAGER.getString(Resources.MENU_OPTION));
        this.aideMenu    .setText(MANAGER.getString(Resources.HELP));
        this.languageMenu.setText(MANAGER.getString(Resources.LANGUAGES));

        this.itemBlockIfNotMatchNumber.setText(MANAGER.getString(Resources.BLOCK_NOT_MATCH));
        this.darkTheme.setText(MANAGER.getString(Resources.DARK_THEME));

        this.french   .setText(MANAGER.getString(Resources.FRENCH));
        this.english  .setText(MANAGER.getString(Resources.ENGLISH));
        this.japanese .setText(MANAGER.getString(Resources.JAPANESE));

        this.aide   .setText(MANAGER.getString(Resources.HELP));
        this.aPropos.setText(MANAGER.getString(Resources.ABOUT));
    }
}
