package renameFiles.ihm;

import renameFiles.Controleur;
import renameFiles.ihm.composants.JComboBoxRenderer;
import renameFiles.ihm.composants.JConsoleLabel;
import renameFiles.ihm.composants.JIntergerTextField;
import renameFiles.ihm.composants.JTextFieldHideText;
import renameFiles.ihm.dialogs.APropos;
import renameFiles.metier.enums.FileType;
import renameFiles.metier.properties.PropertiesManager;
import renameFiles.metier.resources.ResourceManager;
import renameFiles.metier.resources.Resources;
import renameFiles.metier.resources.Traduisible;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The type Ihmgui.
 */
public class IHMGUI extends JFrame implements Traduisible
{
    private static final ResourceManager MANAGER = ResourceManager.getInstance();

    /**
     * The constant ECART_COLOR.
     */
    public static final int ECART_COLOR   = 25;
    /**
     * The constant PAS_FONT_SIZE.
     */
    public static final int PAS_FONT_SIZE = 400;

    private final Controleur ctrl;

    private final JTextField          pathField;
    private final JTextField          paternField;
    private final JTextField          extensions;
    private final JTextField          levelRecherche;

    private final JComboBox<FileType> allTypes;

    private final JCheckBox saveNbIfExist;
    private final JCheckBox replacePbyS;

    private final JButton launchRenamedScript;
    private final Picker  picker;
    private final MenuBar bar;

    private final JConsoleLabel console;

    private final ArrayList<JPanel> allJPanel;

    private final JLabel labelPath;
    private final JLabel labelType;
    private final JLabel labelExte;
    private final JLabel labelPate;
    private final JLabel labelNBSD;

