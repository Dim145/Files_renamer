package renameFiles.ihm.composants;

import renameFiles.ihm.IHMGUI;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.function.Consumer;

public class JIntergerTextField extends JTextField
{
    private static final Clipboard  clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private static final DataFlavor flavor   = DataFlavor.stringFlavor;

    private final IHMGUI ihm;

    public JIntergerTextField( IHMGUI ihm )
    {
        this.ihm = ihm;
    }

    public JIntergerTextField(IHMGUI ihm, String text )
    {
        super(text);

        this.ihm = ihm;
    }

    public JIntergerTextField(IHMGUI ihm, String text, Consumer<? super JTextComponent> action )
    {
        this(ihm, text);

        this.setKeyAction(action);
    }

    public void setKeyAction( Consumer<? super JTextComponent> action )
    {
        for (KeyListener kl : this.getKeyListeners())
            this.removeKeyListener(kl);

        this.addKeyListener(new IntegerKeyListener(action));
    }

    public void addKeyAction( Consumer<? super JTextComponent> action )
    {
        this.addKeyListener(new IntegerKeyListener(action));
    }

    @Override
    public void paste()
    {
        Transferable trans  = clipboard.getContents(this);

        if( trans != null && trans.isDataFlavorSupported(flavor) )
        {
            try
            {
                String texteCopied = (String) trans.getTransferData(flavor);

                if( texteCopied.matches(".*[a-zA-Z].*"))
                {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }

                String txtAv = this.getText();

                super.paste();

                try
                {
                    this.ihm.setNbSDL();
                }
                catch (NumberFormatException err)
                {
                    this.setText(txtAv);
                }
            }
            catch (UnsupportedFlavorException | IOException e)
            {
                // une erreur ? tant pis pour toi.
            }
        }
    }

    public static class IntegerKeyListener implements KeyListener
    {
        private String texteAv = null;

        private final Consumer<? super JTextComponent> action;

        public IntegerKeyListener( Consumer<? super JTextComponent> action )
        {
            this.action = action;
        }

        @Override
        public void keyTyped(KeyEvent e)
        {
            char c = e.getKeyChar();

            if( !Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE )
            {
                if (!e.isControlDown())
                    Toolkit.getDefaultToolkit().beep();

                e.consume();
            }
        }

        @Override
        public void keyPressed(KeyEvent e)
        {
            if( this.texteAv == null )
                this.texteAv = ((JTextComponent) e.getComponent()).getText();
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
            try
            {
                this.action.accept( (JTextComponent) e.getComponent());
            }
            catch (NumberFormatException err)
            {
                Toolkit.getDefaultToolkit().beep();

                ((JTextComponent) e.getComponent()).setText(this.texteAv);
            }

            this.texteAv = null;
        }
    }
}
