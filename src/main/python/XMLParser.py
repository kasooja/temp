import untangle
from termcolor import colored


#"/home/kartik/PycharmProjects/Anne/resources/corpusDir/7/C04-1008_cln.xml"
#class XMLParser:

def extractText(xmlfile):
    obj = untangle.parse(xmlfile)
    text = []

    try:
        for section in obj.Paper.Section:
            try:
                for para in section.Paragraph:
                    text.append(para.cdata + "\n")
                    #print(para.cdata)
            except:
                print(colored("no Paragraphs in the Section!", 'red'))

            try:
                for subSection in section.Section:
                    for para in subSection.Paragraph:
                        #print(para.cdata)
                        text.append(para.cdata + "\n")
            except:
                print(colored("no Section inside Section!", 'red'))
    except:
        print(colored("no Text Inside!!!", 'red'))

    return ''.join(text)


xmlFile = "/home/kartik/PycharmProjects/Anne/resources/corpusDir/1/C65-1018_cln.xml"
extractText(xmlFile)