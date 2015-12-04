/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quotationsoftware.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import quotationsoftware.dao.QuotationDAO;
import quotationsoftware.model.Item;
import quotationsoftware.model.Quotation;
import quotationsoftware.util.Formats;

/**
 *
 * @author sakit
 */
public class ItemWiseQuote implements Initializable {

    @FXML
    private Label customerName;
    @FXML
    private Label customerAddress;
    @FXML
    private Label customerContact;
    @FXML
    private Label enquiryDate;
    @FXML
    private Label quotationNumber;
    @FXML
    private Label shopCity;
    @FXML
    private Label shopName;
    @FXML
    private Label shopAddress;
    @FXML
    private Text textMessage;
    @FXML
    private TableColumn<Item, Integer> itemNumber;
    @FXML
    private TableColumn<Item, String> itemCode;
    @FXML
    private TableColumn<Item, String> itemDescription;
    @FXML
    private TableColumn<Item, Integer> itemQuantity;
    @FXML
    private TableColumn<Item, Integer> mrp;
    @FXML
    private TableColumn<Item, Integer> discount;
    @FXML
    private TableColumn<Item, Integer> nrp;
    @FXML
    private TableColumn<Item, Integer> vat;
    @FXML
    private TableColumn<Item, Integer> aev;
    @FXML
    private TableColumn<Item, Integer> vta;
    @FXML
    private TableColumn<Item, Integer> total;
    @FXML
    private TableColumn<Item, Integer> image;
    @FXML
    private TableColumn<Item, Boolean> delImage;
    @FXML
    private TableView<Item> tableView;

    private ResourceBundle rb;
    private final QuotationDAO qdao = QuotationDAO.getInstance();
    private final Quotation quota = qdao.getQuotation();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rb = resources;
        initQuatationInfo();
        initTable();
        tableView.setPlaceholder(new Text(rb.getString("empty")));
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        switch(quota.getHeadings()){
            case "MRP":
                hideColumn(nrp,discount,vat,aev,vta);
                break;
            case "NRP":
                hideColumn(mrp,discount,vat,aev,vta);
                break;
            case "MRP With NRP & Discounts":
                hideColumn(vat,aev,vta);
                break;
            case "MRP With Discounts":
                if(quota.getVat().equals("VAT in total"))
                    hideColumn(nrp,vta);
                else
                    hideColumn();
        }
        
    }

    private void initQuatationInfo() {
        textMessage.setText(String.format(
                rb.getString("cl_msg"),
                quota.getEnquiryReferenceNo(),
                Formats.toDateFormat(quota.getEnquiryDate())));
        customerName.textProperty().bind(quota.customerNameProperty());
        customerAddress.textProperty().bind(quota.customerAddressProperty());
        customerContact.textProperty().bind(Bindings.concat(quota.contactNumberProperty(), "/", quota.emailIdProperty()));
        enquiryDate.setText(Formats.toDateFormat(quota.getEnquiryDate()));

        InputStream in = null;
        Properties prop = null;
        try {
            in = getClass().getResourceAsStream("/resources/settings/preference");
            prop = new Properties();
            if (in != null) {
                prop.load(in);
                shopName.setText(prop.getProperty("company_name"));
                shopAddress.setText(prop.getProperty("company_address"));
                shopCity.setText(prop.getProperty("company_address"));
            }else{
                System.err.println("file not found");
            }
        } catch (FileNotFoundException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                }
            }
        }
    }

    @FXML
    private void back(ActionEvent e) {
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.getOnCloseRequest().handle(null);
        stage.close();
    }

    private void hideColumn(TableColumn<Item, ?>... columns) {
        image.setVisible(quota.getDisplay().equals("Display with image"));
        final ObservableList<TableColumn<Item, ?>> list = tableView.getColumns();
        if(columns.length!=0){
            int index = 0;
            for(int i=0;i<columns.length;i++){
                index = list.indexOf(columns[i]);
                list.get(index).setVisible(false);                   
            }
        }else{
            for(int i=0,len=list.size();i<len;i++)
                list.get(i).setVisible(true);
        }
    }

    private void initTable() {
        itemNumber.setCellValueFactory(new PropertyValueFactory<>("serialNo"));
        itemNumber.setCellFactory((TableColumn<Item,Integer> param)->{
            return new NumberCell();
        });
        itemCode.setCellValueFactory(new PropertyValueFactory<>("itemCodeL"));
        itemDescription.setCellValueFactory(new PropertyValueFactory<>("itemDescriptionS"));
        itemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        mrp.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        nrp.setCellValueFactory(new PropertyValueFactory<>("nrp"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        vat.setCellValueFactory(new PropertyValueFactory<>("vatPercentage"));
        aev.setCellValueFactory(new PropertyValueFactory<>("nrpWithoutVat"));
        vta.setCellValueFactory(new PropertyValueFactory<>("vatAmount"));
        total.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        image.setCellValueFactory(new PropertyValueFactory<>("image"));
        
        //add delete image to each row if new row not empty
        delImage.setCellValueFactory((TableColumn.CellDataFeatures<Item, Boolean> param) -> {
            return new SimpleBooleanProperty(param.getValue()!=null);
        });
        
        //handle button action for deleteing the row
        delImage.setCellFactory((TableColumn<Item, Boolean> param) -> {
            return new ButtonCell();
        });
    }

    @FXML
    private void save(){
        Item item = new Item();
        System.out.println("new item added");
        tableView.getItems().add(item);
    }
    
    //defining button cell class
    private class ButtonCell extends TableCell<Item, Boolean>{
        final Button delete = new Button();
        final ImageView delImage = new ImageView(
                new Image(getClass().getResourceAsStream("/resources/images/delete-small.png")));
        
        ButtonCell(){
            delImage.setFitHeight(25);
            delImage.setFitWidth(25);
            delete.setGraphic(delImage);
            delete.setId("delete");
            delete.setOnAction(e->{
                Item currentItem = (Item) ButtonCell.this.
                        getTableView().getItems().get(ButtonCell.this.getIndex());
                	//remove selected item from the table list
                tableView.getItems().remove(currentItem);
            });
        }
        
        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty); 
            if(!empty)
                setGraphic(delete);
            else
                setGraphic(null);
        }        
    }
    
    private class NumberCell extends TableCell<Item, Integer>{

        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty); 
            if(empty)
                setText("");
            else
                setText(String.valueOf(getIndex()+1));
        }
    
    }
}
