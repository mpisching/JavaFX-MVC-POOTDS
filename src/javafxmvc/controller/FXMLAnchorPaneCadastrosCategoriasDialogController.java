/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Cliente;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastrosCategoriasDialogController implements Initializable {
    @FXML
    private TextField textFieldCategoriaDescricao;

    @FXML
    private Button buttonConfirmar;

    @FXML
    private Button buttonCancelar;
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Categoria categoria;    

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
     * @return the categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        textFieldCategoriaDescricao.setText(categoria.getDescricao());
    }
    
    @FXML
    private void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            categoria.setDescricao(textFieldCategoriaDescricao.getText());
        
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
        
        if (textFieldCategoriaDescricao.getText() == null || textFieldCategoriaDescricao.getText().isEmpty()) {
            errorMessage += "Descrição inválida!\n";
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
