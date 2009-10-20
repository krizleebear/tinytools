/*
 * UnicodeTest.cpp
 *
 *  Created on: Oct 20, 2009
 *      Author: krizleebear
 */

#include "UnicodeTest.h"

#include <iostream>
#include <fstream>

int main(void)
{
	UnicodeTest* u = new UnicodeTest();
	u->readTest();
}

UnicodeTest::UnicodeTest()
{

}

UnicodeTest::~UnicodeTest()
{
}

void UnicodeTest::readTest()
{
	std::wifstream fis("unicodeFile.txt");

	if(!fis.is_open())
	{
		std::cout << "FILE NOT OPEN!" << std::endl;
		return;
	}

	while(fis.good())
	{
		std::cout << fis.get() << std::endl;
	}



	std::cout << "ende gelände"  << std::endl;

	fis.close();
}
