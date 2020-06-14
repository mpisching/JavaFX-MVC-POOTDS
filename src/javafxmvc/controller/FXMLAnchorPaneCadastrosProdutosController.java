/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;
import javafxmvc.model.domain.Produto;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastrosProdutosController implements Initializable {

    @FXML
    private TableView<Produto> tableView;

    @FXML
    private TableColumn<Produto, String> tableColumnNome;

    @FXML
    private TableColumn<Produto, String> tableColumnQuantidade;

    @FXML
    private Label labelProdutoCodigo;

    @FXML
    private Label labelProdutoNome;

    @FXML
    private Label labelProdutoPreco;

    @FXML
    private Label labelProdutoQuantidade;

    @FXML
    private Label labelProdutoCategoria;

    @FXML
    private Button buttonInserir;

    @FXML
    private Button buttonAlterar;

    @FXML
    private Button buttonRemover;

    private List<Produto> listaProdutos;
    private ObservableList<Produto> observableListProdutos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        produtoDAO.setConnection(connection);

        carregarTableView();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));

    }

    public void carregarTableView() {
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        
        listaProdutos = produtoDAO.listar();
        
        observableListProdutos = FXCollections.observableArrayList(listaProdutos);
        tableView.setItems(observableListProdutos);
    }
    
    public void selecionarItemTableView(Produto produto) {
        if (produto != null) {
            labelProdutoCodigo.setText(Integer.toString(produto.getCdProduto()));
            labelProdutoNome.setText(produto.getNome());
            labelProdutoQuantidade.setText(Integer.toString(produto.getQuantidade()));
            labelProdutoPreco.setText(Double.toString(produto.getPreco()));
            labelProdutoCategoria.setText(produto.getCategoria().getDescricao());
        } else {
            labelProdutoCodigo.setText("");
            labelProdutoNome.setText("");
            labelProdutoQuantidade.setText("");
            labelProdutoPreco.setText("");
            labelProdutoCategoria.setText("");
        }
    }
    

    @FXML
    public void handleButtonInserir() throws IOException {
        Produto produto = new Produto();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto);
        if (buttonConfirmarClicked) {
            produtoDAO.inserir(produto);
            carregarTableView();
        }
    }
    
    @FXML
    public void handleButtonAlterar() throws IOException {
        Produto produto = tableView.getSelectionModel().getSelectedItem();
        if (produto != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto);
            if (buttonConfirmarClicked) {
                produtoDAO.alterar(produto);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na Tabela.");
            alert.show();
        }
    }
    
    @FXML
    public void handleButtonRemover() throws IOException {
        Produto produto = tableView.getSelectionModel().getSelectedItem();
        if (produto != null) {
            produtoDAO.remover(produto);
            carregarTableView();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na Tabela.");
            alert.show();
        }
    }
    
    public boolean showFXMLAnchorPaneCadastrosProdutosDialog(Produto produto) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastrosProdutosDialogController.class.getResource( 
            "/javafxmvc/view/FXMLAnchorPaneCadastrosProdutosDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();
        
        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de produtos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //Setando o produto ao controller
        FXMLAnchorPaneCadastrosProdutosDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setProduto(produto);
        
        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmarClicked();
    }


}
