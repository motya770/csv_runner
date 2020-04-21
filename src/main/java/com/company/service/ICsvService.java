package com.company.service;

import com.company.model.MyLine;

import java.io.IOException;
import java.util.List;

public interface ICsvService {

    void sortMainCSVFile(String mainFile, String pathToSmallFiles, int columnNumber, int maxNumberOfRowsInMemory);

    void sortAndSave(String smallFilesFolder, int fileCounter, List<MyLine> buffer)throws IOException;

    String mergeFiles(String pathToSmallFiles, int fileCounter, int columnNumber);
}
