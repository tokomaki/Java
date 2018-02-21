# -*- coding: utf-8 -*- 

import datetime, os, signal, subprocess, sys, time, unittest

def run(command, stdin = None, timeout = 30):
    """
    Runs the specified command using specified standard input (if any) and
    returns the output on success. If the command doesn't return within the
    specified time (in seconds), "__TIMEOUT__" is returned.
    """

    start = datetime.datetime.now()
    process = subprocess.Popen(command.split(),
                               stdin = subprocess.PIPE, 
                               stdout = subprocess.PIPE,
                               stderr = subprocess.STDOUT)
    if not stdin is None:
        process.stdin.write(stdin)
        process.stdin.close()
    while process.poll() is None:
        time.sleep(0.1)
        now = datetime.datetime.now()
        if (now - start).seconds > timeout:
            os.kill(process.pid, signal.SIGKILL)
            os.waitpid(-1, os.WNOHANG)
            return "__TIMEOUT__"
    return process.stdout.read().strip()

class Problem1(unittest.TestCase):
    
    def test1(self):
        command = "java Term data/cities.txt 5"
        sought = """Top 5 by lexicographic order:
2200	's Gravenmoer, Netherlands
19190	's-Gravenzande, Netherlands
134520	's-Hertogenbosch, Netherlands
3628	't Hofke, Netherlands
246056	A Coruña, Spain
Top 5 by reverse-weight order:
14608512	Shanghai, China
13076300	Buenos Aires, Argentina
12691836	Mumbai, India
12294193	Mexico City, Distrito Federal, Mexico
11624219	Karachi, Pakistan"""
        got = run(command)
        self.assertNotEquals(got, "__TIMEOUT__")
        self.assertEquals(got, sought)

class Problem2(unittest.TestCase):
    
    def test1(self):
        command = "java BinarySearchDeluxe data/wiktionary.txt cook"
        sought = """3"""
        got = run(command)
        self.assertNotEquals(got, "__TIMEOUT__")
        self.assertEquals(got, sought)

class Problem3(unittest.TestCase):
    
    def test1(self):
        command = "java Autocomplete data/cities.txt 10"
        sought = """52851	Istaravshan, Tajikistan
44373	Istres, France
40000	Istok, Kosovo
33390	Istra, Russia
13788	Istmina, Colombia
7763	Istrana, Italy
5670	Istok, Russia
4895	Istebna, Poland
4199	Istiaía, Greece
2591	Istria, Romania"""
        got = run(command, "Ist")
        self.assertNotEquals(got, "__TIMEOUT__")
        self.assertEquals(got, sought)

if __name__ == "__main__":
    unittest.main()
