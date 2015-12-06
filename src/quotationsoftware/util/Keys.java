/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quotationsoftware.util;

/**
 *
 * @author sakit
 */
public class Keys {
    /*=======================SQL statement========================================*/
    public final static String MAX_ID_QUERY = "select max(id) + 1 as next_id from ";
    public final static String QUATATION_LIST_QUERY = "select * from quotations";
    public final static String ITEM_LIST_QUERY = "select * from items where quotation_id = ? and bathroom_id = ?";
    public final static String BATHROOM_LIST_QUERY = "select * from bathrooms";
    public final static String DELETE_QUATATION_QUERY = "delete from quotations where id = ?";
    public final static String DELETE_QUATATION_FROM_BATH_QUERY = "delete from bathrooms where id in (select bathroom_id from items where quotation_id = ?)";
    public final static String DELETE_QUATATION_FROM_ITEM_QUERY = "delete from items where quotation_id = ?";
    public final static String DELETE_ITEM_QUERY = "delete from items where quotation_id = ? and bathroom_id = ?";
    public final static String DELETE_BATHROOM_QUERY = "delete from bathrooms where id = ?";
    public final static String INSERT_QUATATION_SQL = "/resources/sql/insert-quotations.sql";
    public final static String INSERT_ITEM_SQL = "/resources/sql/insert-items.sql";
    public final static String INSERT_BATHROOM_SQL = "/resources/sql/insert-bathrooms.sql";
    public final static String BATHROOM_BY_ID_SQL = "/resources/sql/select-bathrooms-by-quotation-id.sql";
    /*=================================END========================================*/
    public final static String PROG_NAME = "prog_name";
    public final static String[] QUOTE_FORMAT = {"Item Wise Quote", "Bathroom Wise Quote", "Comparative Bathroom Quote"};
    public final static String CMB_TXT="cmb_txt";
    public final static String NOT_LBL = "-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold; -fx-label-padding: 25px 15px 25px 5px; ";
    public final static String NOT_HBOX = "-fx-background-color: #00000099; -fx-background-radius: 4px; ";
    /*=========================FXML VIEW NAME=====================================*/
    public static final String MAIN_SCREEN = "/resources/fxml/main.fxml";
    public static final String HOME = "/resources/fxml/home.fxml";
    public static final String NEW_QUOTE = "/resources/fxml/new_quote.fxml";
    public static final String ITEM_WISE_QUOTE = "/resources/fxml/item_wise_quote.fxml";
    public static final String BATHROOM_WISE = "/resources/fxml/bathroom-wise-quote.fxml";
    public static final String BATHROM_WISE_SUMMARY = "/resources/fxml/bathroom-wise-summary-sheet.fxml";
    public static final String QUOTE_HISTORY = "/resources/fxml/quote-history.fxml";
    public static final String SETTINGS = "/resources/fxml/settings.fxml";
    public static final String HELP = "/resources/fxml/help.fxml";
    public static final String CLIENT_INFO = "/resources/fxml/client-info.fxml";
    public static final String ITEM_CHOOSER = "/resources/fxml/item-chooser.fxml";
    public static final String ITEM_WISE_QUOTE_HISTORY = "/resources/fxml/item-wise-quote-history.fxml";
    public static final String BATHROOM_WISE_QUOTE_HISTORY = "/resources/fxml/bathroom-wise-quote-history.fxml";    
    public static final String PURCHASE_ORDERS = "/resources/fxml/purchase-orders.fxml";    
    public static final String PURCHASE_ORDER_ITEMS = "/resources/fxml/purchase-order-items.fxml";    
    public static final String COMPARATIVE_BATHROOM_QUOTE = "/resources/fxml/comparative_bathroom_quote.fxml";    ;
    /*=========================FXML VIEW NAME=====================================*/
}
