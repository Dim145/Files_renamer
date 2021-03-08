package renameFiles.ihm;

import renameFiles.Controleur;
import renameFiles.ihm.composants.JConsoleLabel;
import renameFiles.ihm.composants.JIntergerTextField;
import renameFiles.ihm.composants.JTextFieldHideText;
import renameFiles.ihm.dialogs.APropos;
import renameFiles.metier.FileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The type Ihmgui.
 */
public class IHMGUI extends JFrame
{
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

        this.setTitle("Renamer");
        this.setIconImage(new ImageIcon( APropos.class.getResource("/images/Files_renamer.png")).getImage());

        this.pathField           = new JFormattedTextField();
        this.allTypes            = new JComboBox<>(FileType.values());
        this.paternField         = new JTextFieldHideText(this);
        this.extensions          = new JFormattedTextField();
        this.saveNbIfExist       = new JCheckBox("Save nb if exist");
        this.replacePbyS         = new JCheckBox("Replace \".\" by \" \"");
        this.launchRenamedScript = new JButton("GO");
        this.console             = new JConsoleLabel();
        this.picker              = new Picker();
        this.bar                 = new MenuBar(this);

        this.levelRecherche = new JIntergerTextField(String.valueOf(this.ctrl.getLevelMax()+1), e -> this.setNbSDL(-1));

        this.launchRenamedScript.addActionListener(e ->
        {
            if( !this.pathField.getText().isEmpty() && !" ".equals(this.pathField.getText()) )
            {
                if( !String.valueOf(this.ctrl.getLevelMax()+1).equals(this.levelRecherche.getText()) )
                    this.levelRecherche.setText(String.valueOf(this.ctrl.getLevelMax()+1));

                this.ctrl.setExtensions(this.extensions.getText());

                this.ctrl.renameFile(this.pathField.getText(), this.paternField.getForeground().equals(Color.GRAY) ? "" : this.paternField.getText(), this.replacePbyS.isSelected());
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
            IHMGUI.this.ctrl.setTypeCourant((FileType) event.getItem());

            FileType current = this.getCurrentType();

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
                        this.paternField.setText("Non obligatoire");
                    }
                }
                break;

                case ALEANAME:
                {
                    this.paternField.setForeground(Color.GRAY);
                    this.paternField.setText("Rien a saisir");
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

        this.allJPanel.get(1).add(new JLabel("path: "));
        this.allJPanel.get(1).add(new JLabel("type: "));
        this.allJPanel.get(1).add(new JLabel("extensions: "));
        this.allJPanel.get(1).add(new JLabel("name patern: "));

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

        JLabel label = new JLabel(" Nb S-D: ");
        this.allJPanel.get(7).add(label              , BorderLayout.CENTER);
        this.allJPanel.get(7).add(this.levelRecherche, BorderLayout.EAST );

        label.setToolTipText("Nombres de Sous-Dossiers à lire.");
        this.levelRecherche.setToolTipText(label.getToolTipText());

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

        this.bar.reWritePrefParam();
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

    /**
     * Save preferences.
     *
     * @param prefs     the prefs
     * @param clearFile the clear file
     */
    public void savePreferences(HashMap<String, Object> prefs, boolean clearFile )
    {
        this.ctrl.savePreferences(prefs, clearFile);
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
}
