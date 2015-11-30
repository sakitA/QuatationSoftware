package quotationsoftware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import quotationsoftware.model.Bathroom;
import quotationsoftware.model.Quotation;
import quotationsoftware.util.DbHandler;
import quotationsoftware.util.Keys;

/**
 * Singleton data access class which handles quotation database operations.
 */
public class QuotationDAO {

    private static final QuotationDAO instance = new QuotationDAO();

    private Quotation quotation;
    private Connection connection;

    private QuotationDAO() {
        try {
            connection = DbHandler.getConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static QuotationDAO getInstance() {
        return instance;
    }

    /**
     * Returns an active quotation.
     *
     * @return
     */
    public Quotation getQuotation() {
        return quotation;
    }

    /**
     * Sets an active quotation.
     *
     * @param quotation
     */
    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    /**
     * Returns a list of all quotations.
     *
     * @return
     * @throws java.lang.Exception
     */
    public List<Quotation> getListQuotation() throws Exception {
        List<Quotation> listQuotation = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(Keys.QUATATION_LIST_QUERY);
                ResultSet resultSet = preparedStatement.executeQuery();) {
            while (resultSet.next()) {
                Quotation quota = new Quotation();
                quota.setId(resultSet.getInt("id"));
                quota.setCustomerName(resultSet.getString("customer_name"));
                quota.setCustomerAddress(resultSet.getString("customer_address"));
                quota.setContactNumber(resultSet.getString("contact_number"));
                quota.setEmailId(resultSet.getString("email_id"));
                quota.setEnquiryReferenceNo(resultSet.getString("enquiry_reference_no"));
                quota.setEnquiryDate(resultSet.getDate("enquiry_date"));
                quota.setReferenceRemark(resultSet.getString("reference_remark"));
                quota.setQuoteFormat(resultSet.getString("quote_format"));
                quota.setHeadings(resultSet.getString("headings"));
                quota.setVat(resultSet.getString("vat"));
                quota.setDisplay(resultSet.getString("display"));
                quota.setQuotationDate(resultSet.getDate("quotation_date"));
                quota.setDate(resultSet.getDate("quotation_date"));
                quota.setBathroomId(resultSet.getInt("bathroom_id"));
                listQuotation.add(quota);
            }
        }
        return listQuotation;
    }

    /**
     * Saves the currently active quotation.
     *
     * @return
     * @throws java.lang.Exception
     */
    public int save() throws Exception {
        int quotationId;
        final String sql = DbHandler.getSqlScript(Keys.INSERT_QUATATION_SQL);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setString(1, quotation.getCustomerName());
            preparedStatement.setString(2, quotation.getCustomerAddress());
            preparedStatement.setString(3, quotation.getContactNumber());
            preparedStatement.setString(4, quotation.getEmailId());
            preparedStatement.setString(5, quotation.getEnquiryReferenceNo());
            preparedStatement.setDate(6, quotation.getDate());
            preparedStatement.setString(7, quotation.getReferenceRemark());
            preparedStatement.setString(8, quotation.getQuoteFormat());
            preparedStatement.setString(9, quotation.getHeadings());
            preparedStatement.setString(10, quotation.getVat());
            preparedStatement.setString(11, quotation.getDisplay());
            preparedStatement.setDate(12, quotation.getQuotationDate());
            if (quotation.getBathroomId() != null) {
                preparedStatement.setInt(13, quotation.getBathroomId());
            } else {
                preparedStatement.setNull(13, Types.INTEGER);
            }
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                quotationId = resultSet.getInt(1);
                connection.commit();
            }
        }
        return quotationId;
    }

    /**
     * Deletes a single quotation. Deletes all it's bathrooms and items.
     *
     * @param quotation
     * @throws java.lang.Exception
     */
    public void delete(Quotation quotation) throws Exception {
        PreparedStatement preparedStatement;
        
        //delete quatation info from all related table;
        preparedStatement = connection.prepareStatement(Keys.DELETE_QUATATION_QUERY);
        preparedStatement.setInt(1, quotation.getId());
        preparedStatement.execute();

        preparedStatement = connection.prepareStatement(Keys.DELETE_QUATATION_FROM_BATH_QUERY);
        preparedStatement.setInt(1, quotation.getId());
        preparedStatement.execute();

        preparedStatement = connection.prepareStatement(Keys.DELETE_QUATATION_FROM_ITEM_QUERY);
        preparedStatement.setInt(1, quotation.getId());
        preparedStatement.execute();

        preparedStatement.close();
        connection.commit();
    }

    /**
     * Copies the currently active quotation and all it's items/bathrooms to a
     * new set of database rows.
     *
     * @throws java.lang.Exception
     */
    public void copy() throws Exception {
        int newQuoteId = save();
        int oldQuoteId = quotation.getId();
        System.out.println("copying " + oldQuoteId + ", new " + newQuoteId);

        ItemDAO itemDAO = ItemDAO.getInstance();
        BathroomDAO bathroomDAO = BathroomDAO.getInstance();

        if (quotation.getQuoteFormat().equals("Item Wise Quote")) {
            itemDAO.save(itemDAO.getListItem(oldQuoteId, -1), newQuoteId, -1);
        } else {
            for (Bathroom bathroom : bathroomDAO.getListBathroom(oldQuoteId)) {
                int newBathroomId = bathroomDAO.save(bathroom);
                itemDAO.save(itemDAO.getListItem(oldQuoteId, bathroom.getId()), newQuoteId, newBathroomId);
            }
        }
    }
}
