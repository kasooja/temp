from sequentialembedding import SequentialEmbedding

"""
Example showing how to load a series of historical embeddings and compute similarities over time.
Warning that loading all the embeddings into main memory can take a lot of RAM
"""

start_year = 1979
end_year = 2014
time_period = 5

aligned = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/ppmi/svd/250/280594/11/aligned/"
acl_embeddings = SequentialEmbedding.load(aligned, range(start_year, end_year + time_period, 5))


def semantic_shift_word_last_1_year_dir(word, start_year):
    print "*******************************"
    print word

    last_year = start_year

    for year in range(last_year + time_period, end_year + time_period, time_period):

        last_vs_current_rel = -1.0

        if not (acl_embeddings.embeds.get(last_year).oov(word) and
                    acl_embeddings.embeds.get(last_year).oov(word)):

            last_year_word_embed = acl_embeddings.embeds.get(last_year).represent(word)
            current_year_word_embed = acl_embeddings.embeds.get(year).represent(word)

            last_vs_current_rel = last_year_word_embed.dot(current_year_word_embed)

        if last_vs_current_rel == -1.0:
            print last_year, "  ", year, "  ", "N/A"
        else:
            print last_year, "  ", year, " change: ", 1 - last_vs_current_rel
        print "....................."

        last_year = year



if __name__ == "__main__":
    acl_embeddings = SequentialEmbedding.load(aligned, range(1979, 2019, 5))

    word = "dsp"
    #word1 = "svm"

    # time_sims = acl_embeddings.get_time_sims(word, word1)
    # print "Similarity between" + word + " and "  + word1 + "drastically increases from 1979s to the 2014s:"
    # for year, sim in time_sims.iteritems():
    #     print "{year:d}, cosine similarity={sim:0.2f}".format(year=year,sim=sim)

    semantic_shift_word_last_1_year_dir(word, 1979)

    time_neigh = acl_embeddings.get_seq_neighbour_set(word, n=5)
    print "Overall related words for " + word + "from 1979s to the 2014s:"
    print time_neigh

    time_neigh_dic = acl_embeddings.get_seq_neighbour_dic(word, n=10)
    print "Related words for " + word + "from 1979s to the 2014s:"

    for year in time_neigh_dic.keys():
        print str(year) + ":  " + str(time_neigh_dic.get(year))


