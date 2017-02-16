import numpy as np

import pyximport
pyximport.install(setup_args={"script_args":["--compiler=unix"],
                              "include_dirs":np.get_include()},
                  reload_support=True)
import os

from collections import Counter
from multiprocessing import Queue, Process
from Queue import Empty
from argparse import ArgumentParser

from aclstringutils import process_line
from ioutils import load_pickle, mkdir
import sparse_io


DATA = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_Cleaned_Uncompressed_Separated_Text/"

DICT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/info/{type}-dict.pkl"

OUT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/cooccurs/{type}/{window_size:d}/"


def worker(proc_num, queue, window_size, type, id_map):
    while True:
        try:
            time_period_dir = str(queue.get(block=False))
        except Empty:
             break
        print "Proc:", proc_num, "time period: ", time_period_dir
        pair_counts = Counter()
        for file in os.listdir(DATA + time_period_dir):
            with open(DATA + time_period_dir + "/" + file) as fp:
                #print proc_num, file
                context = []
                for line in fp:
                    words = process_line(line)
                    for word in words:
                        if type == "word":
                            item = word
                        else:
                            raise Exception("Unknown type {}".format(type))
                        if item == None:
                            continue

                        context.append(id_map[item])
                        if len(context) > window_size * 2 + 1:
                            context.pop(0)
                        pair_counts = _process_context(context, pair_counts, window_size)
        sparse_io.export_mat_from_dict(pair_counts, OUT.format(type=type, window_size=window_size) + "/" + time_period_dir + ".bin")


def _process_context(context, pair_counts, window_size):
    if len(context) < window_size + 1:
        return pair_counts
    target = context[window_size]
    indices = range(0, window_size)
    indices.extend(range(window_size + 1, 2 * window_size + 1))
    for i in indices:
        if i >= len(context):
            break
        pair_counts[(target, context[i])] += 1
    return pair_counts


if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("-type")
    parser.add_argument("-window_size", type=int)
    parser.add_argument("--workers", type=int, default=25)
    args = parser.parse_args()
    mkdir(OUT.format(type=args.type, window_size=args.window_size))
    queue = Queue()
    span_period_years = 5
    start_year = 1979
    end_year = 2019
    for time_period_dir in range(start_year, end_year, span_period_years):
        queue.put(time_period_dir)
    id_map = load_pickle(DICT.format(type=args.type))
    procs = [Process(target=worker, args=[i, queue, args.window_size, args.type, id_map]) for i in range(args.workers)]
    for p in procs:
        p.start()
    for p in procs:
        p.join()
