import numpy as np
from argparse import ArgumentParser

import alignment
from representation_factory import create_representation
from ioutils import write_pickle, words_above_count, mkdir, load_year_words


def align_years(words_dic, years, rep_type, in_dir, out_dir, count_dir, min_count, **rep_args):
    first_iter = True
    base_embed = None
    for year in years:
        print "Loading year:", year
        year_embed =  create_representation(rep_type, in_dir + str(year), **rep_args)
        #year_words = words_above_count(count_dir, year, min_count)

        valid_words = set(words_above_count(count_dir, year, min_count))
        print "valid words in year " + str(year) + ": " + str(len(valid_words))
        overall_words = words_dic[year]
        print "overall words in year " + str(year) + ": " + str(len(overall_words))
        year_words = list(valid_words.intersection(overall_words))

        year_embed.get_subembed(year_words)
        print "Aligning year:", year
        if first_iter:
            aligned_embed = year_embed
            first_iter = False
        else:
            aligned_embed = alignment.smart_procrustes_align(base_embed, year_embed)
        base_embed = aligned_embed  
        print "Writing year:", year
        foutname = out_dir + str(year)
        np.save(foutname + "-w.npy",aligned_embed.m)
        write_pickle(aligned_embed.iw, foutname + "-vocab.pkl")

if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("-dir")
    parser.add_argument("-rep_type")
    parser.add_argument("-count_dir")
    parser.add_argument("-word_file")
    parser.add_argument("--min-count", type=int, default=22)
    parser.add_argument("--start-year", type=int, default=1979)
    parser.add_argument("--end-year", type=int, default=2014)
    parser.add_argument("--year-inc", type=int, default=5)
    args = parser.parse_args()
    years = range(args.start_year, args.end_year + 1, args.year_inc)
    out_dir = args.dir + "/aligned/"
    words = load_year_words(args.word_file, years)
    mkdir(out_dir)
    align_years(words, years, args.rep_type, args.dir, out_dir, args.count_dir, args.min_count)
