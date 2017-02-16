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


6. Use python/embeddings/count/makecoocccurs.py to create a co-occurrence matrix.

        DATA = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_Cleaned_Uncompressed_Separated_Text/"

        DICT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/info/{type}-dict.pkl"

        OUT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/cooccurs/{type}/{window_size:d}/"

      script parameters: -type word -window_size 5
    
    
7. Copy, rename and put the word-dict (DICT) file used in creating the word-cooccurrence matrix in the above step, to OUT folder above /home/kat/Downloads/Anne/embeddings/span_period_years/5/cooccurs/word (notice removal of window_size:d, and type value used here is word) as index.pkl.
    In short, index.pkl here is same as word-dict.pkl in the info folder, copied in the co-occurs folder.

   Then use python/embeddings/count/ppmigen.py to generate ppmi based matrix using the co-occurence matrix created in the above step.
   
      script parameters: -count_path /home/kat/Downloads/Anne/embeddings/span_period_years/5/cooccurs/word/5/1979.bin -out_path /home/kat/Downloads/Anne/embeddings/span_period_years/5/ppmi/1979
    
      The other parameters used the default values in this example.
    
   Run ppmigen.py for all the time_period_dirs by changing the script parameters.

   
8. Use python/embeddings/count/makelowdim.py to generate low dimensional SVD embeddings from the ppmi matrices generated above. 
   
      script parameters:

      -in_dir /home/kat/Downloads/Anne/embeddings/span_period_years/5/ppmi/ -count_dir /home/kat/Downloads/Anne/embeddings/span_period_years/5/time_period_freqs/ -word_file /home/kat/Downloads/Anne/embeddings/span_period_years/5/word_lists/full-word-nstop.pkl

      The other parameters used the default values in this example.


9. Use python/embeddings/count/seq_procrustes.py to align the learned embeddings in the above step.
      
      script parameters:
      
      -dir /home/kat/Downloads/Anne/embeddings/span_period_years/5/ppmi/svd/250/280594/11/ -rep_type SVD -count_dir /home/kat/Downloads/Anne/embeddings/span_period_years/5/time_period_freqs/ -word_file /home/kat/Downloads/Anne/embeddings/span_period_years/5/word_lists/full-word-nstop.pkl
      
      The other parameters used the default values in this example.
      
      
10. Finally, run python/embeddings/count/example.py with the aligned embeddings generated above:
    
    /home/kat/Downloads/Anne/embeddings/span_period_years/5/ppmi/svd/250/280594/11/aligned/
    
    Sample output 1 below:
    
    Similarity between translation and statistical drastically increases from 1979s to the 2014s:
    
    1979, cosine similarity=-0.01
    
    1984, cosine similarity=-0.05
    
    1989, cosine similarity=0.09
    
    1994, cosine similarity=0.17
    
    1999, cosine similarity=0.43
    
    2004, cosine similarity=0.45
    
    2009, cosine similarity=0.51
    
    2014, cosine similarity=0.41
    
    Overall related words for translation from 1979s to the 2014s:
    
    set(['translator', 'tsujii', 'sergei', 'smt', 'translations', 'ebmt', 'slocum', 'machine', 'mt', 'lawson', 'bilingual', 'mechanical', 'translation', 'automatic'])
    
    Related words for translation from 1979s to the 2014s:
    
    1979:  set(['aided', 'translator', 'translations', 'logical', 'machine', 'mt', 'mechanical', 'translated', 'translation', 'automatic'])
    
    1984:  set(['ajcl', 'translator', 'lugano', 'allc', 'loh', 'issco', 'machine', 'mt', 'lawson', 'translation'])
    
    1989:  set(['sergei', 'tsujii', 'rbmt', 'transfer', 'interlingua', 'ebmt', 'slocum', 'machine', 'nirenburg', 'translation'])
    
    1994:  set(['transfer', 'translations', 'ebmt', 'machine', 'mt', 'ents', 'translation', 'kbmt', 'furuse', 'tdmt'])
    
    1999:  set(['translating', 'smt', 'translations', 'ebmt', 'machine', 'mt', 'bilingual', 'translation', 'hpat', 'alignment'])
    
    2004:  set(['och', 'source', 'smt', 'translations', 'machine', 'mt', 'bilingual', 'translation', 'zens', 'alignment'])
    
    2009:  set(['koehn', 'smt', 'translations', 'ebmt', 'machine', 'mt', 'statistical', 'translation', 'translate', 'moses'])
    
    2014:  set(['bleu', 'translating', 'somers', 'smt', 'translations', 'machine', 'mt', 'translation', 'quality', 'moses'])
    
    
    
    
    
    
    
   Sample output 2 below:
   
   Similarity between neural and deep drastically increases from 1979s to the 2014s:
   
   1979, cosine similarity=0.00
   
   1984, cosine similarity=-0.06
   
   1989, cosine similarity=0.07
   
   1994, cosine similarity=-0.01
   
   1999, cosine similarity=0.07
   
   2004, cosine similarity=-0.05
   
   2009, cosine similarity=0.46
   
   2014, cosine similarity=0.64

   Overall related words for neural from 1979s to the 2014s:
    
   set(['ssns', 'spreading', 'subscribe', 'zero', 'computation', 'nets', 'networks', 'network', 'zone', 'recurrent', 'young', 'bengio', 'quarterly', 'hinton', 'convolutional', 'rnn', 'activation', 'optical', 'bayesian', 'timely', 'neural', 'yorktown', 'backpropagation', 'softmax', 'z'])

   Related words for neural from 1979s to the 2014s:
    
   1979:  set(['zone', 'yn', 'yielding', 'young', 'yield', 'yorktown', 'zero', 'yields', 'york', 'z'])
   
   1984:  set(['contributing', 'rapidly', 'interdisciplinary', 'neural', 'spreading', 'reviews', 'optical', 'computation', 'quarterly', 'activation'])
   
   1989:  set(['bayesian', 'refereed', 'publication', 'neural', 'timely', 'subscribe', 'computation', 'quarterly', 'classifier', 'today'])
   
   1994:  set(['network', 'neural', 'recurrent', 'elman', 'networks', 'backpropagation', 'hinton', 'nets', 'rumelhart', 'rcc'])
   
   1999:  set(['sejnowski', 'pulsed', 'network', 'rosenberg', 'neural', 'hnn', 'backpropagation', 'softmax', 'synchrony', 'networks'])
   
   2004:  set(['network', 'ssns', 'sbns', 'neural', 'bishop', 'backpropagation', 'ssn', 'activations', 'synchrony', 'networks'])
   
   2009:  set(['rnn', 'neural', 'recurrent', 'dnns', 'autoencoders', 'bengio', 'collobert', 'mikolov', 'hinton', 'mnih'])
   
   2014:  set(['convolutional', 'feedforward', 'network', 'neural', 'recursive', 'recurrent', 'elman', 'rnns', 'networks', 'rnn'])