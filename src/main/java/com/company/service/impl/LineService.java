package com.company.service.impl;

import com.company.model.MyLine;
import com.company.service.ILineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class LineService implements ILineService {

    @Override
    public MyLine parseLine(String line, int columnNumber) {

        if(StringUtils.isEmpty(line)){
            log.warn("Line is empty");
            return null;
        }

        String[] columns = line.split(",");
        if(columns.length==0 && columns.length < columnNumber){
            log.warn("Failed to parse columns");
            return null;
        }

        String valueToParse = columns[columnNumber];
        if(StringUtils.isEmpty(valueToParse)){
            log.warn("Value inside column is empty");
            return null;
        }

        double value = Double.parseDouble(valueToParse);

        MyLine myLine = new MyLine();
        myLine.setLine(line);
        myLine.setValue(value);
        return myLine;
    }

    @Override
    public void sortLines(List<MyLine> lines) {

    }
}
