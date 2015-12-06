package quotationsoftware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import quotationsoftware.model.Item;
import quotationsoftware.util.Keys;
import quotationsoftware.util.DbHandler;

/**
 * Singleton data access class which handles item database operations.
 */
public class ItemDAO {

    private static final ItemDAO instance = new ItemDAO();

    private List<Item> listItem;
    private Connection connection;
    
    private ItemDAO() {
        try {
            connection = quotationsoftware.util.DbHandler.getConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ItemDAO getInstance() {
        return instance;
    }

    /**
     * Returns a list of items belonging to the specified quotation and bathroom.
     * @param quotationId
     * @param bathroomId
     * @return 
     * @throws java.lang.Exception
     */
    public List<Item> getListItem(int quotationId, int bathroomId) throws Exception {
        listItem = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(Keys.ITEM_LIST_QUERY);) {
            preparedStatement.setInt(1, quotationId);
            preparedStatement.setInt(2, bathroomId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = new Item();
                    item.setId(resultSet.getInt("id"));
                    item.setQuotationId(quotationId);
                    item.setBathroomId(resultSet.getInt("bathroom_id"));
                    item.setSerialNo(resultSet.getInt("serial_no"));
                    item.setLongItemCode(resultSet.getString("item_code_l"));
                    item.setMediumItemCode(resultSet.getString("item_code_m"));
                    item.setShortItemCode(resultSet.getString("item_code_s"));
                    item.setShortItemDescription(resultSet.getString("item_description_s"));
                    item.setLongItemCode(resultSet.getString("item_description_l"));
                    item.setQuantity(resultSet.getInt("quantity"));
                    item.setMrp(resultSet.getDouble("mrp"));
                    item.setNrp(resultSet.getDouble("nrp"));
                    item.setDiscount(resultSet.getDouble("discount"));
                    item.setVatPercentage(resultSet.getDouble("vat_percentage"));
                    //item.setNrpWithoutVat(resultSet.getDouble("nrp_without_vat"));
                    item.setRdWithoutVat(resultSet.getDouble("rd_without_vat"));
                    //item.setVatAmount(resultSet.getDouble("vat_amount"));
                    item.setRdIncludingVat(resultSet.getDouble("rd_including_vat"));
                    item.setMarginAmount(resultSet.getDouble("margin_amount"));
                    item.setMarginPercentage(resultSet.getDouble("margin_percentage"));
                    item.setBathroomElement(resultSet.getInt("bathroom_element"));
                    item.setImage(resultSet.getInt("image"));
                    //item.setTotalAmount(resultSet.getDouble("total_amount"));
                    listItem.add(item);
                }
            }
        }
        return listItem;
    }

    /**
     * Saves a list of item to the specified quotation and bathroom.
     * @param listSave
     * @param quotationId
     * @param bathroomId
     * @throws java.lang.Exception
     */
    public void save(List<Item> listSave, int quotationId, int bathroomId) throws Exception {        
        final String sql = DbHandler.getSqlScript(Keys.INSERT_ITEM_SQL);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Item item : listSave) {
                preparedStatement.setInt(1, quotationId);
                preparedStatement.setInt(2, bathroomId);
                preparedStatement.setInt(3, item.getSerialNo());
                preparedStatement.setString(4, item.getLongItemCode());
                preparedStatement.setString(5, item.getMediumItemCode());
                preparedStatement.setString(6, item.getShortItemCode());
                preparedStatement.setString(7, item.getShortItemDescription());
                preparedStatement.setString(8, item.getDetailedItemDescription());
                preparedStatement.setDouble(9, item.getQuantity());
                preparedStatement.setDouble(10, item.getMrp());
                preparedStatement.setDouble(11, item.getNrp());
                preparedStatement.setDouble(12, item.getDiscount());
                preparedStatement.setDouble(13, item.getVatPercentage());
                preparedStatement.setDouble(14, item.getNrpWithoutVat());
                preparedStatement.setDouble(15, item.getRdWithoutVat());
                preparedStatement.setDouble(16, item.getVatAmount());
                preparedStatement.setDouble(17, item.getRdIncludingVat());
                preparedStatement.setDouble(18, item.getMarginAmount());
                preparedStatement.setDouble(19, item.getMarginPercentage());
                preparedStatement.setDouble(20, item.getImage());
                preparedStatement.setInt(21, item.getBathroomElement());
                preparedStatement.setDouble(22, item.getTotalAmount());
                System.out.println("item save for bath "+bathroomId+" quote "+quotationId);
                preparedStatement.execute();
            }
        }
        connection.commit();
    }

    /**
     * Deletes all items belonging to the specified quotation and bathroom.
     * @param quotationId
     * @param bathroomId
     * @throws java.lang.Exception
     */
    public void delete(int quotationId, int bathroomId) throws Exception {
        try (PreparedStatement preparedStatement = connection.prepareStatement(Keys.DELETE_ITEM_QUERY)) {
            preparedStatement.setInt(1, quotationId);
            preparedStatement.setInt(2, bathroomId);
            preparedStatement.execute();
        }
        connection.commit();
    }
}
