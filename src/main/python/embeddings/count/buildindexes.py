import os

from ioutils import write_pickle
from aclstringutils import process_line

DATA = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_Cleaned_Uncompressed_Separated_Text/"
OUT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/info/"


def process_file(fp, word_dict):
    for line in fp:
        #print line
        words = process_line(line)
        if len(words) == 0:
            continue
        for word in words:
            if word not in word_dict:
                dic_size = len(word_dict)
                word_dict[word] = dic_size
            else:
                print word


if __name__ == "__main__":
    word_dict = {}
    span_period_years = 5
    start_year = 1979
    end_year = 2019
    for dir_name in range(start_year, end_year, span_period_years):
        folder = str(dir_name)
        print "Processing folder...", folder
        for datafile in os.listdir(DATA + folder):
            with open(DATA + folder + "/" + datafile) as fp:
                print "Processing file..", folder + "/" + datafile
                process_file(fp, word_dict)
    write_pickle(word_dict, OUT + "word-dict.pkl")
