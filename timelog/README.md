TimeLog (for Windows)
========================

Very simple tool that lets you know when your computer was switched on or off. (or went to sleep and woke up). 

You might find that useful to track your work hours.

There is no GUI. It uses JNA to parse Windows Event Log and produces output like this:

    ------------ 2016-05-15 ------------ 
    19:24:07: POWERUP
    19:24:15: WAKE
    19:25:17: SHUTDOWN
    ------------ 2016-06-02 ------------ 
    17:37:55: POWERUP
    17:38:02: WAKE
    17:48:27: WAKE
    17:52:18: POWERUP
    17:52:19: POWERUP
  
 License
=======

This tool is licensed under the Apache Software License, version 2.0. The great JNA library that does the heavy lifting can be found [here](https://github.com/java-native-access/jna)