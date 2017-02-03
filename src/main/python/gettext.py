from __future__ import print_function
import nltk
import nltk.data
import sys
import re
from optparse import OptionParser #for the tagging option; the module is deprecated, code should be updated, if time allows

reload(sys)  
sys.setdefaultencoding('utf8')

infile = open(sys.argv[1], 'r')
outfile = open(sys.argv[2], 'w')
logfile = open(sys.argv[3], 'a')

filename = sys.argv[1] #save name of currently processed file for logging purposes (system exits below)

parser = OptionParser() #set up the parsing option
parser.add_option('-t', '--tag', action='store', type='int', dest='tagger', help='set this option to 1 to tag the text and extract only nouns, adjectives, and verbs')
(options, args) = parser.parse_args()

#arrays to hold text data
text = []
output = []

#set up sentence splitter
extra_abbreviations = ['dr', 'vs', 'mr', 'mrs', 'prof', 'inc', 'et al', 'cf', 'e.g', 'Fig', 'i.e', 'annessectionnumber', 'Eq', 'eq', 'Dr', 'fig']
sentence_splitter = nltk.data.load('tokenizers/punkt/english.pickle')
sentence_splitter._params.abbrev_types.update(extra_abbreviations)

lines = infile.readlines()
for line in lines:
	if not re.search('[a-zA-Z0-9]', line): #remove lines without any letter content
		line = ''
	if re.match('.?\s?[0-9]+\s?.?$', line): #remove lines that only contain page numbers
		line = ''
	#do not conflate headings
	if re.search('^\s*[0-9]*\.?[0-9]* [A-Z]\w+|[A-Z ]+', line) and len(line) < 35: 
		if not re.search('[\,\;\:\(\)\[\]\=\~\+\/]', line):
			line = re.sub(r'\n', 'annespaperheading', line)
			line = re.sub(r'(?P<first>^\s*[0-9]*\.?[0-9]*)', '\g<1>annessectionnumber', line)
	text.append(line.rstrip('\n'))
	
result = ' '.join(text)
result = re.sub(r'\s+', ' ', result)
if not re.search('the', result): #stop the whole thing if the text is essentially empty
	print('File ' + filename + ' has no text!\n', file=logfile)
	sys.exit(0)
	
try:
	sents = sentence_splitter.tokenize(result) 
except UnicodeDecodeError as ude:
	result = re.sub(r'(annespaperheading|annessectionnumber)', '', result)
	print(result, file=outfile)
	print('Non-decodable characters in file ' + filename + ', fallback to literal output!\n', file=logfile)
	sys.exit(0)
	
for sent in sents:
	if re.search('.- .', sent):#remove word division
		sent = re.sub(r'(?P<first>.)- (?P<second>.)', '\g<1>\g<2>', sent)
	if re.search('[A-Z][a-z]+[A-Z]', sent):
		sent = re.sub(r'(?P<first>[A-Z][a-z]+)(?P<second>[A-Z])', '\g<1> \g<2>', sent)
	if re.search('annespaperheading', sent): #restore headings
		sent = re.sub(r'annespaperheading', '\n', sent)
	if re.search('annessectionnumber', sent): #restore headings
		sent = re.sub(r'annessectionnumber', '', sent)
	sent = re.sub(r'[^0-9A-Za-z,;\.\!\?\-\(\)\r\n ]', '', sent) #get rid of non-alphanumeric characters
	if options.tagger==1:
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
	output.append(sent)
	
reworkedo = '\n'.join(output)	
	
if re.search('et al.\n', reworkedo): #remove incorrect splits that persisted from the start
	reworkedo = re.sub(r'\n\s', '\n', reworkedo)
	reworkedo = re.sub(r'et al\.\n', 'et al\. ', reworkedo)
	reworkedo = re.sub(r'(?P<figure>[fF]ig\.)\n', '\g<1> ', reworkedo)

print(reworkedo, file=outfile)
	
infile.close()
outfile.close()
logfile.close()