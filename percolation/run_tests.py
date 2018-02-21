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
        command = "java Percolation data/input10.txt"
        sought = """56 open sites
percolates"""
        got = run(command)
        self.assertNotEquals(got, "__TIMEOUT__")
        self.assertEquals(got, sought)

    def test2(self):
        command = "java Percolation data/input10-no.txt"
        sought = """55 open sites
does not percolate"""
        got = run(command)
        self.assertNotEquals(got, "__TIMEOUT__")
        self.assertEquals(got, sought)

class Problem2(unittest.TestCase):
    
    def test1(self):
        command = "java PercolationStats 50 10000"
        got = run(command)
        self.assertNotEquals(got, "__TIMEOUT__")
        toks = got.split()
        mu, sigma, clow, chigh = toks[2], float(toks[5]), toks[8], toks[11]
        self.assertTrue(mu.startswith("0.59") and sigma < 0.1 and clow.startswith("0.59") and chigh.startswith("0.59"))

if __name__ == "__main__":
    unittest.main()
