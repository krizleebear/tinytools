/*
 * UnicodeTest.h
 *
 *  Created on: Oct 20, 2009
 *      Author: krizleebear
 */

#ifndef UNICODETEST_H_
#define UNICODETEST_H_

class UnicodeTest {
private:
	wchar_t buffer[1024];
public:
	UnicodeTest();
	virtual ~UnicodeTest();

	void readTest();
};

#endif /* UNICODETEST_H_ */
