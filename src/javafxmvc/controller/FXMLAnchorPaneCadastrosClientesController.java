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
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastrosClientesController implements Initializable {


    @FXML // fx:id="tableViewClientes"
    private TableView<Cliente> tableViewClientes; // Value injected by FXMLLoader

    @FXML // fx:id="tableColumnClienteNome"
    private TableColumn<Cliente, String> tableColumnClienteNome; // Value injected by FXMLLoader

    @FXML // fx:id="tableColumnClienteCPF"
    private TableColumn<Cliente, String> tableColumnClienteCPF; // Value injected by FXMLLoader

    @FXML // fx:id="labelClienteCodigo"
    private Label labelClienteCodigo; // Value injected by FXMLLoader

    @FXML // fx:id="labelClienteNome"
    private Label labelClienteNome; // Value injected by FXMLLoader

    @FXML // fx:id="labelClienteCPF"
    private Label labelClienteCPF; // Value injected by FXMLLoader

    @FXML // fx:id="labelClienteTelefone"
    private Label labelClienteTelefone; // Value injected by FXMLLoader

    @FXML // fx:id="buttonInserir"
    private Button buttonInserir; // Value injected by FXMLLoader

    @FXML // fx:id="buttonAlterar"
    private Button buttonAlterar; // Value injected by FXMLLoader

    @FXML // fx:id="buttonRemover"
    private Button buttonRemover; // Value injected by FXMLLoader
    
    private List<Cliente> listaClientes;
    private ObservableList<Cliente> observableListClientes;
    
    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        
        carregarTableViewClientes();
        
        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));
    }    
    
    public void carregarTableViewClientes() {
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnClienteCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        
        listaClientes = clienteDAO.listar();
        
        observableListClientes = FXCollections.observableArrayList(listaClientes);
        tableViewClientes.setItems(observableListClientes);
    }
    
    public void selecionarItemTableViewClientes(Cliente cliente) {
        if (cliente != null) {
            labelClienteCodigo.setText(Integer.toString(cliente.getCdCliente()));
            labelClienteNome.setText(cliente.getNome());
            labelClienteCPF.setText(cliente.getCpf());
            labelClienteTelefone.setText(cliente.getTelefone());
        } else {
            labelClienteCodigo.setText("");
            labelClienteNome.setText("");
            labelClienteCPF.setText("");
            labelClienteTelefone.setText("");
        }
    }
    
    @FXML
    public void handleButtonInserir() throws IOException {
        Cliente cliente = new Cliente();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosClientesDialog(cliente);
        if (buttonConfirmarClicked) {
            clienteDAO.inserir(cliente);
            carregarTableViewClientes();
        }
    }
    
    @FXML
    public void handleButtonAlterar() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosClientesDialog(cliente);
            if (buttonConfirmarClicked) {
                clienteDAO.alterar(cliente);
                carregarTableViewClientes();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na Tabela.");
            alert.show();
        }
    }
    
    @FXML
    public void handleButtonRemover() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            clienteDAO.remover(cliente);
            carregarTableViewClientes();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na Tabela.");
            alert.show();
        }
    }
    
    public boolean showFXMLAnchorPaneCadastrosClientesDialog(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastrosClientesDialogController.class.getResource( 
            "/javafxmvc/view/FXMLAnchorPaneCadastrosClientesDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();
        
        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de clientes");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //Setando o cliente ao controller
        FXMLAnchorPaneCadastrosClientesDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);
        
        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmarClicked();
    }
    
}
