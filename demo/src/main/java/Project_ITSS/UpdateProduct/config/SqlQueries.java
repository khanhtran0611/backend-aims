package Project_ITSS.UpdateProduct.config;

public class SqlQueries {
    
    // Product table queries
    public static final String UPDATE_PRODUCT = 
        "UPDATE Product SET title = ?, price = ?, weight = ?, rush_order_supported = ?, " +
        "image_url = ?, barcode = ?, import_date = ?, introduction = ?, quantity = ? " +
        "WHERE product_id = ?";
    
    // Book table queries
    public static final String UPDATE_BOOK = 
        "UPDATE Book SET genre = ?, page_count = ?, publication_date = ?, authors = ?, " +
        "publishers = ?, cover_type = ? WHERE product_id = ?";
    
    // CD table queries
    public static final String UPDATE_CD = 
        "UPDATE CD SET Track_list = ?, genre = ?, record_label = ?, artists = ?, " +
        "release_date = ? WHERE Product_id = ?";
    
    // DVD table queries
    public static final String UPDATE_DVD = 
        "UPDATE DVD SET title = ?, release_Date = ?, DVD_type = ?, genre = ?, " +
        "studio = ?, director = ? WHERE Product_id = ?";
    
    // Logger table queries
    public static final String INSERT_LOGGER = 
        "INSERT INTO Logger (action_name, recorded_at, note) VALUES (?, CURRENT_DATE, ?)";
} 