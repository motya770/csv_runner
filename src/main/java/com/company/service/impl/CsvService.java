package com.company.service.impl;

import com.company.model.MyLine;
import com.company.service.ICsvService;
import com.company.service.ILineService;
import com.company.service.IMergerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CsvService implements ICsvService {

    @Autowired
    private ILineService lineService;

    @Autowired
    private IMergerService mergerService;

    ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public void sortMainCSVFile(String mainFile, String pathToSmallFiles,
                                int columnNumber, int maxNumberOfRowsInMemory) {
        int fileCounter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(mainFile))) {
            String line;
            int counter = 0;
            List<MyLine> buffer = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                if(counter==0 && fileCounter ==0){
                    //skipping main csv header
                    counter++;
                    continue;
                }

                MyLine myLine = lineService.parseLine(line, columnNumber);
                if(myLine==null){
                    continue;
                }
                buffer.add(myLine);

                //TODO fix to parameter
                if(counter==maxNumberOfRowsInMemory){

                   final int fileCounterConst = fileCounter;
                   final List<MyLine> bufferConst = buffer;

                   Runnable runnable = ()-> {
                       try {
                           sortAndSave(pathToSmallFiles, fileCounterConst, bufferConst);
                       }catch (Exception e){
                           log.error("{}", e);
                       }
                   };
                   executorService.execute(runnable);

                   counter=0;
                   fileCounter++;
                   buffer = new ArrayList<>();
                }
                counter++;
            }

            log.info("writing finished");

            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);

            String mergedFile = mergeFiles(pathToSmallFiles, fileCounter, columnNumber);

            log.info("Finished to merge. Result in: {}", mergedFile);

        }catch (Exception e){
            log.error("{}", e);
        }
    }

    @Override
    public String mergeFiles(String pathToSmallFiles, int fileCounter, int columnNumber) {
        String mergedFile =  pathToSmallFiles + 0 + ".csv";
        for(int i=1; i < fileCounter; i++){
            //TODO move to utils
             String secondFilePath =  pathToSmallFiles + i + ".csv";
             log.info("staring to merge: {} {}", mergedFile, secondFilePath);
             mergedFile = mergerService.mergeTwoFiles(mergedFile, secondFilePath, i, columnNumber);
        }
        return mergedFile;
    }

    @Override
    public void sortAndSave(String smallFilesFolder, int fileCounter, List<MyLine> buffer) throws IOException {
        Collections.sort(buffer);

        for(MyLine myLine: buffer){
            System.out.println(myLine.getValue());
        }
        System.out.println();

        String fullSmallPathName = smallFilesFolder + fileCounter + ".csv";
        log.info("writing to file {}", fullSmallPathName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fullSmallPathName));
        buffer.forEach((singeMyLine)->{
            try {
                writer.write(singeMyLine.getLine());
                writer.write("\n");
            }catch(Exception e){
                log.error("{}", e);
            }
        });
        writer.close();
    }
}
