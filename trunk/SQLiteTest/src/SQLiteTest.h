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

  const char* dbfile;
  sqlite3* db;

public:

  SQLiteTest(const char* dbfile);

  int openDB();
  int testQuery(const char* query, int numberOfCycles);
  int execute(const char* query);
  int prepareStatement(const char* query, sqlite3_stmt **ppStatement);
  void closeDB();
  void analyzeTimestamps(clock_t startTime, clock_t endTime, const char* query, int numberOfCycles);

  void log(const char* message);

  virtual
  ~SQLiteTest();
};

#endif /* SQLITETEST_H_ */
