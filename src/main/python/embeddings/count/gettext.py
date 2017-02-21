from __future__ import print_function
import nltk
import nltk.data
import sys
import re
import os
from ioutils import  mkdir

reload(sys)
sys.setdefaultencoding('utf8')

# set up sentence splitter
extra_abbreviations = ['dr', 'vs', 'mr', 'mrs', 'prof', 'inc', 'et al', 'cf', 'e.g', 'Fig', 'i.e', 'annessectionnumber',
                       'Eq', 'eq', 'Dr', 'fig']
sentence_splitter = nltk.data.load('tokenizers/punkt/english.pickle')
sentence_splitter._params.abbrev_types.update(extra_abbreviations)

DATA = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_Cleaned_Uncompressed_Separated_Text/"
OUTPUT = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_cleaned_pos/"

start_year = 1979
end_year = 2019
span_period_years = 5


for dir_name in range(start_year, end_year, span_period_years):
    folder = str(dir_name)
    mkdir(OUTPUT + "/" + folder)
    print("Processing folder...", folder)
    for datafile in os.listdir(DATA + folder):
        with open(DATA + folder + "/" + datafile) as fp:
            output = []
            outfile = open(OUTPUT + "/" + folder + "/" + datafile, 'w')

            for line in fp:
                line = line.decode('utf-8').strip()
                sents = sentence_splitter.tokenize(line)

                for sent in sents:
                    tok = nltk.word_tokenize(sent)
                    sent = ''
                    pos = nltk.pos_tag(tok)
                    for p in pos:
                        if re.match('NN', p[1]):
                            sent += p[0] + '_N '
                        if re.match('JJ', p[1]):
                            sent += p[0] + '_A '
                        if re.match('V', p[1]):
                            sent += p[0] + '_V '
                    #print(sent)
                    output.append(sent)
            reworkedo = '\n'.join(output)

            if re.search('et al.\n', reworkedo):  # remove incorrect splits that persisted from the start
                reworkedo = re.sub(r'\n\s', '\n', reworkedo)
                reworkedo = re.sub(r'et al\.\n', 'et al\. ', reworkedo)
                reworkedo = re.sub(r'(?P<figure>[fF]ig\.)\n', '\g<1> ', reworkedo)

            print(reworkedo, file=outfile)
            outfile.close()


                    #print(reworkedo, file=outfile)

#infile.close()
#outfile.close()
#logfile.close()