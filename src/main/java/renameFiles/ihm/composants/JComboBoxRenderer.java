package renameFiles.ihm.composants;

import renameFiles.metier.enums.FileType;
import renameFiles.metier.resources.ResourceManager;
import renameFiles.metier.resources.Traduisible;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JComboBoxRenderer extends DefaultListCellRenderer implements Traduisible
{
    private Method setText;
    private FileType type;

    private final ListCellRenderer<? super FileType> renderer;

    public JComboBoxRenderer(ListCellRenderer<? super FileType> renderer)
    {
        super();

        this.renderer = renderer;

        try
        {
            this.setText = renderer.getClass().getMethod("setText", String.class);
        }
        catch (NoSuchMethodException exception)
        {
            //exception.printStackTrace();
        }

        ResourceManager.getInstance().addObjectToTranslate(this);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus)
    {
        Component component = this.renderer.getListCellRendererComponent(list, (FileType) value, index, isSelected, cellHasFocus);

        this.type = (FileType) value;

        this.setNewText();

        return component;
    }

    @Override
    public void setNewText()
    {
        if( this.type != null )
        {
            try
            {
                this.setText.invoke(this.renderer, ResourceManager.getInstance().getString(this.type.name()));
            }
            catch (IllegalAccessException | InvocationTargetException e)
            {
                //e.printStackTrace();
            }
        }
    }
}
