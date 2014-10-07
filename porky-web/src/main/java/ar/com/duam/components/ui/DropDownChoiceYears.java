package ar.com.duam.components.ui;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;

@SuppressWarnings("rawtypes")
public class DropDownChoiceYears extends DropDownChoice
{
	private static final long serialVersionUID = 2825171316870598917L;

	@SuppressWarnings("unchecked")
	public DropDownChoiceYears(String id, Locale locale)
    {
        super(id);

        Calendar calendar = Calendar.getInstance(locale);
        Integer firstYear = calendar.get(Calendar.YEAR) - 5;
        Integer[] years = new Integer[10];

        for (int i = 0; i < 10; i++)
        {
            years[i] = firstYear++;
        }

        setChoiceRenderer(new ChoiceRendererYears());
        setChoices(Arrays.asList(years));
        setModel(new Model(new Integer(Calendar.getInstance().get(Calendar.YEAR)).toString()));

		setNullValid(false);
	}

	@Override
	protected boolean wantOnSelectionChangedNotifications()
	{
		return false;
	}
				
    class ChoiceRendererYears extends ChoiceRenderer
    {
		private static final long serialVersionUID = 4174885579142576177L;

		@Override
        public Object getDisplayValue(Object arg0)
        {
            return arg0;
        }

        @Override
        public String getIdValue(Object arg0, int arg1)
        {
            return arg0.toString();
        }
    }
}