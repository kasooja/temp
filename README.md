# temp

Total files in ACL corpus from 1979 to 2015: 21481 
ACL Corpus download: [ ParsCit structured XML ] @ http://acl-arc.comp.nus.edu.sg/

First, use the text-preprocessors written in Java to create Hyphen_Cleaned_Uncompressed_Separated_Text folder.

Then, below steps using Python code:


1. First use python/embeddings/count/buildindexes.py to create a word-dictionary from the overall text. 

    DATA = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_Cleaned_Uncompressed_Separated_Text/"

    OUT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/info/"

2. Use python/embeddings/count/computefrequencies.py to compute the frequencies per time-period dir, and overall frequencies of the words

    DATA = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_Cleaned_Uncompressed_Separated_Text/"

    OUT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/"
