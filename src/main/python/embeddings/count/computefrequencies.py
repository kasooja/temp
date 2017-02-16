import os
from Queue import Empty
from collections import Counter
from multiprocessing import Queue, Process


from ioutils import write_pickle, load_pickle, mkdir
from aclstringutils import process_line

DATA = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_Cleaned_Uncompressed_Separated_Text/"
OUT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/"


def worker(proc_num, queue):
    while True:
        try:
            dir = str(queue.get(block=False))
        except Empty:
             break
        print "Proc:", proc_num, "time period:", dir
        word_freqs = Counter()
        for file in os.listdir(DATA + dir):
            with open(DATA + dir + "/" + file) as fp:
                print proc_num, file
                for line in fp:
                    words = process_line(line)
                    if len(words) == 0:
                        continue
                    for word in words:
                        word_freqs[word] += 1

        write_pickle(word_freqs, OUT + "time_period_freqs/" + dir + "-word.pkl")

if __name__ == "__main__":
    queue = Queue()
    span_period_years = 5
    start_year_dir = 1979
    end_year_dir = 2014
    mkdir(OUT + "time_period_freqs/")
    for dir in range(start_year_dir, end_year_dir, span_period_years):
        queue.put(dir)
    procs = [Process(target=worker, args=[i, queue]) for i in range(25)]
    for p in procs:
        p.start()
    for p in procs:
        p.join()
    print "Getting full freqs..."
    word_freqs = Counter()

    for dir in range(start_year_dir, end_year_dir, span_period_years):
        dir = str(dir)
        print dir
        word_freqs += load_pickle(OUT + "time_period_freqs/" + dir + "-word.pkl")

    write_pickle(word_freqs, OUT + "full_freqs/word.pkl") 
