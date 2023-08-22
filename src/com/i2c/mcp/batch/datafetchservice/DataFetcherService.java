package com.i2c.mcp.batch.datafetchservice;

import com.i2c.mcp.batch.datafetchservice.businesslogic.DataFetcherTask;

public class DataFetcherService {

    public void start() {
        DataFetcherTask dataFetcherTask=new DataFetcherTask();
        new Thread(dataFetcherTask).start();
    }
}

