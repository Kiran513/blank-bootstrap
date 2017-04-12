

#1026 Assignment 2
#Paul Salvatore 250668447
#March 16, 2016



def findTimezone(coordinates):
    """
    Takes in a list of coordinates of the form: [latitude, longitude].
    Returns a string indicating which timezone these coordinates belong to.
    If the coordinates do not fall within any of the predefined timezones returns None.
    """
    if coordinates[0] > 49.189787 or coordinates[0] < 24.660845 or coordinates[1] > -67.444574 or coordinates[1] < -125.242264:
        return None
    elif -87.518395 < coordinates[1] <= -67.444574:
        return 'Eastern'
    elif -101.998892 < coordinates[1] <= 87.518395:
        return 'Central'
    elif -115.236428 < coordinates[1] <= -101.998892:
        return 'Mountain'
    else:
        return 'Pacific'


def readKeywords(file):
    """
    Reads a text file containing keywords of the form: 'word,value'
    Return a list of lists of the form: [[value, 'word'], [value, 'word'].. ]
    """
    formatedKeywords = []
    try:
        rawKeywords = open(file, 'r', encoding='utf-8')
        for line in rawKeywords:
            line = line.replace(',', ' ').lower().split()
            formatedKeywords += [[int(line[1]), line[0]],]
        return formatedKeywords
    except:
        return None


def readTweets(file):
    """
    Reads a text file containing tweets.
    Returns a dictionary with each timezone as an index assigned to a list of tweets corresponding to that timezone.
    Ignores any tweets that are not within the specified timezones.
    """
    try:
        sortedTweets = {'Eastern':[], 'Pacific':[], 'Mountain':[], 'Central':[]}
        rawTweets = open(file, 'r', encoding='utf-8')

        #Calls the findTimezone function with the coordinates so it knows which index to assign the tweet to,
        #If it is not in a timezone, the tweet is skipped.
        for line in rawTweets:
            timezone = findTimezone([float(line.split()[0][1:-1]), float(line.split()[1][0:-1])])
            if timezone == None:
                continue
            else:
                sortedTweets[timezone] += [line.lower(),]

        return sortedTweets

    except:
        return None


def hasKeyword(tweet, keywords):
    """
    Takes in a single tweet and the list of keywords and their associated values.
    Checks to see if a tweet contains any of the keywords, if so returns True, else returns False.
    """
    for word in keywords:
        if word[1] in tweet:
            return True
    return False


def findSentiment(tweets, keywords):
    """
    Takes in all of the tweets for a single timezone and a list containing the keywords and their associated values.
    Returns the average sentiment score for the timezone, ignoring tweets that do not contain any key words.
    """

    numWithKeywords = 0
    zoneHappinessScore = 0

    for tweet in tweets:
        happinessScore = 0
        keywordsFound = 0

        #Checks to see if there are any key words at all using the hasKeyword function,
        #If not the tweet is skipped, keeps track of how many tweets have key words in them:
        if hasKeyword(tweet, keywords):
            numWithKeywords += 1

            #Begins summing the sentiment scores for each keyword found,
            #Keeps track of how many key words are found in each tweet
            for word in keywords:
                if word[1] in tweet:
                    keywordsFound += 1
                    happinessScore += word[0]

            #Adds the average sentiment score/keyword for this tweet to the total sentiment score.
            zoneHappinessScore += happinessScore/keywordsFound

    if numWithKeywords == 0:
        return 0
    else:
        return zoneHappinessScore/numWithKeywords


def findHappiness():
    """
    Prompts user for files containing tweets and a list of keywords and their associated values.
    Uses the keywords to find the average sentiment values of the tweets divided by their respective timezones.
    Prints out the number of tweets in each timezone, the average sentiment value of that timezone, and the timezone with this highest score.
    """

    #Takes the file input and calls the appropriate functions to the read the files,
    #While checking to ensure the inputs are valid before continuing:
    tweetsInput = input('What is the name of the tweets file?')
    sortedTweets = readTweets(tweetsInput)
    if sortedTweets == None:
        print("Invalid Tweet File Input")
        return None

    keysInput = input('What is the name of the keywords file?')
    KEYWORDS = readKeywords(keysInput)
    if KEYWORDS == None:
        print("Invalid Keywords File Input")
        return None

    #Calls the findSentiment function to generate a list with the average scores for each timezone:
    sentiments = []
    happiest = None
    for timezone in sortedTweets:
        zoneSentiment = findSentiment(sortedTweets[timezone], KEYWORDS)
        if happiest == None:
            happiest = (timezone, zoneSentiment)
        elif zoneSentiment > happiest[1]:
            happiest = (timezone, zoneSentiment)
        sentiments += [(timezone, round(zoneSentiment, 3)),]

    #Prints the resultant sentiment scores and the number of tweets from each region:
    for timezone in sentiments:
        print ('\n', '%30s' %('Sentiment score in ' + timezone[0] + ':'), timezone[1])
        print ('%31s' %('Number of tweets from ' + timezone[0] + ':'), len(sortedTweets[timezone[0]]))

    print ('\nHappiest timezone:', happiest[0])

findHappiness()


