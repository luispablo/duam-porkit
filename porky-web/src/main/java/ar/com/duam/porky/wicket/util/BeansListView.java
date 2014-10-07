package ar.com.duam.porky.wicket.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

@SuppressWarnings("rawtypes")
public class BeansListView extends ListView
{
	private static final long serialVersionUID = 4774245004777759273L;
	
	private Class itemClass = null;

    @SuppressWarnings("unchecked")
	public BeansListView(String id, List data, Class itemClass)
    {
        super(id, data);

        this.itemClass = itemClass;
    }

    @SuppressWarnings("unchecked")
	protected void populateItem(ListItem item)
    {
        Object object = item.getModelObject();

        Field[] fields = this.itemClass.getDeclaredFields();

        for (int i = 0; i < fields.length; i++)
        {
            try
            {
                Method method = this.itemClass.getDeclaredMethod("get" +
                        fields[i].getName().substring(0, 1).toUpperCase() +
                        fields[i].getName().substring(1), (Class[]) null);

                item.add(new Label(fields[i].getName(), method.invoke(object, (Object[]) null).toString()));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
