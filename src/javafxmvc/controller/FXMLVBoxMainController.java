/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLVBoxMainController implements Initializable {

    @FXML
    private MenuItem menuItemCadastrosCliente;
    
    @FXML
    private MenuItem menuItemCadastrosProdutos;

    @FXML
    private MenuItem menuItemCadastrosCategorias;    
    
    @FXML
    private MenuItem menuItemProcessosVenda;
    
    @FXML 
    private MenuItem menuItemGraficosVendasPorMes;
    
    @FXML
    private MenuItem menuItemRelatoriosQuantidadeProdutosEstoque;
    
    @FXML
    private AnchorPane anchorPane;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    @FXML
    public void handleMenuItemCadastrosClientes() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosClientes.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemCadastrosProdutos() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosProdutos.fxml"));
        anchorPane.getChildren().setAll(a);
    }  
    
    @FXML
    public void handleMenuItemCadastrosCategorias() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosCategorias.fxml"));
        anchorPane.getChildren().setAll(a);
    } 
    
    @FXML
    public void handleMenuItemProcessosVendas() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneProcessosVendas.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemGraficosVendasPorMes() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneGraficosVendasPorMes.fxml"));
        anchorPane.getChildren().setAll(a);
    }   
    
    @FXML
    public void handleMenuItemRelatorioQuantidadeProdutos() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneRelatorioQuantidadeProdutos.fxml"));
        anchorPane.getChildren().setAll(a);
    }     
}
