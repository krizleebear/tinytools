/*
 * DataTypes.h
 *
 *  Created on: Sep 13, 2009
 *      Author: esol
 */

#ifndef DATATYPES_H_
#define DATATYPES_H_

#include <string>

using namespace std;

class DataTypes
{
public:

  struct Phone;
  struct Person;

  struct Phone
  {
    int id;
    string displayedNumber;
  };

  struct Person
  {
    int id;
    string displayedName;
    string firstName;
    string lastName;
    Phone phone1;
    Phone phone2;
    Phone phone3;

    virtual string
    toString()
    {

      return "Person :: " +  this->displayedName + " " + this->firstName + " " + this->lastName;
    }
  };

};

#endif /* DATATYPES_H_ */
