package renameFiles.ihm.composants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * The type J console label.
 */
public class JConsoleLabel extends JScrollPane
{
    /**
     * The constant MAX_LINES.
     */
    public static  final int MAX_LINES = 70;

    private final JLabel console;

    private String textTampon;

    /**
     * Instantiates a new J console label.
     */
    public JConsoleLabel()
    {
        super(new JLabel());

        this.console    = (JLabel) this.getViewport().getView();
        this.textTampon = "";

        this.console.setText("<html>");
        this.console.setOpaque(true);
        this.console.setVerticalAlignment(JLabel.BOTTOM);
        this.console.setVerticalTextPosition(JLabel.BOTTOM);

        JScrollBar scrollBar = this.getVerticalScrollBar();
        scrollBar.addAdjustmentListener(new ConsoleBarListener());
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText()
    {
        return this.console.getText();
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText( String text )
    {
        this.console.setText(text);
    }

    /**
     * Add text.
     *
     * @param text the text
     */
    public void addText( String text )
    {
        if( text == null || text.isEmpty() ) return;

        String[] consoleText = this.console.getText().split("<br/>");

        if( consoleText.length > JConsoleLabel.MAX_LINES )
        {
            this.console.setText("<html> ");

            for (int cpt = 1; cpt < consoleText.length; cpt++ )
                this.console.setText(this.console.getText() + consoleText[cpt] + "<br/>");
        }

        String versionCorriger = text.replaceAll("\n", "<br/>");

        this.console.setText(this.console.getText() + versionCorriger + (versionCorriger.endsWith("<br/>") ? "" : "<br/>"));
    }

    public void addTexteToTampon(String texte)
    {
        if( texte == null ) return;

        this.textTampon += texte;

        if( !texte.endsWith("\n") && !texte.endsWith("<br/>") )
            this.textTampon += "\n";
    }

    public void addTamponToConsole()
    {
        this.addText(this.textTampon);

        this.textTampon = "";
    }

    /**
     * Gets nb lines.
     *
     * @return the nb lines
     */
    public int getNbLines()
    {
        return this.console.getText().split("<br/>").length;
    }

    @Override
    public void setBackground(Color bg)
    {
        super.setBackground(bg);

        if( this.console != null )
            this.console.setBackground(bg);
    }

    @Override
    public void setForeground(Color fg)
    {
        super.setForeground(fg);

        if( this.console != null )
            this.console.setForeground(fg);
    }

    @Override
    public void setFont(Font font)
    {
        super.setFont(font);

        if( this.console != null )
            this.console.setFont(font);
    }

    @Override
    public Font getFont()
    {
        return this.console != null ? this.console.getFont() : super.getFont();
    }

    /**
     * The type Console bar listener.
     */
    public static class ConsoleBarListener implements AdjustmentListener
    {
        private int maximum;

        /**
         * Instantiates a new Console bar listener.
         */
        public ConsoleBarListener()
        {
            this.maximum = 0;
        }

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e)
        {
            Adjustable bar = e.getAdjustable();

            if ( this.maximum - bar.getMaximum() == 0 )
                return;

            bar.setValue(bar.getMaximum());
            this.maximum = bar.getMaximum();
        }
    }
}
