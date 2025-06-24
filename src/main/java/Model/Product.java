package Model;

public class Product {
    private int productId;
    private String productName;
    private double price;
    private String image;
    private String description;
    private String type;

    // Constructors
    public Product() {}

    public Product(int productId, String productName, double price, String image, String description, String type) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.image = image;
        this.description = description;
        this.type = type;
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }


}
