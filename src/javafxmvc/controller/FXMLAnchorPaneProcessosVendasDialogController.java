/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;
import javafxmvc.model.domain.ItemDeVenda;
import javafxmvc.model.domain.Produto;
import javafxmvc.model.domain.Venda;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneProcessosVendasDialogController implements Initializable {

    @FXML
    private ComboBox<Cliente> comboBoxClientes;
    @FXML
    private DatePicker datePickerData;
    @FXML
    private CheckBox checkBoxPago;
    @FXML
    private TableView<ItemDeVenda> tableViewItensDeVenda;
    @FXML
    private TableColumn<ItemDeVenda, Produto> tableColumnProduto;
    @FXML
    private TableColumn<ItemDeVenda, Integer> tableColumnQuantidade;
    @FXML
    private TableColumn<ItemDeVenda, Double> tableColumnValor;
    @FXML
    private TextField textFieldValor;
    @FXML
    private ComboBox<Produto> comboBoxProduto;
    @FXML
    private TextField textFieldQuantidadeProduto;
    @FXML
    private Button buttonAdicionar;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private ContextMenu contextMenuTableView;
    @FXML
    private MenuItem contextMenuItemAtualizarQtd;
    @FXML
    private MenuItem contextMenuItemRemoverItem;
    

    private List<Cliente> listaClientes;
    private List<Produto> listaProdutos;
    private ObservableList<Cliente> observableListClientes;
    private ObservableList<Produto> observableListProdutos;
    private ObservableList<ItemDeVenda> observableListItensDeVenda;

    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Venda venda;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        produtoDAO.setConnection(connection);
        carregarComboBoxClientes();
        carregarComboBoxProdutos();
        tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }

    private void carregarComboBoxClientes() {
        listaClientes = clienteDAO.listar();
        observableListClientes = FXCollections.observableArrayList(listaClientes);
        comboBoxClientes.setItems(observableListClientes);
    }

    private void carregarComboBoxProdutos() {
        listaProdutos = produtoDAO.listar();
        observableListProdutos = FXCollections.observableArrayList(listaProdutos);
        comboBoxProduto.setItems(observableListProdutos);
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
     * @return the venda
     */
    public Venda getVenda() {
        return venda;
    }

    /**
     * @param venda the venda to set
     */
    public void setVenda(Venda venda) {
        this.venda = venda;
        comboBoxClientes.getSelectionModel().select(this.venda.getCliente());
        datePickerData.setValue(this.venda.getData());
        checkBoxPago.setSelected(this.venda.isPago());
        observableListItensDeVenda = FXCollections.observableArrayList(
                this.venda.getItensDeVenda());
        tableViewItensDeVenda.setItems(observableListItensDeVenda);
        textFieldValor.setText(String.format("%.2f", venda.getValor()));
    }

    @FXML
    public void handleButtonAdicionar() {
        Produto produto;
        ItemDeVenda itemDeVenda = new ItemDeVenda();
        if (comboBoxProduto.getSelectionModel().getSelectedItem() != null) {
            produto = comboBoxProduto.getSelectionModel().getSelectedItem();
            if (produto.getQuantidade() >= Integer.parseInt(textFieldQuantidadeProduto.getText())) {
                itemDeVenda.setProduto(produto);
                itemDeVenda.setQuantidade(Integer.parseInt(textFieldQuantidadeProduto.getText()));
                itemDeVenda.setValor(produto.getPreco() * itemDeVenda.getQuantidade());
                itemDeVenda.setVenda(venda);
                venda.getItensDeVenda().add(itemDeVenda);
                venda.setValor(venda.getValor() + itemDeVenda.getValor());
                observableListItensDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
                tableViewItensDeVenda.setItems(observableListItensDeVenda);
                textFieldValor.setText(String.format("%.2f", venda.getValor()));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problemas na escolha do produto");
                alert.setContentText("Não existe quantidade suficiente de produtos para venda.");
                alert.show();
            }
        }
    }

    @FXML
    private void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            venda.setCliente(comboBoxClientes.getSelectionModel().getSelectedItem());
            venda.setPago(checkBoxPago.isSelected());
            venda.setData(datePickerData.getValue());

            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleButtonCancelar() {
        dialogStage.close();
    }
    
    @FXML
    void handleTableViewMouseClicked(MouseEvent event) {
        ItemDeVenda itemDeVenda
                = tableViewItensDeVenda.getSelectionModel().getSelectedItem();
        if (itemDeVenda == null) {
            contextMenuItemAtualizarQtd.setDisable(true);
            contextMenuItemRemoverItem.setDisable(true);
        } else {
            contextMenuItemAtualizarQtd.setDisable(false);
            contextMenuItemRemoverItem.setDisable(false);
        }

    }    

    @FXML
    private void handleContextMenuItemAtualizarQtd() {
        ItemDeVenda itemDeVenda
                = tableViewItensDeVenda.getSelectionModel().getSelectedItem();
        int index = tableViewItensDeVenda.getSelectionModel().getSelectedIndex();
        itemDeVenda.setQuantidade(
                Integer.parseInt(inputDialog(itemDeVenda.getQuantidade())));
        //venda.getItensDeVenda().set(venda.getItensDeVenda().indexOf(itemDeVenda),itemDeVenda);
        venda.getItensDeVenda().set(index, itemDeVenda);
        venda.setValor(venda.getValor() - itemDeVenda.getValor());
        itemDeVenda.setValor(itemDeVenda.getProduto().getPreco() * itemDeVenda.getQuantidade());
        venda.setValor(venda.getValor() + itemDeVenda.getValor());
        tableViewItensDeVenda.refresh();
        textFieldValor.setText(String.format("%.2f", venda.getValor()));
    }
    
    private String inputDialog(int value) {
        TextInputDialog dialog = new TextInputDialog(Integer.toString(value));
        dialog.setTitle("Entrada de dados.");
        dialog.setHeaderText("Atualização da quantidade de produtos.");
        dialog.setContentText("Quantidade: ");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        //if (result.isPresent()){
        //    System.out.println("Your name: " + result.get());
        return result.get();
    }

    @FXML
    private void handleContextMenuItemRemoverItem() {
        ItemDeVenda itemDeVenda
                = tableViewItensDeVenda.getSelectionModel().getSelectedItem();
        int index = tableViewItensDeVenda.getSelectionModel().getSelectedIndex();
        //itemDeVenda.setQuantidade(
        //        Integer.parseInt(inputDialog(itemDeVenda.getQuantidade())));
        venda.setValor(venda.getValor() - itemDeVenda.getValor());
        venda.getItensDeVenda().remove(index);
        observableListItensDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
        tableViewItensDeVenda.setItems(observableListItensDeVenda);

        //itemDeVenda.setValor(itemDeVenda.getProduto().getPreco() * itemDeVenda.getQuantidade());
//        tableViewItensDeVenda.refresh();
        textFieldValor.setText(String.format("%.2f", venda.getValor()));
    }

    //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (comboBoxClientes.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Cliente inválido!\n";
        }

        if (datePickerData.getValue() == null) {
            errorMessage += "Data inválida!\n";
        }

        if (observableListItensDeVenda == null) {
            errorMessage += "Itens de venda inválidos!\n";
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
