package ar.com.duam.components.ui;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;

@SuppressWarnings("rawtypes")
public class DropDownChoiceMonths extends DropDownChoice
{
	private static final long serialVersionUID = 1723701964320291147L;
	private static Logger logger = Logger.getLogger(DropDownChoiceMonths.class);

    @SuppressWarnings("unchecked")
	public DropDownChoiceMonths(String id, Locale locale)
    {
        super(id);
        
        DateFormatSymbols dateSymbols = new DateFormatSymbols(locale);
        String[] monthNames = dateSymbols.getMonths();

        ArrayList<String> months = new ArrayList<String>(Arrays.asList(monthNames));
        months.remove(months.size() - 1);
        
        setChoiceRenderer(new ChoiceRendererMonths());
        setChoices(months);
        
        String month = new Integer(Calendar.getInstance().get(Calendar.MONTH) + 1).toString();
        logger.debug("Setting model to "+ month);
        
        setModel(new Model(month));
        setModelValue(new String[]{month});
		
		setNullValid(false);
    }
    
	@Override
	protected boolean wantOnSelectionChangedNotifications()
	{
		return false;
	}
	
    @SuppressWarnings("serial")
	class ChoiceRendererMonths extends ChoiceRenderer
    {
        @Override
        public String getIdValue(Object object, int index)
        {
            return new Integer(index + 1).toString();
        }
    }
    
}