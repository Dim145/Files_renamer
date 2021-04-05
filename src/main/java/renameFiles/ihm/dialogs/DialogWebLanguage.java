package renameFiles.ihm.dialogs;

import renameFiles.ihm.MenuBar;
import renameFiles.metier.properties.PropertiesManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Locale;

public class DialogWebLanguage extends JDialog
{
    private final JList<Locale> listLanguages;
    private final DefaultListModel<Locale> listModel;

    private final JButton buttonUP;
    private final JButton buttonDOWN;

    public DialogWebLanguage(MenuBar menuBar)
    {
        this.setTitle("Test");
        this.setModal(true);

        this.listModel = new DefaultListModel<>();

        String   pref = null;
        try
        {
            pref = PropertiesManager.getInstance().getPropertie("WebLanguages");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Locale[] listLocales;

        if( pref == null )
        {
            listLocales = MenuBar.availablesLanguages;
        }
        else
        {
            String[] listLanguage = pref.split(",");
            listLocales  = new Locale[listLanguage.length];

            for (int i = 0; i < listLocales.length; i++)
                for (Locale l : MenuBar.availablesLanguages)
                    if( l.getLanguage().equalsIgnoreCase(listLanguage[i]) )
                        listLocales[i] = l;
        }

        for (Locale l : listLocales)
            if( l != null )
                this.listModel.addElement(l);

        this.listLanguages = new JList<>(listModel);
        this.listLanguages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.listLanguages.setCellRenderer(new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                return super.getListCellRendererComponent(list, ((Locale) value).getDisplayName(), index, isSelected, cellHasFocus);
            }
        });

        this.buttonUP   = new JButton("UP");
        this.buttonDOWN = new JButton("DOWN");

        this.buttonUP  .addActionListener(this::changeLanguageOrder);
        this.buttonDOWN.addActionListener(this.buttonUP.getActionListeners()[0]);

        this.listLanguages.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e)
            {
                int val = e.getKeyCode();

                if(val == KeyEvent.VK_RIGHT)
                    buttonUP.doClick();
                else if( val == KeyEvent.VK_LEFT )
                    buttonDOWN.doClick();

                super.keyReleased(e);
            }
        });

        this.add(this.listLanguages);

        JPanel panelBTN = new JPanel();
        panelBTN.add(this.buttonUP);
        panelBTN.add(this.buttonDOWN);

        this.add(panelBTN, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);

        this.setVisible(true);
    }

    private void swap(int a, int b)
    {
        Locale locale1 = listModel.getElementAt(a);
        Locale locale2 = listModel.getElementAt(b);

        listModel.set(a, locale2);
        listModel.set(b, locale1);
    }

    private void changeLanguageOrder(ActionEvent e)
    {
        int moveMe = listLanguages.getSelectedIndex();

        if( moveMe < 0 ) return;

        if (e.getSource() == buttonUP )
        {
            //UP ARROW BUTTON
            if (moveMe != 0)
            {
                //not already at top
                swap(moveMe, moveMe - 1);
                listLanguages.setSelectedIndex(moveMe - 1);
                listLanguages.ensureIndexIsVisible(moveMe - 1);
            }
        }
        else
        {
            //DOWN ARROW BUTTON
            if (moveMe != listModel.getSize() - 1)
            {
                //not already at bottom
                swap(moveMe, moveMe + 1);
                listLanguages.setSelectedIndex(moveMe + 1);
                listLanguages.ensureIndexIsVisible(moveMe + 1);
            }
        }

        changeLocalPreference();
    }

    private void changeLocalPreference()
    {
        StringBuilder propertieString = new StringBuilder();

        for (int i = 0; i < this.listModel.getSize(); i++)
        {
            Locale l = this.listModel.getElementAt(i);

            propertieString.append(l.getLanguage()).append(i == listModel.getSize() - 1 ? "" : ",");
        }

        try
        {
            PropertiesManager.getInstance().setPropertie("WebLanguages", propertieString.toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
