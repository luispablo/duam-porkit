/*
 * Templater.java
 *
 * Created on July, 2010
 *
 * Copyright(c) 2010 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.services;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class Templater
{

    private static final String TEMPLATES_BASE_PATH = "ar/com/duam/porky/templates/";
    private static final String SUGGEST = "Suggest";
    private static final String TEMPLATES_EXTENSION = ".vm";
    private static final String UTF8 = "UTF-8";

    public String getSuggestMail(String from, String fromMail, String to, String comments) throws Exception
    {
        Template template = this.getTemplate(SUGGEST);

        VelocityContext context = new VelocityContext();
        context.put("from", from);
        context.put("fromMail", fromMail);
        context.put("to", to);
        
        if (comments != null && comments.trim().length() > 0)
        {
            context.put("comments", "<p>También ha dicho:<br/><i>"+ comments +"</i></p>");
        }

        return buildString(template, context);
    }

    private String buildString(Template template, VelocityContext context) throws Exception
    {
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        String output = writer.toString();
        writer.flush();
        writer.close();

        return output;
    }

    private Template getTemplate(String templateName)
            throws Exception
    {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
        properties.setProperty(Velocity.INPUT_ENCODING, UTF8);
        properties.setProperty(Velocity.OUTPUT_ENCODING, UTF8);

        VelocityEngine engine = new VelocityEngine();
        engine.init(properties);

        return engine.getTemplate(TEMPLATES_BASE_PATH + templateName + TEMPLATES_EXTENSION);
    }
}
