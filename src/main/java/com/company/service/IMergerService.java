package com.company.service;

public interface IMergerService {

    String mergeTwoFiles(String accumulativeFile, String nextFile, int tempFileCounter, int columnNumber);
}
