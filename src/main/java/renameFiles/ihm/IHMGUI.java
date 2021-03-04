package renameFiles.ihm;

import renameFiles.Controleur;
import renameFiles.metier.FileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class IHMGUI extends JFrame
{
    public static final int ECART_COLOR   = 25;
    public static final int PAS_FONT_SIZE = 200;

    private final Controleur ctrl;

    private final JTextField          pathField;
    private final JComboBox<FileType> allTypes;
    private final JTextField          paternField;
    private final JTextField          extensions;

    private final JButton launchRenamedScript;

    private final JLabel  console;

    private final Picker picker;

    private final ArrayList<JPanel> allJPanel;

    private boolean isReplacePbyS;

    public IHMGUI(Controleur ctrl)
    {
        super();

        this.ctrl      = ctrl;
        this.allJPanel = new ArrayList<>();

        this.setTitle("Renamer");
        this.setIconImage(new ImageIcon( APropos.class.getResource("/images/Files_renamer.png")).getImage());

        this.pathField           = new JFormattedTextField();
        this.allTypes            = new JComboBox<>(FileType.values());
        this.paternField         = new JFormattedTextField();
        this.extensions          = new JFormattedTextField();
        this.launchRenamedScript = new JButton("GO");
        this.console             = new JLabel();
        this.picker              = new Picker();

        this.isReplacePbyS = false;

        this.setAutoRequestFocus(true);

        launchRenamedScript.addActionListener(e ->
        {
            if( !this.pathField.getText().isEmpty() && !" ".equals(this.pathField.getText()) )
            {
                this.ctrl.setExtensions(this.extensions.getText());

                this.ctrl.renameFile(this.pathField.getText(), this.paternField.getForeground().equals(Color.GRAY) ? "" : this.paternField.getText(), this.isReplacePbyS);
            }
            else
            {
                this.printInConsole("<font color=\"red\">Selectionnez un repertoire d'où partir</font>" );            }

        });

        this.extensions.addActionListener(e -> this.paternField.grabFocus());

        this.extensions.setText("mp4,mkv");
        this.launchRenamedScript.setOpaque(true);
        this.console.setText("<html>");
        this.console.setOpaque(true);
        this.console.setBackground(Color.WHITE);
        this.console.setVerticalAlignment(JLabel.BOTTOM);
        this.console.setVerticalTextPosition(JLabel.BOTTOM);

        this.pathField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e)
            {
                super.focusGained(e);
                IHMGUI.this.paternField.grabFocus();

                String s = IHMGUI.this.picker.pickADirectory();

                if( s != null )
                    IHMGUI.this.setCurrentPath( s );
            }
        });

        this.paternField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e)
            {
                super.focusGained(e);

                if( IHMGUI.this.paternField.getForeground().equals(Color.GRAY) )
                {
                    IHMGUI.this.paternField.setText("");
                    Color baseColor = IHMGUI.this.paternField.getBackground().equals(Color.WHITE) ? new Color(50, 50, 50) : Color.WHITE;
                    IHMGUI.this.paternField.setForeground(IHMGUI.couleurPlusClair(baseColor, baseColor.equals(Color.WHITE)));
                }
            }
        });

        this.allTypes.addItemListener(event ->
        {
            IHMGUI.this.ctrl.setTypeCourant((FileType) event.getItem());

            if( event.getItem() != FileType.AUTRES )
            {
                this.extensions.setEditable(false);
                this.extensions.setText(((FileType) event.getItem()).getListExtensionInString());

                if( IHMGUI.this.paternField.getText().isEmpty() )
                {
                    this.paternField.setForeground(Color.GRAY);
                    this.paternField.setText("Non obligatoire");
                }
            }
            else
            {
                this.extensions .setEditable(true);
                this.extensions.setText("");

                if( this.paternField.getForeground().equals(Color.GRAY))
                {
                    this.paternField.setText("");

                    Color baseColor = IHMGUI.this.paternField.getBackground().equals(Color.WHITE) ? new Color(50, 50, 50) : Color.WHITE;
                    IHMGUI.this.paternField.setForeground(IHMGUI.couleurPlusClair(baseColor, baseColor.equals(Color.WHITE)));
                }
            }
        });

        JPanel tmp  = new JPanel();
        JPanel tmp2 = new JPanel();
        JPanel tmp3 = new JPanel();
        JPanel tmp4 = new JPanel();

        this.allJPanel.add(tmp );
        this.allJPanel.add(tmp2);
        this.allJPanel.add(tmp3);
        this.allJPanel.add(tmp4);

        tmp .setLayout(new GridLayout(4, 1));
        tmp2.setLayout(new GridLayout(4, 1));
        tmp3.setLayout(new BorderLayout());
        tmp4.setLayout(new BorderLayout());

        tmp.add(this.pathField);
        tmp.add(this.allTypes);
        tmp.add(this.extensions);
        tmp.add(tmp4);

        tmp2.add(new JLabel("path: "));
        tmp2.add(new JLabel("type: "));
        tmp2.add(new JLabel("extensions: "));
        tmp2.add(new JLabel("name patern: "));

        tmp3.add(tmp, BorderLayout.CENTER);
        tmp3.add(tmp2, BorderLayout.WEST);

        tmp4.add(this.paternField        , BorderLayout.CENTER);
        tmp4.add(this.launchRenamedScript, BorderLayout.EAST  );

        JScrollPane panelScroll = new JScrollPane(this.console);
        JScrollBar bar = panelScroll.getVerticalScrollBar();

        AtomicInteger maximumValue = new AtomicInteger(bar.getMaximum());
        bar.addAdjustmentListener(e ->
        {
            if ( maximumValue.get() - bar.getMaximum() == 0 )
                return;

            bar.setValue(bar.getMaximum());
            maximumValue.set(bar.getMaximum());
        });

        this.add( panelScroll, BorderLayout.CENTER );
        this.add( tmp3       , BorderLayout.NORTH  );
        this.setJMenuBar(new MenuBar(this));

        Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int)dimension.getHeight();

        this.pack();

        // permet de mettre une taille dynamique. Le pas est de
        Font currentFont = this.getFont().deriveFont(this.getFont().getSize() + (1f * height / IHMGUI.PAS_FONT_SIZE));

        IHMGUI.majAllFonts(this, currentFont);

        this.setSize(this.getWidth() + 100*height/IHMGUI.PAS_FONT_SIZE, this.getHeight()+200);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.extensions.grabFocus();
    }

    public void changeReplacePbyS( boolean isReplacePbyS )
    {
        this.isReplacePbyS = isReplacePbyS;
    }

    private static void majAllFonts(Container comp, Font font)
    {
        comp.setFont(font);

        for (int i = 0; i < comp.getComponentCount(); i++)
            if( comp.getComponent(i) instanceof Container )
                majAllFonts((Container) comp.getComponent(i), font);
    }

    public void setCurrentPath(String path)
    {
        this.pathField.setText(path);
        this.picker.setCurrentDirectory(new File(path));
    }

    public void printInConsole(String s)
    {
        String[] consoleText = this.console.getText().split("<br/>");

        if( consoleText.length > 70 )
        {
            this.console.setText("<html> ");

            for (int cpt = 1; cpt < consoleText.length; cpt++ )
                this.console.setText(this.console.getText() + consoleText[cpt] + "<br/>");
        }

        this.console.setText(this.console.getText() + s.replaceAll("\n", "<br/>") + "<br/>");
        this.changeConsoleColorByUIColor();
    }

    public void setBlockIfNotMathPatern(boolean b)
    {
        this.ctrl.setBlockIfNotMathPatern(b);
    }

    public void setColorForIHMAndChildren(Color baseColor)
    {
        this.setBackground(baseColor);

        for (JPanel panel : this.allJPanel )
        {
            panel.setBackground(baseColor);

            for (int i = 0; i < panel.getComponentCount(); i++)
                if( !(panel.getComponent(i) instanceof JButton) && !(panel.getComponent(i) instanceof JComboBox) )
                panel.getComponent(i).setForeground(baseColor == Color.WHITE ? Color.BLACK : Color.WHITE);
        }

        this.extensions.setBackground(IHMGUI.couleurPlusClair(baseColor, baseColor == Color.WHITE));
        this.pathField.setBackground(IHMGUI.couleurPlusClair(baseColor, baseColor == Color.WHITE));
        this.paternField.setBackground(IHMGUI.couleurPlusClair(baseColor, baseColor == Color.WHITE));
        this.console.setBackground(baseColor);
        //this.launchRenamedScript.setBackground(baseColor);
        this.picker.setBackground(baseColor);

        this.changeConsoleColorByUIColor();

        if( baseColor == Color.WHITE )
        {
            this.console.setForeground(Color.BLACK);
            this.extensions.setForeground(Color.BLACK);
            this.paternField.setForeground(Color.BLACK);
            this.pathField.setForeground(Color.BLACK);
        }
        else
        {
            this.console.setForeground(Color.WHITE);
            this.extensions.setForeground(Color.WHITE);
            this.paternField.setForeground(Color.WHITE);
            this.pathField.setForeground(Color.WHITE);
        }
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

    private static Color couleurPlusClair(Color baseColor, boolean plusFoncer)
    {
        if( plusFoncer )
            return new Color(baseColor.getRed() - ECART_COLOR, baseColor.getGreen() - ECART_COLOR, baseColor.getBlue() - ECART_COLOR);
        else
            return new Color(baseColor.getRed() + ECART_COLOR, baseColor.getGreen() + ECART_COLOR, baseColor.getBlue() + ECART_COLOR);
    }

    public void changeTheme( boolean darkTheme)
    {
        ((MenuBar) this.getJMenuBar()).changeTheme(darkTheme);
    }

    public void changeBlockParam( boolean blockIfNotMatch)
    {
        ((MenuBar) this.getJMenuBar()).changeBlockParam(blockIfNotMatch);
    }

    public void changeReplacePbySParam( boolean isReplacePbyS)
    {
        ((MenuBar) this.getJMenuBar()).changeReplacePbyS(isReplacePbyS);
    }

    public void saveBooleanPreferences( String name, boolean value, boolean clearFile )
    {
        this.ctrl.saveBooleanPreferences(name, value, clearFile);
    }
}
