/*
 * MyMain.c
 *
 *  Created on: Sep 6, 2009
 *      Author: chrislee
 */

#include <iostream>
#include <stdio.h>
#include <sqlite3.h>
#include <sys/param.h> //MAXPATHLEN
#include "SQLiteTest.h"

void log(char* string)
{
	std::cout << string << std::endl;
}

void printWorkingDir()
{
	char currentPath[MAXPATHLEN];
	getcwd(currentPath, MAXPATHLEN);

	log(currentPath);
}

static int callback(void *NotUsed, int argc, char **argv, char **azColName)
{
	int i;
	for (i = 0; i < argc; i++)
	{
		printf("%s = %s\n", azColName[i], argv[i] ? argv[i] : "NULL");
	}
	printf("\n");
	return 0;
}

int dbTest(char* dbFilename, char* sqlQuery)
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

int main()
{
	log("!!!Hello World!!!"); // prints !!!Hello World!!!

	printWorkingDir();
	//dbTest("test.db", "select * from mytable");

	SQLiteTest* sqlite = new SQLiteTest("test.db");
	sqlite->openDB();
	sqlite->testQuery("select * from mytable", 10000);
	sqlite->testQuery("select id,name from mytable", 10000);
	sqlite->testQuery("select id,name from mytable order by name", 10000);
	sqlite->closeDB();
	delete sqlite;

	return 0;
}

