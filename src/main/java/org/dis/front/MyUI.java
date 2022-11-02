package org.dis.front;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.dis.back.BRException;
import org.dis.back.EmpleadoBR;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private TextField creaLabel(String texto){
        TextField etiqueta = new TextField();
        etiqueta.setCaption(texto);
        return etiqueta;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout salarioBruto = new HorizontalLayout();
        final HorizontalLayout salarioNeto = new HorizontalLayout();
        final VerticalLayout salarioBrutoContenedor = new VerticalLayout();
        final VerticalLayout salarioNetoContenedor = new VerticalLayout();

        TextField tipo = creaLabel("Tipo de empleado");
        TextField ventasMes = creaLabel("Ventas del mes");
        TextField horasExtra = creaLabel("Horas extra");
        salarioBruto.addComponents(tipo, ventasMes, horasExtra);

        TextField inputSalarioBruto = creaLabel("Salario bruto del empleado");
        salarioNeto.addComponent(inputSalarioBruto);

        Button botonSalarioBruto = new Button("Calcula Salario Bruto");
        botonSalarioBruto.addClickListener(e -> {
            String tipoEmpleadoIn = tipo.getValue();
            double ventasMesIn = Double.parseDouble( ventasMes.getValue()) ;
            double horasExtraIn = Double.parseDouble( horasExtra.getValue()) ;

            EmpleadoBR empleado = new EmpleadoBR();

            try {
                double resultado = empleado.calculaSalarioBruto(tipoEmpleadoIn, ventasMesIn, horasExtraIn);
                Label labelSalarioBruto = new Label("El salario bruto obtenido es: "+ resultado +"€");
                salarioBrutoContenedor.addComponent(labelSalarioBruto);
            } catch (BRException ex) {
                Label labelSalarioBruto = new Label( ex.getMessage() );
                salarioBrutoContenedor.addComponent(labelSalarioBruto);
            }
        });

        Button botonSalarioNeto = new Button("Calcula Salario Neto");
        botonSalarioNeto.addClickListener(e -> {
        });

        salarioBrutoContenedor.addComponents(salarioBruto, botonSalarioBruto);
        salarioNetoContenedor.addComponents(salarioNeto, botonSalarioNeto);

        TabSheet tabs = new TabSheet();
        tabs.addTab(salarioBrutoContenedor).setCaption("Calcula Salario Bruto");
        tabs.addTab(salarioNetoContenedor).setCaption("Calcula Salario Neto");

        layout.addComponent(tabs);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
