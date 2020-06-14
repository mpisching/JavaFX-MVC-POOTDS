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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Produto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneRelatorioQuantidadeProdutosController implements Initializable {

    @FXML
    private TableView<Produto> tableView;
    @FXML
    private TableColumn<Produto, Integer> tableColumnProdutoCodigo;
    @FXML
    private TableColumn<Produto, String> tableColumnProdutoNome;
    @FXML
    private TableColumn<Produto, Double> tableColumnProdutoPreco;
    @FXML
    private TableColumn<Produto, Integer> tableColumnProdutoQuantidade;
    @FXML
    private TableColumn<Produto, Categoria> tableColumnProdutoCategoria;
    @FXML
    private Button buttonImprimir;
    
    private List<Produto> listaProdutos;
    private ObservableList<Produto> observableListProdutos;
    
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
    }    
    
    private void carregarTableView() {
        tableColumnProdutoCodigo.setCellValueFactory(new PropertyValueFactory<>("cdProduto"));
        tableColumnProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnProdutoPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tableColumnProdutoQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableColumnProdutoCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        
        listaProdutos = produtoDAO.listar();
        
        observableListProdutos = FXCollections.observableArrayList(listaProdutos);
        tableView.setItems(observableListProdutos);
    }
    
    //@FXML
    public void handleImprimir() throws JRException {
        URL url = getClass().getResource("/javafxmvc/report/JavaFXMVC_POOTDS_RelatorioQuantidadeProdutos.jasper");
        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(url);
        
        //null: caso não existam filtros
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
        
        //false: não deixa fechar a aplicação principal
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setVisible(true);  
    }
    
}
