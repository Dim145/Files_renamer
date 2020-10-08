package renameFiles.ihm;

import renameFiles.Controleur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

public class IHMGUI extends JFrame
{
    public static final int ECART_COLOR = 25;

    private final Controleur ctrl;

    private final JTextField pathField;
    private final JTextField paternField;
    private final JTextField extensions;

    private final JButton launchRenamedScript;

    private final JLabel  console;

    private final Picker picker;

    private final ArrayList<JPanel> allJPanel;

    public IHMGUI(Controleur ctrl)
    {
        super();

        this.ctrl      = ctrl;
        this.allJPanel = new ArrayList<>();

        this.setTitle("Renamer");
        this.setIconImage(new ImageIcon( APropos.class.getResource("/Images/Files_renamer.png")).getImage());

        this.pathField           = new JFormattedTextField();
        this.paternField         = new JFormattedTextField();
        this.extensions          = new JFormattedTextField();
        this.launchRenamedScript = new JButton("GO");
        this.console             = new JLabel();
        this.picker              = new Picker();

        this.setAutoRequestFocus(false);

        launchRenamedScript.addActionListener(e ->
        {
            if( !this.pathField.getText().isEmpty() && !this.pathField.getText().isBlank() )
            {
                this.ctrl.setExtensions(this.extensions.getText());
                this.ctrl.renameFile(this.pathField.getText(), this.paternField.getText());
            }
            else
            {
                this.printInConsole("<font color=\"red\">Selectionnez un repertoire d'où partir</font>" );            }

        });

        this.extensions.addActionListener(e -> this.paternField.grabFocus());

        this.extensions.setText("mp4,mkv");
        this.launchRenamedScript.setOpaque(true);
        this.console.setPreferredSize(new Dimension(this.getWidth(), 150));
        this.console.setText("<html>");
        this.console.setOpaque(true);
        this.console.setBackground(Color.WHITE);
        this.console.setVerticalAlignment(JLabel.BOTTOM);
        this.console.setVerticalTextPosition(JLabel.BOTTOM);

        this.pathField.addActionListener(e ->
        {
            String s = IHMGUI.this.picker.pickADirectory();

            if( s != null )
                IHMGUI.this.setCurrentPath(s);
            IHMGUI.this.paternField.grabFocus();
        });

        this.pathField.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                String s = IHMGUI.this.picker.pickADirectory();

                if( s != null )
                    IHMGUI.this.setCurrentPath( s );
                IHMGUI.this.paternField.grabFocus();
            }

            @Override
            public void mousePressed(MouseEvent e)
            {

            }

            @Override
            public void mouseReleased(MouseEvent e)
            {

            }

            @Override
            public void mouseEntered(MouseEvent e)
            {

            }

            @Override
            public void mouseExited(MouseEvent e)
            {

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

        tmp .setLayout(new GridLayout(3, 1));
        tmp2.setLayout(new GridLayout(3, 1));
        tmp3.setLayout(new BorderLayout());
        tmp4.setLayout(new BorderLayout());

        tmp.add(this.pathField);
        tmp.add(this.extensions);
        tmp.add(this.paternField);

        tmp2.add(new JLabel("path: "));
        tmp2.add(new JLabel("extensions: "));
        tmp2.add(new JLabel("name patern: "));

        tmp3.add(tmp, BorderLayout.CENTER);
        tmp3.add(tmp2, BorderLayout.WEST);

        tmp4.add(launchRenamedScript, BorderLayout.SOUTH);

        this.add( this.console, BorderLayout.CENTER );
        this.add( tmp3        , BorderLayout.NORTH  );
        this.add( tmp4        , BorderLayout.EAST   );
        this.setJMenuBar(new MenuBar(this));

        this.pack();
        this.setSize(this.getWidth() + 200, this.getHeight());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.extensions.grabFocus();
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
                panel.getComponent(i).setForeground(baseColor == Color.WHITE ? Color.BLACK : Color.WHITE);
        }

        this.extensions.setBackground(IHMGUI.couleurPlusClair(baseColor, baseColor == Color.WHITE));
        this.pathField.setBackground(IHMGUI.couleurPlusClair(baseColor, baseColor == Color.WHITE));
        this.paternField.setBackground(IHMGUI.couleurPlusClair(baseColor, baseColor == Color.WHITE));
        this.console.setBackground(baseColor);
        this.launchRenamedScript.setBackground(baseColor);
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

    public void changeTheme()
    {
        ((MenuBar) this.getJMenuBar()).changeTheme();
    }

    public void changeBlockParam()
    {
        ((MenuBar) this.getJMenuBar()).changeBlockParam();
    }

    public void saveBooleanPreferences( String name, boolean value, boolean clearFile )
    {
        this.ctrl.saveBooleanPreferences(name, value, clearFile);
    }
}
