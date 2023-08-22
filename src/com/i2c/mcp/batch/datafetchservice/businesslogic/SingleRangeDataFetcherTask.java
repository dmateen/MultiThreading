package com.i2c.mcp.batch.datafetchservice.businesslogic;

import com.i2c.mcp.batch.datafetchservice.dao.DataFetcherDao;

import java.util.concurrent.Callable;

public class SingleRangeDataFetcherTask implements Callable<Boolean> {

    String threadName;
    int dbStartingValue;
    int dbEndingValue;
    SingleRangeDataFetcherTask(String threadName,int start,int end){
        this.threadName=threadName;
        this.dbStartingValue=start;
        this.dbEndingValue=end;
    }


    @Override
    public Boolean call() throws Exception {
        System.out.println(threadName+" | Start:"+dbStartingValue+" | End:"+dbEndingValue);
        DataFetcherDao dataFetcherDao=new DataFetcherDao();
        dataFetcherDao.getData(dbStartingValue,dbEndingValue);

        return true;
    }
}
