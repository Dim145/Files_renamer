package renameFiles.ihm;

import renameFiles.Controleur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class IHMGUI extends JFrame
{
    private final Controleur ctrl;

    private final JTextField pathField;
    private final JTextField paternField;
    private final JTextField extensions;

    private final JLabel  console;

    private final Picker picker;

    public IHMGUI(Controleur ctrl)
    {
        super();

        this.ctrl = ctrl;

        this.setTitle("Renamer");

        this.pathField           = new JFormattedTextField();
        this.paternField         = new JFormattedTextField();
        this.extensions          = new JFormattedTextField();
        JButton launchRenamedScript = new JButton("GO");
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
        this.console.setPreferredSize(new Dimension(this.getWidth(), 150));
        this.console.setText("<html>");
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

        this.pack();
        this.setSize(this.getWidth() + 200, this.getHeight());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

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
    }
}
