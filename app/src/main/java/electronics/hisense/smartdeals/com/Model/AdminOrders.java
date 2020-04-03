package electronics.hisense.smartdeals.com.Model;

public class AdminOrders
{
    private   String name,date,time,location,phone;

    public AdminOrders()
    {

    }

    public AdminOrders(String name, String date, String time, String location, String phone) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
