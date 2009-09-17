/*
 * SQLiteTest.cpp
 *
 *  Created on: Sep 7, 2009
 *      Author: chrislee
 */

#include "SQLiteTest.h"
#include <stdio.h>
#include <iostream>
#include <stdlib.h>
#include <string.h>

SQLiteTest::SQLiteTest(const char* dbfile)
{
  this->dbfile = dbfile;
  this->db = 0;
}

SQLiteTest::~SQLiteTest()
{
}

void
SQLiteTest::log(const char* message)
{
  std::cout << message << std::endl;
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
  std::cout << "opening db " << this->dbfile << std::endl;

  int rc = 0;
  rc = sqlite3_open(this->dbfile, &db);
  if (rc)
    {
      fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(db));
      sqlite3_close(db);
    }

  log("opened db...");
  return rc;
}

void
SQLiteTest::closeDB()
{
  log("closing db...");

  if (db)
    {
      sqlite3_close(db);
      log("closed db...");
    }
}

void
SQLiteTest::analyzeTimestamps(clock_t startTime, clock_t endTime, const char* query,
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
  std::cout << "-----------------------------------------------------------"
      << std::endl;
}

int
SQLiteTest::execute(const char* query)
{
  int rc = 0;
  char *zErrMsg = 0;
  rc = sqlite3_exec(db, query, emptyCallback, 0, &zErrMsg);
  if (rc != SQLITE_OK)
    {
      fprintf(stderr, "SQL error: %s\n", zErrMsg);
      sqlite3_free(zErrMsg);
      return rc;
    }
  return rc;
}

int
SQLiteTest::testQuery(const char* query, int numberOfCycles)
{
  int rc = 0;

  clock_t startTime, endTime;
  startTime = clock();

  for (int i = 0; i < numberOfCycles; i++)
    {
      execute(query);
    }

  endTime = clock();
  analyzeTimestamps(startTime, endTime, query, numberOfCycles);

  return rc;
}

int
SQLiteTest::prepareStatement(const char* query, sqlite3_stmt **ppStatement)
{
  int rc = 0;
  char *zErrMsg = 0;

  rc = sqlite3_prepare(db, query, strlen(query), ppStatement, NULL);

  if (rc != SQLITE_OK)
    {
      fprintf(stderr, "SQL error: %s\n", zErrMsg);
      sqlite3_free(zErrMsg);
      return rc;
    }
  return rc;
}

long
SQLiteTest::getLastInsertedID()
{
  return sqlite3_last_insert_rowid(db);
}
