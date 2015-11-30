package quotationsoftware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import quotationsoftware.model.Bathroom;
import quotationsoftware.util.DbHandler;
import quotationsoftware.util.Keys;

/**
 * Singleton data access class which handles bathroom database operations.
 */
public class BathroomDAO {

    private static final BathroomDAO instance = new BathroomDAO();
    private Connection connection;
            
    private BathroomDAO() {
        try {
            connection = DbHandler.getConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Bathroom bathroom;

    public static BathroomDAO getInstance() {
        return instance;
    }
    
    /**
     * Returns a list of all bathrooms.
     * @param quotationID
     * @return 
     * @throws java.lang.Exception
     */
    public List<Bathroom> getListBathroom(int quotationID) throws Exception {
        List<Bathroom> listBathroom = new ArrayList<>();
        String sql;
        if(quotationID==-1)
            sql = Keys.BATHROOM_LIST_QUERY;
        else
            sql = DbHandler.getSqlScript(Keys.BATHROOM_BY_ID_SQL);
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if(quotationID!=-1)
                preparedStatement.setInt(1, quotationID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Bathroom bath = new Bathroom();
                    bath.setId(resultSet.getInt("id"));
                    bath.setDescription(resultSet.getString("description"));
                    bath.setSeries(resultSet.getString("series"));
                    bath.setAmount(resultSet.getDouble("amount"));
                }
            }
        }
        return listBathroom;
    }
    
    /**
     * Saves a list of bathroom.
     * @param listBathroom
     * @throws java.lang.Exception
     */
    public void save(List<Bathroom> listBathroom) throws Exception {
        for(Bathroom bath:listBathroom){
            save(bath);
        }
    }

    /**
     * Saves a single bathroom.
     * @param bathroom
     * @return 
     * @throws java.lang.Exception
     */
    public int save(Bathroom bathroom) throws Exception {
        final String sql = DbHandler.getSqlScript(Keys.INSERT_BATHROOM_SQL);
        int bathroomId;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, bathroom.getDescription());
            preparedStatement.setString(2, bathroom.getSeries());
            preparedStatement.setDouble(3, bathroom.getAmount());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            bathroomId = resultSet.getInt(1);
        }
        connection.commit();
        System.out.println("save bath "+bathroomId);
        return bathroomId;
    }
    
    /**
     * Deletes all bathrooms and their items belonging to a single quotation.
     * @param quotationId
     * @throws java.lang.Exception
     */
    public void delete(int quotationId) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(Keys.DELETE_QUATATION_FROM_BATH_QUERY);
        preparedStatement.setInt(1, quotationId);
        preparedStatement.execute();

        preparedStatement = connection.prepareStatement(Keys.DELETE_QUATATION_FROM_ITEM_QUERY);
        preparedStatement.setInt(1, quotationId);
        preparedStatement.execute();

        preparedStatement.close();
        connection.commit();
    }

    /**
     * Deletes a sin
     * @param quotationId
     * @param quotationIdgle bathroom and all it's items.
     * @param bathroomId
     * @throws java.lang.Exception
     */
    public void delete(int quotationId, int bathroomId) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(Keys.DELETE_BATHROOM_QUERY);
        preparedStatement.setInt(1, bathroomId);
        preparedStatement.execute();
        
        preparedStatement = connection.prepareStatement(Keys.DELETE_QUATATION_FROM_ITEM_QUERY);
        preparedStatement.setInt(1, quotationId);
        preparedStatement.setInt(2, bathroomId);
        preparedStatement.execute();

        preparedStatement.close();
        connection.commit();
    }
    
    /**
     * Returns a currently active bathroom.
     * Bathroom becomes active when editing is started on it's items.
     * @return 
     */
    public Bathroom getBathroom() {
        return bathroom;
    }

    /**
     * Sets a currently active bathroom. 
     * Bathroom becomes active when editing is started on it's items.
     * @param bathroom
     */
    public void setBathroom(Bathroom bathroom) {
        this.bathroom = bathroom;
    }
}
