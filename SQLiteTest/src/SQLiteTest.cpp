/*
 * SQLiteTest.cpp
 *
 *  Created on: Sep 7, 2009
 *      Author: chrislee
 */

#include "SQLiteTest.h"
#include <stdio.h>
#include <iostream>

SQLiteTest::SQLiteTest(char* dbfile)
{
  this->dbfile = dbfile;
}

SQLiteTest::~SQLiteTest()
{
}

//static int
//callback(void *NotUsed, int argc, char **argv, char **azColName)
//{
//  int i;
//  for (i = 0; i < argc; i++)
//    {
//      printf("%s = %s\n", azColName[i], argv[i] ? argv[i] : "NULL");
//    }
//  printf("\n");
//  return 0;
//}

static int
emptyCallback(void *NotUsed, int argc, char **argv, char **azColName)
{
  return 0;
}

int
SQLiteTest::openDB()
{
  int rc = 0;
  rc = sqlite3_open(this->dbfile, &db);
  if (rc)
    {
      fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(db));
      sqlite3_close(db);
    }
  return rc;
}

void
SQLiteTest::closeDB()
{
  sqlite3_close(db);
}

void
SQLiteTest::analyzeTimestamps(clock_t startTime, clock_t endTime, char* query,
    int numberOfCycles)
{
  float milliseconds = ((float) endTime - (float) startTime)
      / ((float) CLOCKS_PER_SEC / (float) 1000);
  std::cout << milliseconds << " milliseconds for " << numberOfCycles
      << " cycles of this query:" << std::endl;
  std::cout << std::endl;
  std::cout << query << std::endl;
  std::cout << std::endl;
  std::cout << "That makes " << milliseconds / (float) numberOfCycles
      << " milliseconds per cycle" << std::endl;
  std::cout << "-----------------------------------------------------------" << std::endl;
}

int
SQLiteTest::testQuery(char* query, int numberOfCycles)
{
  int rc = 0;
  char *zErrMsg = 0;

  clock_t startTime, endTime;
  startTime = clock();

  for (int i = 0; i < numberOfCycles; i++)
    {
      //rc = sqlite3_exec(db, query, callback, 0, &zErrMsg);
      rc = sqlite3_exec(db, query, emptyCallback, 0, &zErrMsg);
      if (rc != SQLITE_OK)
        {
          fprintf(stderr, "SQL error: %s\n", zErrMsg);
          sqlite3_free(zErrMsg);
          return rc;
        }
    }

  endTime = clock();
  analyzeTimestamps(startTime, endTime, query, numberOfCycles);

  return rc;
}

int
SQLiteTest::testPreparedStatement(char* query, int numberOfCycles)
{
  int rc = 0;
  char *zErrMsg = 0;
  sqlite3_stmt* statement = 0;

  rc = sqlite3_prepare_v2(db, query, strlen(query), &statement, 0);
  if (rc != SQLITE_OK)
    {
      fprintf(stderr, "SQL error: %s\n", zErrMsg);
      sqlite3_free(zErrMsg);
      return rc;
    }

  clock_t startTime, endTime;
  startTime = clock();

  for (int i = 0; i < numberOfCycles; i++)
    {
      rc = sqlite3_step(statement);
      if (rc != SQLITE_OK)
        {
          fprintf(stderr, "SQL error: %s\n", zErrMsg);
          sqlite3_free(zErrMsg);
          return rc;
        }
      sqlite3_reset(statement);
    }

  endTime = clock();
  analyzeTimestamps(startTime, endTime, query, numberOfCycles);

  return rc;
}