    /**
     * Instantiates a new Ihmgui.
     *
     * @param ctrl the ctrl
     */
    public IHMGUI(Controleur ctrl)
    {
        super();

        this.ctrl      = ctrl;
        this.allJPanel = new ArrayList<>();

        this.setTitle(MANAGER.getString(Resources.TITLE));
        this.setIconImage(new ImageIcon( APropos.class.getResource("/images/Files_renamer.png")).getImage());

        this.pathField           = new JFormattedTextField();
        this.allTypes            = new JComboBox<>(FileType.values());
        this.paternField         = new JTextFieldHideText(this);
        this.extensions          = new JFormattedTextField();
        this.saveNbIfExist       = new JCheckBox(MANAGER.getString(Resources.CB_SAVENBIFEXIST));
        this.replacePbyS         = new JCheckBox(MANAGER.getString(Resources.CB_REPLACEPBYS));
        this.launchRenamedScript = new JButton(MANAGER.getString(Resources.BTN_LAUNCH));
        this.console             = new JConsoleLabel();
        this.picker              = new Picker();
        this.bar                 = new MenuBar(this);

        this.allTypes.setRenderer(new JComboBoxRenderer(this.allTypes.getRenderer()));

        this.levelRecherche = new JIntergerTextField(String.valueOf(this.ctrl.getLevelMax()+1), e -> this.setNbSDL(-1));

        this.launchRenamedScript.addActionListener(e ->
        {
            if( !this.pathField.getText().isEmpty() && !" ".equals(this.pathField.getText()) )
            {
                if( !String.valueOf(this.ctrl.getLevelMax()+1).equals(this.levelRecherche.getText()) )
                    this.levelRecherche.setText(String.valueOf(this.ctrl.getLevelMax()+1));

                this.ctrl.setExtensions(this.extensions.getText());

                this.ctrl.renameFile(this.pathField.getText(), this.paternField.getForeground().equals(Color.GRAY) ? "" : this.paternField.getText(),
                        this.replacePbyS.isSelected(), this.bar.isQualiterTextuel());
            }
            else
            {
                this.printInConsole("<font color=\"red\">Selectionnez un repertoire d'où partir</font>" );
            }

        });

        this.extensions.addActionListener(e -> this.paternField.grabFocus());
        this.paternField.addActionListener(e -> this.launchRenamedScript.doClick());

        this.pathField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                super.focusGained(e);
                IHMGUI.this.paternField.grabFocus();

                String s = IHMGUI.this.picker.pickADirectory();

                if( s != null ) IHMGUI.this.setCurrentPath( s );
            }
        });

        this.allTypes.addItemListener(event ->
        {
            FileType current = this.getCurrentType();

            IHMGUI.this.ctrl.setTypeCourant(current);

            this.replacePbyS  .setVisible ( current == FileType.SERIES  );
            this.saveNbIfExist.setVisible ( current == FileType.ALEANAME);
            this.extensions   .setEditable( current != FileType.SERIES  );
            this.paternField  .setEditable( current != FileType.ALEANAME);

            this.extensions.setText( current.getListExtensionInString() );

            switch( current )
            {
                case SERIES:
                {
                    if (IHMGUI.this.paternField.getForeground() == Color.GRAY || IHMGUI.this.paternField.getText().isEmpty() )
                    {
                        this.paternField.setForeground(Color.GRAY);
                        this.paternField.setText(MANAGER.getString(Resources.NO_REQUIRED));
                    }
                }
                break;

                case ALEANAME:
                {
                    this.paternField.setForeground(Color.GRAY);
                    this.paternField.setText(MANAGER.getString(Resources.NO_TO_WRITE));
                }
                break;

                default:
                    if( this.paternField.getForeground().equals(Color.GRAY))
                    {
                        this.paternField.setText("");

                        Color baseColor = IHMGUI.this.paternField.getBackground().equals(Color.WHITE) ? new Color(50, 50, 50) : Color.WHITE;
                        IHMGUI.this.paternField.setForeground(IHMGUI.couleurPlusClair(baseColor, baseColor.equals(Color.WHITE)));
                    }
            }
        });

        this.levelRecherche.setColumns(2);

        this.saveNbIfExist.addActionListener(event -> this.ctrl.setSaveNbIfExist(this.saveNbIfExist.isSelected()));

        this.allJPanel.add(new JPanel(new GridLayout(4, 1)) );
        this.allJPanel.add(new JPanel(new GridLayout(4, 1)));
        this.allJPanel.add(new JPanel(new BorderLayout()));
        this.allJPanel.add(new JPanel(new BorderLayout()));
        this.allJPanel.add(new JPanel(new BorderLayout()));
        this.allJPanel.add(new JPanel(new BorderLayout()));
        this.allJPanel.add(new JPanel(new BorderLayout()));
        this.allJPanel.add(new JPanel(new FlowLayout  ()));

        this.allJPanel.get(0).add(this.allJPanel.get(6));
        this.allJPanel.get(0).add(this.allJPanel.get(4));
        this.allJPanel.get(0).add(this.extensions);
        this.allJPanel.get(0).add(this.allJPanel.get(3));

        this.labelPath = new JLabel(MANAGER.getString(Resources.PATH));
        this.labelType = new JLabel(MANAGER.getString(Resources.TYPE_APP));
        this.labelExte = new JLabel(MANAGER.getString(Resources.EXTENSIONS));
        this.labelPate = new JLabel(MANAGER.getString(Resources.NAME_PATERN));

        this.allJPanel.get(1).add(this.labelPath);
        this.allJPanel.get(1).add(this.labelType);
        this.allJPanel.get(1).add(this.labelExte);
        this.allJPanel.get(1).add(this.labelPate);

        this.allJPanel.get(2).add(this.allJPanel.get(0), BorderLayout.CENTER);
        this.allJPanel.get(2).add(this.allJPanel.get(1), BorderLayout.WEST  );

        this.allJPanel.get(3).add(this.paternField        , BorderLayout.CENTER);
        this.allJPanel.get(3).add(this.launchRenamedScript, BorderLayout.EAST  );

        this.allJPanel.get(4).add(this.allTypes        , BorderLayout.CENTER);
        this.allJPanel.get(4).add(this.allJPanel.get(5), BorderLayout.EAST  );

        this.allJPanel.get(5).add(this.replacePbyS  , BorderLayout.CENTER);
        this.allJPanel.get(5).add(this.saveNbIfExist, BorderLayout.EAST  );

        this.allJPanel.get(6).add(this.pathField       , BorderLayout.CENTER);
        this.allJPanel.get(6).add(this.allJPanel.get(7), BorderLayout.EAST );

        this.labelNBSD = new JLabel(MANAGER.getString(Resources.NB_SD));
        this.allJPanel.get(7).add(labelNBSD              , BorderLayout.CENTER);
        this.allJPanel.get(7).add(this.levelRecherche, BorderLayout.EAST );

        labelNBSD.setToolTipText(MANAGER.getString(Resources.TOOLTIP_NB_SD));
        this.levelRecherche.setToolTipText(labelNBSD.getToolTipText());

        this.add( this.console         , BorderLayout.CENTER );
        this.add( this.allJPanel.get(2), BorderLayout.NORTH  );
        this.setJMenuBar(this.bar);

        Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int) dimension.getHeight();

        this.pack();

        // permet de mettre une taille dynamique. Le pas est de
        Font currentFont = this.getFont().deriveFont(this.getFont().getSize() + (1f * height / IHMGUI.PAS_FONT_SIZE));

        IHMGUI.majAllFonts(this, currentFont);

        this.setSize(this.getWidth() + 100*height/IHMGUI.PAS_FONT_SIZE, this.getHeight()+200);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.extensions.grabFocus();
        this.saveNbIfExist.setVisible(false);
        this.replacePbyS.setVisible(false);

        ResourceManager.getInstance().addObjectToTranslate(this);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                try
                {
                    PropertiesManager.getInstance().savePropertiesToXML();
                }
                catch (IOException ioException)
                {
                    ioException.printStackTrace();
                }

                super.windowClosing(e);
            }
        });
    }

    /**
     * Sets nb sdl.
     *
     * @param val the val
     * @throws NumberFormatException the number format exception
     */
    public void setNbSDL( int val ) throws NumberFormatException
    {
        if( val > 0 )
        {
            this.levelRecherche.setText(String.valueOf(val));
            return;
        }

        if( this.levelRecherche.getText().isEmpty())
            return;

        this.ctrl.setSDL(Integer.parseInt(this.levelRecherche.getText())-1);
    }

    private static void majAllFonts(Container comp, Font font)
    {
        comp.setFont(font);

        for (int i = 0; i < comp.getComponentCount(); i++)
            if( comp.getComponent(i) instanceof Container )
                majAllFonts((Container) comp.getComponent(i), font);
    }

    /**
     * Sets current path.
     *
     * @param path the path
     */
    public void setCurrentPath(String path)
    {
        if( path.endsWith("\\.") || path.endsWith("/.") )
            path = path.substring(0, path.length()-2);

        this.pathField.setText(path);
        this.picker.setCurrentDirectory(new File(path));
    }

    /**
     * Print in console.
     *
     * @param s the s
     */
    public void printInConsole(String s)
    {
        this.console.addText(s);

        this.changeConsoleColorByUIColor();
    }

    public void addTextToTampon(String text, boolean pushToConsole)
    {
        this.console.addTexteToTampon(text);

        if( pushToConsole ) this.console.addTamponToConsole();
    }

    public void setTypeCourant( FileType type )
    {
        this.allTypes.setSelectedItem(type);
    }

    /**
     * Sets block if not math patern.
     *
     * @param b the b
     */
    public void setBlockIfNotMathPatern(boolean b)
    {
        this.ctrl.changeBlockParam(b);
    }

    /**
     * Sets color for ihm and children.
     *
     * @param baseColor the base color
     */
    public void setColorForIHMAndChildren(Color baseColor)
    {
        Color foreground  = baseColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        Color colorEpurer = IHMGUI.couleurPlusClair(baseColor, baseColor == Color.WHITE);

        this.setBackground(baseColor);

        for (JPanel panel : this.allJPanel )
        {
            panel.setBackground(baseColor);

            for (int i = 0; i < panel.getComponentCount(); i++)
            {
                if( !(panel.getComponent(i) instanceof JButton) && !(panel.getComponent(i) instanceof JComboBox) )
                {
                    if( panel.getComponent(i).getForeground() != Color.GRAY ) // Color.GRAY = desactivé, donc on ne change pas la couleur
                        panel.getComponent(i).setForeground(foreground);

                    panel.getComponent(i).setBackground(colorEpurer);
                }
            }
        }

        this.console.setBackground(baseColor );
        this.console.setForeground(foreground);

        this.changeConsoleColorByUIColor();
    }

    private void changeConsoleColorByUIColor()
    {
        if( this.console.getBackground().equals(Color.WHITE) )
        {
            this.console.setText(this.console.getText().replaceAll("rgb\\(0, 255, 255\\)", "blue"));
            this.console.setText(this.console.getText().replaceAll("rgb\\(255, 60, 60\\)", "red"));
        }
        else
        {
            this.console.setText(this.console.getText().replaceAll("blue", "rgb(0, 255, 255)"));
            this.console.setText(this.console.getText().replaceAll("red" , "rgb(255, 60, 60)"));
        }
    }

    /**
     * Couleur plus clair color.
     *
     * @param baseColor  the base color
     * @param plusFoncer the plus foncer
     * @return the color
     */
    public static Color couleurPlusClair(Color baseColor, boolean plusFoncer)
    {
        if( plusFoncer )
            return new Color(baseColor.getRed() - ECART_COLOR, baseColor.getGreen() - ECART_COLOR, baseColor.getBlue() - ECART_COLOR);
        else
            return new Color(baseColor.getRed() + ECART_COLOR, baseColor.getGreen() + ECART_COLOR, baseColor.getBlue() + ECART_COLOR);
    }

    /**
     * Change theme.
     *
     * @param darkTheme the dark theme
     */
    public void changeTheme( boolean darkTheme)
    {
        this.bar.changeTheme(darkTheme);
    }

    /**
     * Change block param.
     *
     * @param blockIfNotMatch the block if not match
     */
    public void changeBlockParam( boolean blockIfNotMatch)
    {
        this.bar.changeBlockParam(blockIfNotMatch);
    }

    public void savePreferences(String key, String value)
    {
        this.ctrl.savePreferences(key, value);
    }

    /**
     * Gets current type.
     *
     * @return the current type
     */
    public FileType getCurrentType()
    {
        return (FileType) this.allTypes.getSelectedItem();
    }

    @Override
    public void setNewText()
    {
        this.setTitle(MANAGER.getString(Resources.TITLE));

        this.saveNbIfExist.setText(MANAGER.getString(Resources.CB_SAVENBIFEXIST));
        this.replacePbyS  .setText(MANAGER.getString(Resources.CB_REPLACEPBYS));
        this.launchRenamedScript.setText(MANAGER.getString(Resources.BTN_LAUNCH));

        if( this.paternField.getForeground() == Color.GRAY )
            this.paternField.setText(MANAGER.getString(this.getCurrentType() == FileType.ALEANAME ? Resources.NO_TO_WRITE : Resources.NO_REQUIRED));

        this.labelExte.setText(MANAGER.getString(Resources.EXTENSIONS));
        this.labelPate.setText(MANAGER.getString(Resources.NAME_PATERN));
        this.labelPath.setText(MANAGER.getString(Resources.PATH));
        this.labelType.setText(MANAGER.getString(Resources.TYPE_APP));
        this.labelNBSD.setText(MANAGER.getString(Resources.NB_SD));
        this.labelNBSD.setToolTipText(MANAGER.getString(Resources.TOOLTIP_NB_SD));
    }

    public void setQualiterTextuel(boolean b)
    {
        this.bar.setQualiterTextuel(b);
    }
}
