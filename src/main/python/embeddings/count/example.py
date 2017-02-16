from sequentialembedding import SequentialEmbedding

"""
Example showing how to load a series of historical embeddings and compute similarities over time.
Warning that loading all the embeddings into main memory can take a lot of RAM
"""

if __name__ == "__main__":
    aligned = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/ppmi/svd/250/280594/11/aligned/"
    acl_embeddings = SequentialEmbedding.load(aligned, range(1979, 2019, 5))
    time_sims = acl_embeddings.get_time_sims("translation", "statistical")
    print "Similarity between translation and statistical drastically increases from 1979s to the 2009s:"
    for year, sim in time_sims.iteritems():
        print "{year:d}, cosine similarity={sim:0.2f}".format(year=year,sim=sim)

    time_neigh = acl_embeddings.get_seq_neighbour_set("translation", n=5)
    print "Overall related words for translation from 1979s to the 2009s:"
    print time_neigh

    time_neigh_dic = acl_embeddings.get_seq_neighbour_dic("translation", n=10)
    print "Related words for translation from 1979s to the 2009s:"

    for year in time_neigh_dic.keys():
        print str(year) + ":  " + str(time_neigh_dic.get(year))

