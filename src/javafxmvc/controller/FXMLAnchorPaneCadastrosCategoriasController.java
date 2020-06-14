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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastrosCategoriasController implements Initializable {
    @FXML
    private TableView<Categoria> tableView;

    @FXML
    private TableColumn<Categoria, String> tableColumnDescricao;

    @FXML
    private Label labelCodigo;

    @FXML
    private Label labelDescricao;

    @FXML
    private Button buttonInserir;

    @FXML
    private Button buttonAlterar;

    @FXML
    private Button buttonRemover;
    
    @FXML
    private MenuItem contextMenuMenuItemAlterar;
    
    @FXML 
    private MenuItem contextMenuMenuItemRemover;
    private List<Categoria> listaCategorias;
    private ObservableList<Categoria> observableListCategorias;
    
    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDAO.setConnection(connection);
        
        carregarTableViewCategorias();
        
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCategorias(newValue));
    }    
    
    public void carregarTableViewCategorias() {
        tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        listaCategorias = categoriaDAO.listar();
        
        observableListCategorias = FXCollections.observableArrayList(listaCategorias);
        tableView.setItems(observableListCategorias);
    }
    
    public void selecionarItemTableViewCategorias(Categoria categoria) {
        if (categoria != null) {
            labelCodigo.setText(Integer.toString(categoria.getCdCategoria()));
            labelDescricao.setText(categoria.getDescricao());
        } else {
            labelCodigo.setText("");
            labelDescricao.setText("");
        }
    } 
    
    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void handleMouseClickedTableView(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            handleButtonAlterar();
        }
    }   
    
    @FXML
    void handleContextMenuTableViewAlterar(ActionEvent event) throws IOException {
        handleButtonAlterar();
    }

    @FXML
    void handleContextMenuTableViewRemover(ActionEvent event) throws IOException {
        handleButtonRemover();
    }    

    @FXML
    public void handleButtonInserir() throws IOException {
        Categoria categoria = new Categoria();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosCategoriasDialog(categoria);
        if (buttonConfirmarClicked) {
            categoriaDAO.inserir(categoria);
            carregarTableViewCategorias();
        }
    }
    
    @FXML
    public void handleButtonAlterar() throws IOException {
        Categoria categoria = tableView.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosCategoriasDialog(categoria);
            if (buttonConfirmarClicked) {
                categoriaDAO.alterar(categoria);
                carregarTableViewCategorias();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um categoria na Tabela.");
            alert.show();
        }
    }
    
    @FXML
    public void handleButtonRemover() throws IOException {
        Categoria categoria = tableView.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            categoriaDAO.remover(categoria);
            carregarTableViewCategorias();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um categoria na Tabela.");
            alert.show();
        }
    }
    
    public boolean showFXMLAnchorPaneCadastrosCategoriasDialog(Categoria categoria) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastrosCategoriasDialogController.class.getResource( 
            "/javafxmvc/view/FXMLAnchorPaneCadastrosCategoriasDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();
        
        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de categorias");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //Setando o categoria ao controller
        FXMLAnchorPaneCadastrosCategoriasDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCategoria(categoria);
        
        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmarClicked();
    }

        
}
