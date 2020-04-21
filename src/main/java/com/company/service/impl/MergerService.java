package com.company.service.impl;

import com.company.Application;
import com.company.model.MyLine;
import com.company.service.ILineService;
import com.company.service.IMergerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

@Slf4j
@Service
public class MergerService implements IMergerService {

    @Autowired
    private ILineService lineService;

    @Override
    public String mergeTwoFiles(String accumulativeFile, String nextFile, int tempFileCounter, int columnNumber) {
        //TODO define temp file
        String tempFile = Application.TEMP_FILE_SUFF + tempFileCounter +  ".csv";
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile, true));

            BufferedReader accumulativeFileReader = new BufferedReader(new FileReader(accumulativeFile));
            BufferedReader nextFileReader = new BufferedReader(new FileReader(nextFile));

            String firstLine=null;
            String secondLine=null;

            while(true){
                String line;
                if(firstLine==null) {
                    firstLine = accumulativeFileReader.readLine();
                    if(firstLine==null){
                        break;
                    }
                }
                if(secondLine==null) {
                    secondLine = nextFileReader.readLine();
                    if(secondLine==null){
                        break;
                    }
                }

                MyLine myFirstLine =  lineService.parseLine(firstLine, columnNumber);
                if(myFirstLine==null){
                    firstLine = null;
                    continue;
                }
                MyLine mySecondLine = lineService.parseLine(secondLine, columnNumber);
                if(mySecondLine==null){
                    secondLine = null;
                    continue;
                }

                if(myFirstLine.compareTo(mySecondLine) < 0) {
                    line = firstLine;
                    firstLine = null;
                }else{
                    line = secondLine;
                    secondLine = null;
                }
                writer.write(line);
                writer.write("\n");
            }

            if(firstLine!=null){
                writer.write(firstLine);
                writer.write("\n");
            }
            if(secondLine!=null){
                writer.write(secondLine);
                writer.write("\n");
            }

            String line;
            if(firstLine!=null) {
                while ((line = accumulativeFileReader.readLine()) != null) {
                    writer.write(line);
                    writer.write("\n");
                }
            }

            if(secondLine!=null) {
                while ((line = nextFileReader.readLine()) != null) {
                    writer.write(line);
                    writer.write("\n");
                }
            }

            accumulativeFileReader.close();
            nextFileReader.close();
            writer.close();
        }catch (Exception e){
            log.error("{}",e);
        }
        return tempFile;
    }
}
