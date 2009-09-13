/*
 * TestDataGenerator.cpp
 *
 *  Created on: Sep 13, 2009
 *      Author: esol
 */

#include "TestDataGenerator.h"

#include <cstdlib>
#include <iostream>

TestDataGenerator::TestDataGenerator()
{
  srand( time(0) );
}

TestDataGenerator::~TestDataGenerator()
{
}

std::string
getRandomString(int iLength)
{

  std::string strReturn;
  for (int i = 0; i < iLength; ++i)
    {
      int iNumber;
      iNumber = rand() % 122;
      if (48 > iNumber)
        iNumber += 48;
      if ((57 < iNumber) && (65 > iNumber))
        iNumber += 7;
      if ((90 < iNumber) && (97 > iNumber))
        iNumber += 6;
      strReturn += (char) iNumber;
    }
  return strReturn;
}

DataTypes::Person
TestDataGenerator::createRandomPerson()
{
  DataTypes::Person person;

  person.id = 0;
  person.displayedName = getRandomString(rand() % 40);
  person.firstName = getRandomString(rand() % 40);
  person.lastName = getRandomString(rand() % 40);

//  std::cout << person.toString();

  return person;
}
