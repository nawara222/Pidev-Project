package Models;

public class Ticket {

    private int idT;
    private String qrCodeT;
    private int idE; // Reference to the associated Event

    private int idU;


    public Ticket() {
    }

    public Ticket(int idT, String qrCodeT, int idE, int idU) {
        this.idT = idT;
        this.qrCodeT = qrCodeT;
        this.idE = idE;
        this.idU = idU;
    }

    public Ticket(String qrCodeT, int idE, int idU) {
        this.qrCodeT = qrCodeT;
        this.idE = idE;
        this.idU = idU;
    }

    public int getIdT() {
        return idT;
    }

    public void setIdT(int idT) {
        this.idT = idT;
    }

    public String getQrCodeT() {
        return qrCodeT;
    }

    public void setQrCodeT(String qrCodeT) {
        this.qrCodeT = qrCodeT;
    }

    public int getIdE() {
        return idE;
    }

    public void setIdE(int idE) {
        this.idE = idE;
    }

    public int getIdU() {
        return idU;
    }

    public void setIdU(int idU) {
        this.idU = idU;
    }
}