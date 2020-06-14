/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Produto;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastrosProdutosDialogController implements Initializable {

    @FXML
    private Label labelProdutoNome;

    @FXML
    private Label labelProdutoQuantidade;

    @FXML
    private Label labelProdutoPreco;

    @FXML
    private TextField textFieldProdutoNome;

    @FXML
    private TextField textFieldProdutoQuantidade;

    @FXML
    private TextField textFieldProdutoPreco;

    @FXML
    private Label labelProdutoCategoria;

    @FXML
    private ComboBox<Categoria> comboBoxCategoria;

    @FXML
    private Button buttonConfirmar;

    @FXML
    private Button buttonCancelar;
    
    private List<Categoria> listaCategorias;
    private ObservableList<Categoria> observableListCategorias;
        
    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Produto produto;  
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDAO.setConnection(connection);
        carregarComboBoxCategorias();
        setFocusLostHandle();
    } 
    
    private void setFocusLostHandle() {
        textFieldProdutoNome.focusedProperty().addListener((ov, oldV, newV) -> {
        if (!newV) { // focus lost
                if (textFieldProdutoNome.getText() == null || textFieldProdutoNome.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    textFieldProdutoNome.requestFocus();
                }
            }
        });
    }
    
//This works fine too:    
//root.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//    focusState(newValue);
//});
//
//private void focusState(boolean value) {
//    if (value) {
//        System.out.println("Focus Gained");
//    }
//    else {
//        System.out.println("Focus Lost");
//    }
//} 
    
    
    public void carregarComboBoxCategorias() {
        listaCategorias = categoriaDAO.listar();
        observableListCategorias = 
                FXCollections.observableArrayList(listaCategorias);
        comboBoxCategoria.setItems(observableListCategorias);
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
     * @return the produto
     */
    public Produto getProduto() {
        return produto;
    }

    /**
     * @param produto the produto to set
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
        textFieldProdutoNome.setText(produto.getNome());
        textFieldProdutoQuantidade.setText(
                Integer.toString(produto.getQuantidade()));
        textFieldProdutoPreco.setText(Double.toString(produto.getPreco()));
        comboBoxCategoria.getSelectionModel().select(produto.getCategoria());
    }    
    
    @FXML
    private void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            produto.setNome(textFieldProdutoNome.getText());
            produto.setQuantidade(
                    Integer.parseInt(textFieldProdutoQuantidade.getText()));
            produto.setPreco(Double.parseDouble(textFieldProdutoPreco.getText()));
            produto.setCategoria(
                    comboBoxCategoria.getSelectionModel().getSelectedItem());

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
        
        if (textFieldProdutoNome.getText() == null || textFieldProdutoNome.getText().isEmpty()) {
            errorMessage += "Nome inválido!\n";
        }

        if (textFieldProdutoPreco.getText() == null || textFieldProdutoPreco.getText().isEmpty()) {
            errorMessage += "Preço inválido!\n";
        }
        
        if (textFieldProdutoQuantidade.getText() == null || textFieldProdutoQuantidade.getText().isEmpty()) {
            errorMessage += "Quantidade inválida!\n";
        }
        
        if (comboBoxCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma categoria!\n";
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
