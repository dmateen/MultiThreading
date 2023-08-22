package com.i2c.mcp.batch.datafetchservice.beans;

public class TransactionInfo {
    private int company_id;
    private String company_name;

    public TransactionInfo(int id, String name){
        company_id=id;
        company_name=name;
    }
}
