from ioutils import load_year_words

from sequentialembedding import SequentialEmbedding

"""
Example showing extraction of words which had a significant semantic shift
"""

aligned = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/ppmi/svd/250/280594/11/aligned/"
acl_embeddings = SequentialEmbedding.load(aligned, range(1979, 2019, 5))

start_year = 1979
end_year = 2014
time_period = 5

threshold_distance = 0.1



def semantic_shift_word_last_2_year_dir(word, start_year):
    print "*******************************"
    print word

    second_last_year = start_year
    last_year = second_last_year + time_period

    for year in range(last_year + time_period, end_year + time_period, time_period):

        second_last_vs_last_rel = -1.0
        second_last_vs_current_rel = -1.0

        if not (acl_embeddings.embeds.get(second_last_year).oov(word) and
                    acl_embeddings.embeds.get(last_year).oov(word) and acl_embeddings.embeds.get(year).oov(word)):

            second_last_year_word_embed = acl_embeddings.embeds.get(second_last_year).represent(word)
            last_year_word_embed = acl_embeddings.embeds.get(last_year).represent(word)
            current_year_word_embed = acl_embeddings.embeds.get(year).represent(word)

            second_last_vs_last_rel = second_last_year_word_embed.dot(last_year_word_embed)
            second_last_vs_current_rel = second_last_year_word_embed.dot(current_year_word_embed)

        print second_last_year, "  ", last_year, "  ", second_last_vs_last_rel
        print second_last_year, "  ", year, "  ", second_last_vs_current_rel
        print "....................."

        second_last_year = last_year
        last_year = year


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

        print last_year, "  ", year, "  ", last_vs_current_rel
        print "....................."

        last_year = year


def semantic_shift_word_last_1_year_dir_exists_all(word, start_year):
    last_year = start_year

    str_list = []
    change_year_list = []
    rel_list = []

    for year in range(last_year + time_period, end_year + time_period, time_period):

        last_vs_current_rel = -1.0

        if not (acl_embeddings.embeds.get(last_year).oov(word) or
                    acl_embeddings.embeds.get(year).oov(word)):

            last_year_word_embed = acl_embeddings.embeds.get(last_year).represent(word)
            current_year_word_embed = acl_embeddings.embeds.get(year).represent(word)

            last_vs_current_rel = last_year_word_embed.dot(current_year_word_embed)

            str_list.append(str(last_year) + "  " + str(year) + "  " + str(last_vs_current_rel))
            rel_list.append(last_vs_current_rel)
            ch = 1 - last_vs_current_rel
            if ch > 1.0:
                ch = 1.0
            change_year_list.append([year, ch ])

        else:
            return None

        last_year = year

    return rel_list, change_year_list


def getKey(item):
    return item[1]

test_word = "kernel"
#semantic_shift_word_last_1_year_dir_exists_all(test_word, 1984)

semantic_shift_word_last_1_year_dir(test_word, 1979)

word_file = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/word_lists/full-word-nstop.pkl"

years = range(start_year, end_year + 1, time_period)
words = load_year_words(word_file, years)

print "***********************************Start***************************************"

i = 0
j = 0

total_change_list = []
specific_change_list = []
for word in words[end_year]:
    #print i
   # print "*******************************"
    #print word
    exists = semantic_shift_word_last_1_year_dir_exists_all(word, 1984)
    if exists is not None:
        print word
        rel_list, change_year_list = exists
        print rel_list
        print change_year_list
        print "***************"
        j = j+1

        total_change = 0.0
        biggest_change = -100.0
        biggest_change_year = -1900
        for year, change in change_year_list:
            total_change = total_change + change

            if change > biggest_change:
                biggest_change = change
                biggest_change_year = year
                specific_change_list.append([biggest_change_year, biggest_change, word])
        total_change_list.append([word, total_change])


    i= i+1

print i
print j

total_change_list = sorted(total_change_list, key = getKey, reverse=True)

print "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^Change^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
count = 0
for word, change in total_change_list:
    if count < 25:
        print word, change
        count = count + 1
    else:
        break

print "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^Biggest Change^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
target = open("/home/kat/Downloads/Anne/change/specific_change.txt", 'w')
#biggest change in the last 5 year, and the year
specific_change_list = sorted(specific_change_list, key = getKey, reverse=True)
count = 0
for biggest_change_year, biggest_change, word in specific_change_list:
    #if count < 500:
    print word, biggest_change_year, biggest_change
    target.write(word + "\t" + str(biggest_change_year) + "\t" + str(biggest_change) + "\n")
    #count = count + 1(
    #else:
     #   break
target.close()