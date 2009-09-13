/*
 * SQLiteTest.h
 *
 *  Created on: Sep 7, 2009
 *      Author: chrislee
 */

#ifndef SQLITETEST_H_
#define SQLITETEST_H_

#include "sqlite/sqlite3.h"
#include <time.h>

class SQLiteTest
{
private:
  char* dbfile;
  sqlite3* db;
public:



  SQLiteTest(char* dbfile);

  int openDB();
  int testQuery(char* query, int numberOfCycles);
  int execute(char* query);
  int prepareStatement(char* query, sqlite3_stmt **ppStatement);
  void closeDB();
  void analyzeTimestamps(clock_t startTime, clock_t endTime, char* query, int numberOfCycles);

  virtual
  ~SQLiteTest();
};

#endif /* SQLITETEST_H_ */
