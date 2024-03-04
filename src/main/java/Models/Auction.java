package Models;

public class Auction {
    private int id,price,Userid;


    private String time,date,Auctionname,imgpath,description;

    public Auction(int price, int userid, String time, String date, String auctionname, String imgpath, String description) {
        this.price = price;
        Userid = userid;
        this.time = time;
        this.date = date;
        Auctionname = auctionname;
        this.imgpath = imgpath;
        this.description = description;
    }

    public Auction(int id, int price, int userid, String time, String date, String auctionname, String imgpath, String description) {
        this.id = id;
        this.price = price;
        Userid = userid;
        this.time = time;
        this.date = date;
        Auctionname = auctionname;
        this.imgpath = imgpath;
        this.description = description;
    }


    public Auction(int id, int price, int userid, String time, String date, String Auctionname) {
        this.id = id;
        Userid = userid;
        this.price = price;
        this.time = time;
        this.date = date;
        this.Auctionname = Auctionname;

    }

    public Auction() {
    }

    public Auction(int price, int userid, String time, String date, String Auctionname) {
        Userid = userid;

        this.price = price;
        this.time = time;
        this.date = date;
        this.Auctionname = Auctionname;
    }

    public Auction(int id, int newPrice, String newName, int userid) {
        Userid = userid;
    }

    public Auction(int priceInt, float bitcoinFloat, String auctionTime, String string, String auctionName) {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public  String getAuctionname() {
        return Auctionname;
    }

    public void setAuctionname(String Auctionname) {this.Auctionname=Auctionname;
    }
    public int getUserid() {
        return Userid;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }
    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                "imgpath="+imgpath+
                "description="+description+
                ",Userid"+Userid+
                ", time=" + time +
                ", price=" + price +
                ", date=" + date +
                ", Auctionname='" + Auctionname + '\'' +
                '}';
    }
}
