package Models;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Courses
{

    private int id_C,numberW;
    private String descriptionC,type,nameC;
    private float priceC;
    private String image_path;
    private int count;
    private int Userid;
    public Courses( int numberW, String descriptionC, String type, String nameC, float priceC, String image_path, int Userid)
    {
        
        this.numberW = numberW;
        this.descriptionC = descriptionC;
        this.type = type;
        this.nameC = nameC;
        this.priceC = priceC;
        this.image_path = image_path;
        this.Userid = Userid;
    }

    public Courses(String type, int count) {
        this.type = type;
        this.count = count;
    }

    public Courses() {
    }
    public Courses(String nameC,String descriptionC,float priceC, String type, String image_path,int Userid) {
        this.Userid=Userid;
        this.nameC = nameC;
        this.descriptionC = descriptionC;
        this.priceC = priceC;
        this.type = type;
        this.image_path = image_path;
    }
    public Courses(int id_C, String nameC,String descriptionC,float priceC, String type,int numberW) {
        this.id_C = id_C;
        this.nameC = nameC;
        this.descriptionC = descriptionC;
        this.priceC = priceC;
        this.type = type;
        this.numberW = numberW;
    }
    public Courses(String nameC,String descriptionC,float priceC, String type) {
        this.id_C = id_C;
        this.nameC = nameC;
        this.descriptionC = descriptionC;
        this.priceC = priceC;
        this.type = type;
    }


    public Courses(String nameC,String descriptionC,float priceC, String type,int numberW) {
        this.nameC = nameC;
        this.descriptionC = descriptionC;
        this.priceC = priceC;
        this.type = type;
        this.numberW = numberW;

    }
    public Courses(ResultSet resultSet) throws SQLException {
        this.id_C = resultSet.getInt("id_C");
        this.nameC = resultSet.getString("nameC");
        this.descriptionC = resultSet.getString("descriptionC");
        this.priceC = resultSet.getFloat("priceC");
    }

    public int getUserid() {
        return Userid;
    }

    public void setUserid(int Userid) {
        this.Userid = Userid;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getId_C() {
        return id_C;
    }

    public void setId_C(int id_C) {
        this.id_C = id_C;
    }

    public int getNumberW() {
        return numberW;
    }

    public void setNumberW(int numberW) {
        this.numberW = numberW;
    }

    public String getDescriptionC() {
        return descriptionC;
    }

    public void setDescriptionC(String descriptionC) {
        this.descriptionC = descriptionC;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNameC() {
        return nameC;
    }

    public void setNameC(String nameC) {
        this.nameC = nameC;
    }

    public float getPriceC() {
        return priceC;
    }

    public void setPriceC(float priceC) {
        this.priceC = priceC;
    }

    @Override
    public String toString() {
        return "Courses{" +
                "id_C=" + id_C +
                ", numberW=" + numberW +
                ", descriptionC='" + descriptionC + '\'' +
                ", type='" + type + '\'' +
                ", nameC='" + nameC + '\'' +
                ", priceC=" + priceC +
                '}';
    }

    public int getCount()
    {
        return count;
    }
}
