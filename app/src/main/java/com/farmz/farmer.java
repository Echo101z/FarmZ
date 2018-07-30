package com.farmz;

/**
 * Created by Admin on 23-Sep-17.
 */

public class farmer {
    String farmername;
    String farmerid;
    String phno;
    String query;
    String address;

    public farmer(){

    }

    public farmer(String farmername, String farmerid, String phno, String query,String address) {
        this.farmername = farmername;
        this.farmerid = farmerid;
        this.phno = phno;
        this.query=query;
        this.address = address;
    }

    public String getFarmername() {
        return farmername;
    }

    public String getFarmerid() {
        return farmerid;
    }

    public String getPhno() {
        return phno;
    }
    public String getQuery() {
        return query;
    }

    public String getAddress() {
        return address;
    }
}
