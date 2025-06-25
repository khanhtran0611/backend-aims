package Project_ITSS.AddProduct.config;

public class SqlQueries {
    
    // Product table queries
    public static final String INSERT_PRODUCT = 
        "INSERT INTO product (title, price, weight, rush_order_supported, image_url, barcode, import_date, introduction, quantity, type) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING product_id";
    
    public static final String UPDATE_PRODUCT = 
        "UPDATE Product SET title = ?, price = ?, weight = ?, rush_order_supported = ?, " +
        "image_url = ?, barcode = ?, import_date = ?, introduction = ?, quantity = ? " +
        "WHERE product_id = ?";
    
    // Book table queries
    public static final String INSERT_BOOK = 
        "INSERT INTO Book (product_id, genre, page_count, publication_date, authors, publishers, cover_type) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    // CD table queries
    public static final String INSERT_CD = 
        "INSERT INTO CD (Product_id, Track_list, genre, record_label, artists, release_date) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    
    // DVD table queries
    public static final String INSERT_DVD = 
        "INSERT INTO DVD (Product_id, title, release_Date, DVD_type, genre, studio, director) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    // Logger table queries
    public static final String INSERT_LOGGER = 
        "INSERT INTO Logger (action_name, recorded_at, note) VALUES (?, CURRENT_DATE, ?)";
} 