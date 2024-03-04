package Models;

public class category {

   public int id_category;
   public String name;
    public String date;



    public int Userid;
    public category(){}
    public category(int id_category, String name, String date) {
        this.id_category = id_category;
        this.name = name;
        this.date = date;
    }
    public category(String name, String date,int Userid) {
        this.name = name;
        this.date = date;
        this.Userid=Userid;
    }
    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
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
    public int getUserid() {
        return Userid;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }
    @Override
    public String toString() {
        return name;
    }

}
