package Models;

public class Bid {
    private int idbid,idAuctin,Userid,bidAmount;

    public Bid(int idbid, int idAuctin, int userid, int bidAmount) {
        this.idbid = idbid;
        this.idAuctin = idAuctin;
        Userid = userid;
        this.bidAmount = bidAmount;
    }

    public Bid(int idAuctin, int userid, int bidAmount) {
        this.idAuctin = idAuctin;
        Userid = userid;
        this.bidAmount = bidAmount;
    }

    public Bid(int bidAmount) {
        this.bidAmount = bidAmount;
    }



    public int getIdbid() {
        return idbid;
    }

    public void setIdbid(int idbid) {
        this.idbid = idbid;
    }

    public int getIdAuctin() {
        return idAuctin;
    }

    public void setIdAuctin(int idAuctin) {
        this.idAuctin = idAuctin;
    }

    public int getUserid() {
        return Userid;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "idbid=" + idbid +
                ", idAuctin=" + idAuctin +
                ", Userid=" + Userid +
                ", bidAmount=" + bidAmount +
                '}';
    }
}
