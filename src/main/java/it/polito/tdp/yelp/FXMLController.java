/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Locale;
import it.polito.tdp.yelp.model.LocalePiuDistante;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnDistante"
    private Button btnDistante; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaPercorso"
    private Button btnCalcolaPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB1"
    private ComboBox<Locale> cmbB1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB2"
    private ComboBox<Locale> cmbB2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    txtResult.clear();
    String citta= cmbCitta.getValue();
    if (citta== null) {
    	txtResult.setText("Inserire una citta");
    }
    model.creaGrafo(citta);
    txtResult.appendText("Grafo creato!\n");
	txtResult.appendText("VERTICI: "+this.model.nVertici()+"\n");
	txtResult.appendText("ARCHI: "+this.model.nArchi()+"\n");
	cmbB1.getItems().clear();
	cmbB1.getItems().addAll(model.addVertici());
	cmbB2.getItems().clear();
	cmbB2.getItems().addAll(model.addVertici()); 
    	
    }

    @FXML
    void doCalcolaLocaleDistante(ActionEvent event) {
   Locale b1= cmbB1.getValue();
   if (b1==null) {
		txtResult.setText("Devi selezionare un locale dopo aver creato il grafo");
		return;
	}
    	LocalePiuDistante result= model.getLocalePiuDistante(b1);
    	txtResult.setText(result.toString());
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	Locale b1= cmbB1.getValue();
    	Locale b2= cmbB2.getValue();
    	String x= txtX2.getText();
    	double soglia;
    	try {
    		soglia=Double.parseDouble(x);
    	} catch (NumberFormatException e ) {
    		txtResult.appendText("ERRORE FORMATO");
    		return;
    	}
    	if (b2==null) {
    		txtResult.setText("Selezionare il locale dopo aver creato il grafo");
    	}
    	List<Locale> result= this.model.calcolaPercorso(b1, b2, soglia);
    	for (Locale s: result) {
    		txtResult.appendText("\n"+s+"\n");
    	}
    	
    	
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDistante != null : "fx:id=\"btnDistante\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        //assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB1 != null : "fx:id=\"cmbB1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB2 != null : "fx:id=\"cmbB2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbCitta.getItems().clear();
    	cmbCitta.getItems().addAll(model.getCitta());
    }
}
