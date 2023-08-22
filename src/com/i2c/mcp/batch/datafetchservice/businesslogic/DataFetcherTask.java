package com.i2c.mcp.batch.datafetchservice.businesslogic;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;


public class DataFetcherTask implements Runnable {
    final String TABLE_NAME_KEY="TABLE_NAME";
    final String BATCH_SIZE_KEY="BATCH_SIZE";
    final String MIN_INDEX_KEY="MIN_INDEX";
    final String MAX_INDEX_KEY="MAX_INDEX";
    final String NO_OF_THREADS="THREAD_COUNT";
    final String POOL_SIZE="POOL_SIZE";


    String tableName;
    int batchSize;
    int minIndex;
    int maxIndex;
    int maxThreads;
    int poolSize;

    @Override
    public void run() {
        //Read Config
        readConfigFile();

        //Write Executor Service and Create Threads
        ExecutorService pool= Executors.newFixedThreadPool(poolSize);
        List<Future<Boolean>> threadResponses= new ArrayList<>();

        try {
            int start = minIndex;
            for (int threadCreated = 0; threadCreated < maxThreads; threadCreated++) {

                threadResponses.add(pool.submit(new SingleRangeDataFetcherTask("Thread " + threadCreated, start, start + batchSize)));
                start = start + batchSize;
            }

            for(Future<Boolean> threadResponse:threadResponses){
                threadResponse.get();
            }


        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            pool.shutdown();
        }





        //Pass Threads the batch min and max depending on the batch size

    }



    void readConfigFile(){
        Properties properties=new Properties();
        String fileName="src/resources/app.config";

        try {
            InputStream inputStream = Files.newInputStream(Paths.get(fileName));
            properties.load(inputStream);

            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        tableName= properties.getProperty(TABLE_NAME_KEY);
        batchSize= Integer.valueOf(properties.getProperty(BATCH_SIZE_KEY));
        minIndex=Integer.valueOf(properties.getProperty(MIN_INDEX_KEY)) ;
        maxIndex=Integer.valueOf(properties.getProperty(MAX_INDEX_KEY));
        maxThreads =Integer.valueOf(properties.getProperty(NO_OF_THREADS));
        poolSize=Integer.valueOf(properties.getProperty(POOL_SIZE));

    }
}
