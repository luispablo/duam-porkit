package ar.com.duam.porky.graphics;

import org.apache.wicket.markup.html.image.Image;


/**
 * @author Luis Pablo Gallo (luispablo.gallo@gmail.com)
 */
public class ConceptosBarGraph 
{
    
    private String wicketId = null;
    private int width = 150; // Valor por defecto.
    private int height = 200; // Valor por defecto.
    private String fontFamilyName = "Arial";
    
    /**
     * Crea una nueva instancia de IncomeOutcomeBarGraph.
     *
     * @param wicketId ID del <img> de wicket en el HTML.
     */
    public ConceptosBarGraph(String wicketId) 
    {
        this.wicketId = wicketId;
    }
    
    /**
     * Devuelve una imagen generada a partir de los parámetros indicados.
     *
     * @return Image
     */
    public Image getImage()
    {
        return new Image(this.wicketId, this.wicketId);
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }

    public String getFontFamilyName() {
        return fontFamilyName;
    }

    public void setFontFamilyName(String fontFamilyName) {
        this.fontFamilyName = fontFamilyName;
    }
    
}