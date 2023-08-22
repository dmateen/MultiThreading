package com.i2c.mcp.batch.datafetchservice.businesslogic;

import com.i2c.mcp.batch.datafetchservice.DataFetcherService;

public class MainController {

        public static void main(String[] args) {
                DataFetcherService dataFetcherService= new DataFetcherService();
                dataFetcherService.start();
        }

}
