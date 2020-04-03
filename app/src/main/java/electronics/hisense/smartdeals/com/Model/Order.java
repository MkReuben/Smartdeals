package electronics.hisense.smartdeals.com.Model;

public class Order {
    private String pid,name,price,discount;

    public Order() {
    }

    public Order(String pid, String name, String price, String discount) {
        this.pid = pid;
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setSid(String pid) {
        this.pid = pid;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
