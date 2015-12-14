to use run `sbt "run <path to log file> <n1> <n2> <n3>"`

n1 - maximal distance between 2 neighbor strings

n2 - minimal number of neighbor strings to treat them as not an anomaly

n3 - number of strings in one block which can be processed independently

example:

`sbt "run logs.txt 4 20 1000"`