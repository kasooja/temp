import numpy as np
import time
import random

from sklearn.utils.extmath import randomized_svd
from multiprocessing import Queue, Process
from argparse import ArgumentParser

from explicit import Explicit
from ioutils import load_year_words, mkdir, write_pickle, words_above_count

from sequentialembedding import SequentialEmbedding
import collections

"""
Example showing extraction of words which had a significant semantic shift
"""

if __name__ == "__main__":

    aligned = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/ppmi/svd/250/280594/11/aligned/"
    acl_embeddings = SequentialEmbedding.load(aligned, range(1979, 2019, 5))

    word_file = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/word_lists/full-word-nstop.pkl"
    start_year = 1979
    end_year = 2014
    time_period = 5

    years = range(start_year, end_year + 1, time_period)
    years.reverse()

    words = load_year_words(word_file, years)

    threshold_distance = 0.1

    sample_word = "translation"

    collections.OrderedDict().keys()

    for word in words[end_year]:

        print "*******************************"
        print word

        prev_year = 1984
        prev_prev_year = 1979
        word = "neural"
        for year in range(1989, 2019, 5):
            prev_rel = -1.0
            rel = -1.0
            if not (acl_embeddings.embeds.get(prev_prev_year).oov(word) and acl_embeddings.embeds.get(prev_year).oov(word) and acl_embeddings.embeds.get(year).oov(word)):
                previous_previous_year_word_embed = acl_embeddings.embeds.get(prev_prev_year).represent(word)
                previous_year_word_embed = acl_embeddings.embeds.get(prev_year).represent(word)
                current_year_word_embed = acl_embeddings.embeds.get(year).represent(word)

                prev_rel = previous_previous_year_word_embed.dot(previous_year_word_embed)
                rel = previous_previous_year_word_embed.dot(current_year_word_embed)

            print prev_prev_year, "  ", prev_year, "  ", prev_rel
            print prev_prev_year, "  ", year, "  ", rel
            print "....................."

            prev_prev_year = prev_year
            prev_year = year


        # prev_prev_year = 1979
        # prev_year = 198
        #
        # for year in range(1989, 2019, 5):
        #     print prev_prev_year, "  ", prev_year, "  ", year
        #     prev_prev_year = prev_year
        #     prev_year = year




    # def get_time_sims(self, word1, word2):
    #     time_sims = collections.OrderedDict()
    #     for year, embed in self.embeds.iteritems():
    #         time_sims[year] = embed.similarity(word1, word2)
    #     return time_sims




    #
    # aligned = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/ppmi/svd/250/280594/11/aligned/"
    # acl_embeddings = SequentialEmbedding.load(aligned, range(1979, 2019, 5))
    # time_sims = acl_embeddings.get_time_sims("neural", "deep")
    # print "Similarity between neural and deep drastically increases from 1979s to the 2014s:"
    # for year, sim in time_sims.iteritems():
    #     print "{year:d}, cosine similarity={sim:0.2f}".format(year=year,sim=sim)
    #
    # time_neigh = acl_embeddings.get_seq_neighbour_set("neural", n=5)
    # print "Overall related words for neural from 1979s to the 2014s:"
    # print time_neigh
    #
    # time_neigh_dic = acl_embeddings.get_seq_neighbour_dic("neural", n=10)
    # print "Related words for neural from 1979s to the 2014s:"
    #
    # for year in time_neigh_dic.keys():
    #     print str(year) + ":  " + str(time_neigh_dic.get(year))

