/*
 * SQLiteTest.h
 *
 *  Created on: Sep 7, 2009
 *      Author: chrislee
 */

#ifndef SQLITETEST_H_
#define SQLITETEST_H_

#include "sqlite3.h"

class SQLiteTest
{
private:
  char* dbfile;
  sqlite3* db;
public:



  SQLiteTest(char* dbfile);

  int openDB();
  int testQuery(char* query, int numberOfCycles);
  void closeDB();

  virtual
  ~SQLiteTest();
};

#endif /* SQLITETEST_H_ */
