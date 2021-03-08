package renameFiles.ihm.composants;

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

    private IntegerKeyListener listener;

    public JIntergerTextField(String text)
    {
        this(text, null);
    }

    public JIntergerTextField(String text, Consumer<? super JTextComponent> action)
    {
        super(text);

        this.setKeyAction(action);
    }

    public void setKeyAction( Consumer<? super JTextComponent> action )
    {
        this.removeKeyListener(this.listener);

        this.listener = new IntegerKeyListener(action);

        this.addKeyListener(this.listener);
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
                    if( this.listener != null && this.listener.getAction() != null )
                        this.listener.getAction().accept(this);
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

        public Consumer<? super JTextComponent> getAction()
        {
            return this.action;
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
                if( this.action != null )
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
