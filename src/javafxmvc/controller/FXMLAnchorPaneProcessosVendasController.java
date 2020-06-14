/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.model.dao.ItemDeVendaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.dao.VendaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;
import javafxmvc.model.domain.ItemDeVenda;
import javafxmvc.model.domain.Produto;
import javafxmvc.model.domain.Venda;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneProcessosVendasController implements Initializable {

    @FXML
    private TableView<Venda> tableView;
    @FXML
    private TableColumn<Venda, Integer> tableColumnVendaCodigo;
    @FXML
    private TableColumn<Venda, LocalDate> tableColumnVendaData;
    @FXML
    private TableColumn<Venda, Venda> tableColumnVendaCliente;
    @FXML
    private Label labelVendaCodigo;
    @FXML
    private Label labelVendaData;
    @FXML
    private Label labelVendaValor;
    @FXML
    private Label labelVendaCliente;
    @FXML
    private CheckBox checkBoxVendaPago;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;

    private List<Venda> listaVendas;
    private ObservableList<Venda> observableListVendas;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VendaDAO vendaDAO = new VendaDAO();
    private final ItemDeVendaDAO itemDeVendaDAO = new ItemDeVendaDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vendaDAO.setConnection(connection);

        carregarTableView();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }

    public void carregarTableView() {
        tableColumnVendaCodigo.setCellValueFactory(new PropertyValueFactory<>("cdVenda"));
        tableColumnVendaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnVendaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        listaVendas = vendaDAO.listar();

        observableListVendas = FXCollections.observableArrayList(listaVendas);
        tableView.setItems(observableListVendas);
    }

    public void selecionarItemTableView(Venda venda) {
        if (venda != null) {
            labelVendaCodigo.setText(Integer.toString(venda.getCdVenda()));
            labelVendaData.setText(String.valueOf(
                    venda.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            labelVendaValor.setText(String.format("%.2f", venda.getValor()));
            checkBoxVendaPago.setSelected(venda.isPago());
            labelVendaCliente.setText(venda.getCliente().getNome());
        } else {
            labelVendaCodigo.setText("");
            labelVendaData.setText("");
            labelVendaValor.setText("");
            checkBoxVendaPago.setSelected(false);
            labelVendaCliente.setText("");
        }
    }

    @FXML
    private void handleButtonInserir(ActionEvent event) throws IOException, SQLException {
        Venda venda = new Venda();
        List<ItemDeVenda> itensDeVenda = new ArrayList<>();
        venda.setItensDeVenda(itensDeVenda);
        boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessosVendasDialog(venda);
        if (buttonConfirmarClicked) {
            try {
                connection.setAutoCommit(false);
                vendaDAO.setConnection(connection);
                vendaDAO.inserir(venda);
                itemDeVendaDAO.setConnection(connection);
                produtoDAO.setConnection(connection);
                for (ItemDeVenda itemDeVenda: venda.getItensDeVenda()) {
                    Produto produto = itemDeVenda.getProduto();
                    itemDeVenda.setVenda(vendaDAO.buscarUltimaVenda());
                    itemDeVendaDAO.inserir(itemDeVenda);
                    produto.setQuantidade(
                            produto.getQuantidade() - itemDeVenda.getQuantidade());
                    produtoDAO.alterar(produto);
                }
                connection.commit();
                carregarTableView();
            } catch (SQLException exc) {
                try {
                    connection.rollback();
                } catch (SQLException exc1) {
                    Logger.getLogger(FXMLAnchorPaneProcessosVendasController.class.getName()).log(Level.SEVERE, null, exc1);
                }
                Logger.getLogger(FXMLAnchorPaneProcessosVendasController.class.getName()).log(Level.SEVERE, null, exc);
            }   
        }
    }

    @FXML
    private void handleButtonAlterar(ActionEvent event) throws IOException {
        Venda venda = tableView.getSelectionModel().getSelectedItem();
        if (venda != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessosVendasDialog(venda);
            if (buttonConfirmarClicked) {
                vendaDAO.alterar(venda);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um venda na Tabela.");
            alert.show();
        }        
    }

    @FXML
    private void handleButtonRemover(ActionEvent event) throws SQLException {
        Venda venda = tableView.getSelectionModel().getSelectedItem();
        if (venda != null) {
            try {
                connection.setAutoCommit(false);
                vendaDAO.setConnection(connection);
                itemDeVendaDAO.setConnection(connection);
                produtoDAO.setConnection(connection);
                for (ItemDeVenda itemDeVenda : venda.getItensDeVenda()) {
                    Produto produto = itemDeVenda.getProduto();
                    produto.setQuantidade(produto.getQuantidade() + itemDeVenda.getQuantidade());
                    produtoDAO.alterar(produto);
                    itemDeVendaDAO.remover(itemDeVenda);
                }
                vendaDAO.remover(venda);
                connection.commit();
                carregarTableView();
            } catch (SQLException exc) {
                try {
                    connection.rollback();
                } catch (SQLException exc1) {
                    Logger.getLogger(FXMLAnchorPaneProcessosVendasController.class.getName()).log(Level.SEVERE, null, exc1);
                }
                Logger.getLogger(FXMLAnchorPaneProcessosVendasController.class.getName()).log(Level.SEVERE, null, exc);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Por favor, escolha uma venda na tabela!");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneProcessosVendasDialog(Venda venda) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneProcessosVendasDialogController.class.getResource(
                "/javafxmvc/view/FXMLAnchorPaneProcessosVendasDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de vendas");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o venda ao controller
        FXMLAnchorPaneProcessosVendasDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVenda(venda);

        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }

}
