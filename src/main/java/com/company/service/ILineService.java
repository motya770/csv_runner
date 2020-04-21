package com.company.service;

import com.company.model.MyLine;

import java.util.List;

public interface ILineService {

   MyLine parseLine(String line, int columnNumber);

   void sortLines(List<MyLine> lines);

}
