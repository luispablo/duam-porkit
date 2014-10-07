/*
 * FirstDayOfMonth.java
 *
 * Created on 29/09/2008
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.api.date;

import java.util.Calendar;
import java.util.Date;

/**
 * <p>This class is a subclass of the java.util.Date, and automatically creates
 * itself to the first day of the month.</p>
 * <br>
 * 
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class FirstDayOfMonth extends Date
{
    private static final long serialVersionUID = -4495514863197466117L;

    public FirstDayOfMonth(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        setTime(calendar.getTimeInMillis());
    }

    public FirstDayOfMonth(Calendar calendar)
    {
        this(calendar.getTime());
    }

    public FirstDayOfMonth()
    {
        this(Calendar.getInstance());
    }
    
    /**
     * <p>Return a new instance similar to this, but setting the hours, minutes,
     * seconds and milliseconds to zero.</p>
     * <br/>
     * 
     * @return FirstDayOfMonth
     */
    public FirstDayOfMonth withoutHours()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return new FirstDayOfMonth(calendar);
    }

}