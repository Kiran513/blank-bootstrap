

#1026 Assignment 1
#Paul Salvatore 250668447
#February 4, 2016

#I found it easier to use my test cases if I implemented the program as a function, I hope that is okay.




#Parameters my program must adhere to:
    #Cost:
        #Budget: cost of $30/day and 0.25/km.
        #Day: cost of $50/day and charge of 0.25/km for every km over 100km/day.
        #Week: cost of $200/week and charge extra 50/week if you drive 1000 - 2000km/week, extra 100/week if you drive >2000km/week and 0.25/km over for each km over 2000/week.
        #Age: if you're under 25 extra cost of $10/day.

    #Need to display how many km were driven and what the cost is, as well as all of the input information.



#For my test cases I made the components of cost apart of the parameters of the cost function and commented out the inputs for testing purposes.
def test():

    print ('\nBudget under 25:')
    cost('Test', 24, 'B', 10, 0, 100)
    print ('\nBudget over 25:')
    cost('Test', 25, 'b', 10, 0, 100)
    print ('\nBudget over 25 (wrong code):')
    cost('Test', 25, 'P', 10, 0, 100)
    print ('\n')

    print ('\nDay under 100 km:')
    cost('Test', 25, 'd', 1, 0, 100)
    print ('\nDay over 100 km (by 100 km):')
    cost('Test', 25, 'd', 1, 0, 200)
    print ('\n')

    print ('\nWeek under 1000 km:')
    cost('Test', 25, 'w', 7, 0, 100)
    print ('\nWeek under 1000 km (round up to two weeks):')
    cost('Test', 25, 'w', 8, 0, 100)
    print ('\nWeek under 1000 km (two weeks:)')
    cost('Test', 25, 'w', 14, 0, 100)
    print ('\nWeek 1000 < km < 2000:')
    cost('Test', 25, 'w', 7, 0, 1100)
    print ('\nWeek 1000 < km < 2000 (two weeks):')
    cost('Test', 25, 'w', 14, 0, 1100)
    print ('\nWeek over 2000 km (by 100 km):')
    cost('Test', 25, 'w', 7, 0, 2100)
    print ('\nWeek over 2000 km (2 weeks, 2100 km):')
    cost('Test', 25, 'w', 14, 0, 2100)
    print ('\nWeek over 2000 km (2.5 weeks, 2100 km):')
    cost('Test', 25, 'w', 17, 0, 2100)





#To check my test cases I uncomment the line below and comment out the next several lines until the end of the inputs, as marked below.
# def cost(name, age, code, DAYS, START, END):
def cost():

    #The program first takes the user input for each of the required fields:
    name = input('What is your name?')
    age = int(input('What is your age?'))
    code = input('What is your classification code?')
    DAYS = int(input('How long (in days) will you be taking the car?'))
    START = int(input('What is the starting odometer reading?'))
    END = int(input('What is the final odometer reading?'))                     #This is the last line I comment out when testing.

    #The program than checks that the code inputs are valid, if not it ends the program.
    code = code.lower()
    if code != 'b' and code != 'd' and code != 'w':
        print('\n' + '%-21s' %(name), " Age: " + str(age))
        print((code.upper() + ' is not a valid code.'))
        return None

    distanceDriven = END - START
    overDistance = 0

    #Calculating cost based on the code:
    if code == 'b':
        cost = 30.00*DAYS + 0.25*distanceDriven
    elif code == 'd':
        if (distanceDriven/DAYS) > 100:                 #Checks if distance driven/day is over 100, if so will find by how much.
            overDistance = distanceDriven - 100*DAYS
        cost = 50.00*DAYS + 0.25*overDistance
    else:

        #Finds how many weeks they are renting it for, rounds up for fractions of a week.
        WEEKS = DAYS/7.0
        if WEEKS%1.0 != 0:
            WEEKS += 1
            WEEKS = int(WEEKS)

        if (distanceDriven/WEEKS) > 2000:               #Checks if distance driven/week is over 2000, if so will find by how much.
            overDistance = distanceDriven - 2000*WEEKS
        cost = WEEKS*200.00 + 0.25*overDistance

        #These two if statements checks if extra fees/week need be added.
        if 1000 < (distanceDriven/WEEKS) < 2000:
            cost = cost + 50*WEEKS
        if (distanceDriven/WEEKS) > 2000:
            cost = cost + 100*WEEKS

    #Accounting for those under the age of 25:
    if age < 25:
        cost = cost + DAYS*10.0

    #This code formats and prints the results:
    print('\n' + '%-23s' %(name), " Age: " + str(age))
    print('Code: ' + '%-18s' %(code.upper()), str(DAYS) + " day(s)")
    print('%-24s' %('Starting odometer:'), START)
    print('%-24s' %('Ending odometer:'), END)
    print('%-24s' %('Kilometers driven:'), distanceDriven)
    print('%-24s' %('Amount due:'), '$' + '%-10.2f' %(cost))

# test()

cost()


