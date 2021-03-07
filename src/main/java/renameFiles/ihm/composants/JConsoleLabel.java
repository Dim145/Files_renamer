package renameFiles.ihm.composants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class JConsoleLabel extends JScrollPane
{
    private final JLabel     console;

    public JConsoleLabel()
    {
        super(new JLabel());

        this.console = (JLabel) this.getViewport().getView();

        this.console.setText("<html>");
        this.console.setOpaque(true);
        this.console.setVerticalAlignment(JLabel.BOTTOM);
        this.console.setVerticalTextPosition(JLabel.BOTTOM);

        JScrollBar scrollBar = this.getVerticalScrollBar();
        scrollBar.addAdjustmentListener(new ConsoleBarListener());
    }

    public String getText()
    {
        return this.console.getText();
    }

    public void setText( String text )
    {
        this.console.setText(text);
    }

    public void addText( String text )
    {
        String[] consoleText = this.console.getText().split("<br/>");

        if( consoleText.length > 70 )
        {
            this.console.setText("<html> ");

            for (int cpt = 1; cpt < consoleText.length; cpt++ )
                this.console.setText(this.console.getText() + consoleText[cpt] + "<br/>");
        }

        this.console.setText(this.console.getText() + text.replaceAll("\n", "<br/>") + "<br/>");
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
        return this.console.getFont();
    }

    public static class ConsoleBarListener implements AdjustmentListener
    {
        private int maximum;

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
