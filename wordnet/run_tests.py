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
        command = "java WordNet data/synsets.txt data/hypernyms.txt worm bird"
        sought = """# of nouns = 119188
isNoun(worm) = true
isNoun(bird) = true
isNoun(worm bird) = false
sca(worm, bird) = animal animate_being beast brute creature fauna
distance(worm, bird) = 5"""
        got = run(command)
        self.assertNotEquals(got, "__TIMEOUT__")
        self.assertEquals(got, sought)

class Problem2(unittest.TestCase):
    
    def test1(self):
        command = "java ShortestCommonAncestor data/digraph1.txt"
        sought = """length = 4, ancestor = 1
length = 3, ancestor = 5
length = 4, ancestor = 0"""
        got = run(command, "3 10 8 11 6 2")
        self.assertNotEquals(got, "__TIMEOUT__")
        self.assertEquals(got, sought)

class Problem3(unittest.TestCase):
    
    def test1(self):
        command = "java Outcast data/synsets.txt data/hypernyms.txt data/outcast5.txt data/outcast8.txt data/outcast11.txt"
        sought = """outcast(data/outcast5.txt) = table
outcast(data/outcast8.txt) = bed
outcast(data/outcast11.txt) = potato"""
        got = run(command)
        self.assertNotEquals(got, "__TIMEOUT__")
        self.assertEquals(got, sought)

if __name__ == "__main__":
    unittest.main()
