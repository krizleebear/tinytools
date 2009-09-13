/*
 * TestDataGenerator.h
 *
 *  Created on: Sep 13, 2009
 *      Author: esol
 */

#ifndef TESTDATAGENERATOR_H_
#define TESTDATAGENERATOR_H_

#include "DataTypes.h"

class TestDataGenerator
{
public:
  TestDataGenerator();
  virtual
  ~TestDataGenerator();

  DataTypes::Person createRandomPerson();

};

#endif /* TESTDATAGENERATOR_H_ */
