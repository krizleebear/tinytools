/*
 * MyMain.c
 *
 *  Created on: Sep 6, 2009
 *      Author: chrislee
 */

#include <iostream>
#include <stdio.h>
#include <sys/param.h> //MAXPATHLEN
#include "SQLiteTest.h"
#include <time.h>
#include <string>

#include "TestDataGenerator.h"
#include "DataTypes.h"

void
log(const char* string)
{
  std::cout << string << std::endl;
}

static int
callback(void *NotUsed, int argc, char **argv, char **azColName)
{
  int i;
  for (i = 0; i < argc; i++)
    {
      printf("%s = %s\n", azColName[i], argv[i] ? argv[i] : "NULL");
    }
  printf("\n");
  return 0;
}

int
dbTest(char* dbFilename, char* sqlQuery)
{
  sqlite3 *db;
  char *zErrMsg = 0;
  int rc;

  rc = sqlite3_open(dbFilename, &db);
  if (rc)
    {
      fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(db));
      sqlite3_close(db);
      return 1;
    }
  rc = sqlite3_exec(db, sqlQuery, callback, 0, &zErrMsg);
  if (rc != SQLITE_OK)
    {
      fprintf(stderr, "SQL error: %s\n", zErrMsg);
      sqlite3_free(zErrMsg);
    }
  sqlite3_close(db);
  return 0;
}

void
testPreparedStatement(SQLiteTest* sqlite)
{
  int numberOfCycles = 10000;
  clock_t startTime, endTime;

  const char* insertPersonSQL =
      "insert into Person (displayedName, firstName, lastName) values (?, ?, ?);";

  startTime = clock();

  log("preparing statement");

  sqlite3_stmt* insertPersonStatement;
  sqlite->prepareStatement(insertPersonSQL, &insertPersonStatement);
  endTime = clock();

  sqlite->analyzeTimestamps(startTime, endTime, "preparing insertPerson", 1);

  startTime = clock();

  for (int i = 0; i < numberOfCycles; i++)
    {
      sqlite3_bind_text(insertPersonStatement, 1, "vorname nachname", -1,
          SQLITE_STATIC);
      sqlite3_bind_text(insertPersonStatement, 2, "vorname", -1, SQLITE_STATIC);
      sqlite3_bind_text(insertPersonStatement, 3, "nachname", -1, SQLITE_STATIC);

      sqlite3_step(insertPersonStatement);
      sqlite3_reset(insertPersonStatement);
    }
  endTime = clock();

  sqlite->analyzeTimestamps(startTime, endTime, insertPersonSQL, numberOfCycles);

  sqlite3_finalize(insertPersonStatement);
}

int
main()
{
  TestDataGenerator* gen = new TestDataGenerator();

  gen->createRandomPerson();

  //return 0;

  char currentPath[MAXPATHLEN];
  getcwd(currentPath, MAXPATHLEN);
  std::cout << "Current working dir: " << currentPath << std::endl;

  SQLiteTest* sqlite = new SQLiteTest("test.db");
  sqlite->openDB();

  sqlite->testQuery(
      "insert into Person (displayedName, firstName, lastName) values ('vorname nachname', 'vorname', 'nachname');",
      1000);

  testPreparedStatement(sqlite);

  sqlite->testQuery(
      "select Person.displayedName, Phone.displayedNumber from Person,Phone where Person.id = Phone.person_id;",
      1000);

  sqlite->closeDB();
  delete sqlite;

  return 0;
}

