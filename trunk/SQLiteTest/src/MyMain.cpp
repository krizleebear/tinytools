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

void
log(char* string)
{
  std::cout << string << std::endl;
}

void
printWorkingDir()
{
  char currentPath[MAXPATHLEN];
  getcwd(currentPath, MAXPATHLEN);

  log(currentPath);
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

void testPreparedStatement(SQLiteTest* sqlite)
{
  int numberOfCycles = 10;
  char* insertSQL =
            "insert into Person (displayedName, firstName, lastName) values (?, ?, ?);";
    sqlite3_stmt* insertStatement;
    sqlite->prepareStatement(insertSQL, &insertStatement);

    clock_t startTime, endTime;
    startTime = clock();

    for (int i = 0; i < numberOfCycles; i++)
      {
        sqlite3_bind_text(insertStatement, 1, "vorname nachname", -1, SQLITE_STATIC);
        sqlite3_bind_text(insertStatement, 2, "vorname", -1, SQLITE_STATIC);
        sqlite3_bind_text(insertStatement, 3, "nachname", -1, SQLITE_STATIC);

        sqlite3_step(insertStatement);

        sqlite3_reset(insertStatement);
      }

    endTime = clock();
    sqlite->analyzeTimestamps(startTime, endTime, insertSQL, numberOfCycles);
}

int
main()
{
  log("!!!Hello World!!!"); // prints !!!Hello World!!!

  printWorkingDir();

  SQLiteTest* sqlite = new SQLiteTest("test.db");
  sqlite->openDB();

  testPreparedStatement(sqlite);

  sqlite->testQuery(
      "select Person.displayedName, Phone.displayedNumber from Person,Phone where Person.id = Phone.person_id;",
      1000);

  sqlite->closeDB();
  delete sqlite;

  return 0;
}



