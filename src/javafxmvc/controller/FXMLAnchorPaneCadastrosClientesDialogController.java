/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.domain.Cliente;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastrosClientesDialogController implements Initializable {

    @FXML
    private Label labelClienteNome;

    @FXML
    private Label labelClienteCPF;

    @FXML
    private Label labelClienteTelefone;

    @FXML
    private TextField textFieldClienteNome;

    @FXML
    private TextField textFieldClienteCPF;

    @FXML
    private TextField textFieldClienteTelefone;

    @FXML
    private Button buttonConfirmar;

    @FXML
    private Button buttonCancelar;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Cliente cliente;
    
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     * @return the dialogStage
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmarClicked
     */
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        textFieldClienteNome.setText(cliente.getNome());
        textFieldClienteCPF.setText(cliente.getCpf());
        textFieldClienteTelefone.setText(cliente.getTelefone());
    }
    
    @FXML
    private void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            cliente.setNome(textFieldClienteNome.getText());
            cliente.setCpf(textFieldClienteCPF.getText());
            cliente.setTelefone(textFieldClienteTelefone.getText());
        
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    private void handleButtonCancelar() {
        dialogStage.close();
    }
    
    //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        
        if (textFieldClienteNome.getText() == null || textFieldClienteNome.getText().isEmpty()) {
            errorMessage += "Nome inválido!\n";
        }

        if (textFieldClienteCPF.getText() == null || textFieldClienteCPF.getText().isEmpty()) {
            errorMessage += "CPF inválido!\n";
        }
        
        if (textFieldClienteTelefone.getText() == null || textFieldClienteTelefone.getText().isEmpty()) {
            errorMessage += "Telefone inválido!\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
