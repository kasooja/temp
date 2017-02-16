# temp

Total files in ACL corpus from 1979 to 2015: 21481 
ACL Corpus download: [ ParsCit structured XML ] @ http://acl-arc.comp.nus.edu.sg/

First, use the text-preprocessors written in Java to create Hyphen_Cleaned_Uncompressed_Separated_Text folder.

Then, below steps using Python code:

End year value is 2019 in the example because it is in the range function with step value of 5.
So, the values of the dirs would be 1979, 1984, 1989, ...., 2009, 2014.

1. First use python/embeddings/count/buildindexes.py to create a word-dictionary from the overall text. 

    DATA = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_Cleaned_Uncompressed_Separated_Text/"

    OUT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/info/"

2. Use python/embeddings/count/computefrequencies.py to compute the frequencies per time-period dir, and overall frequencies of the words

    DATA = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_Cleaned_Uncompressed_Separated_Text/"

    OUT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/"

3. Use python/embeddings/count/combinefreqdicts.py to make a pickle (serialized) file containing all time-period freq dists.
    
    DIR = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/time_period_freqs/"

4. Use python/embeddings/count/makewordlists.py to create all word list, and all minus stopwords word list.
    
    FREQS = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/time_period_freqs/{type}.pkl"
 
    OUT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/word_lists/{type}-{cond}.pkl"

5. Use python/embeddings/count/makeglobalwordlists.py to create a global word list.
    Use average frequency or total frequency?
    
    FREQS = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/full_freqs/{type}.pkl"
    
    OUT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/word_lists/full-{type}-{cond}.pkl"

6. Use python/embeddings/count/makecoocccurs.py to create a co-occurence matrix.
   
    script parameters: -type word -window_size 5
    
    
    
    
    
    
Below is to be updated:
    
index.pkl here is same as word-dict.pkl in the info folder


ppmigen args example:
-count_path /home/kat/Downloads/Anne/CurrentData/Uncompressed_pickled/cooccurs/word/5/2009.bin -out_path /home/kat/Downloads/Annee/CurrentData/Uncompressed_pickled/ppmi/2009

makelowdim args example:

-in_dir /home/kat/Downloads/Anne/CurrentData/Uncompressed_pickled/ppmi/ -count_dir /home/kat/Downloads/Annee/CurrentData/Uncompressed_pickled/halfdecade_freqs/ -word_file /home/kat/Downloads/Annee/CurrentData/Uncompressed_pickled/cooccurs/word/5/index.pkl
